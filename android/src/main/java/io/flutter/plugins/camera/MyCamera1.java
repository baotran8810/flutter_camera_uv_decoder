package io.flutter.plugins.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.camera.types.ExposureMode;
import io.flutter.plugins.camera.types.FlashMode;
import io.flutter.plugins.camera.types.FocusMode;
import io.flutter.view.TextureRegistry.SurfaceTextureEntry;
import jp.co.uv.blind.helper.Decode;
import jp.co.uv.blind.helper.OpenCVHelper;

@SuppressWarnings("deprecation")
public class MyCamera1 implements OpenCVHelper.OnFocusListener {
  private Activity activity;
  private SurfaceTextureEntry surfaceTextureEntry;
  private final Context applicationContext;
  private final DartMessenger dartMessenger;
  private Camera mCamera;
  private OpenCVHelper mOpenCVHelper;

  static {
    System.loadLibrary("opencv_java3");
  }

  MyCamera1(Activity activity, SurfaceTextureEntry surfaceTextureEntry, final DartMessenger dartMessenger, String resolutionPreset) {
    this.activity = activity;
    this.applicationContext = activity.getApplicationContext();
    this.surfaceTextureEntry = surfaceTextureEntry;
    this.dartMessenger = dartMessenger;
  }

  public void open(String imageFormatGroup) {
    try {
      mCamera = Camera.open();
      mOpenCVHelper = new OpenCVHelper();

      Parameters parameters = mCamera.getParameters();
      setFocusMode(parameters);
      List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

      Size bestPreviewSize = getBestPreviewSize();

      parameters.setPreviewSize(bestPreviewSize.getWidth(), bestPreviewSize.getHeight());
      parameters.setWhiteBalance(Parameters.WHITE_BALANCE_AUTO);
      if (parameters.isVideoStabilizationSupported()) {
        parameters.setVideoStabilization(true);
      }
      parameters.setZoom(0);
      parameters.setExposureCompensation(0);

      mCamera.setParameters(parameters);
      mCamera.setDisplayOrientation(90);

      SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
      surfaceTexture.setDefaultBufferSize(bestPreviewSize.getWidth(), bestPreviewSize.getHeight());

      mCamera.setPreviewTexture(surfaceTexture);
      startPreview();
      this.dartMessenger.sendCameraInitializedEvent(bestPreviewSize.getWidth(), bestPreviewSize.getHeight(), ExposureMode.auto, FocusMode.auto, false, false);
    } catch (Exception exception) {
      Log.d("Exception Camera", "e: " + exception.toString());
    }
  }

  private Size getBestPreviewSize() {
    // TODO refactor
    Size bestSize1 = new Size(1920, 1080);
    Size bestSize2 = new Size(1280, 720);

    List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
    Camera.Size bestSizeFallback = sizeList.get(0);

    // Return if size match with bestSize, priority: bestSize1 => bestSize2
    for (int i = 0; i < sizeList.size(); i++) {
      int currentWidth = sizeList.get(i).width;
      int currentHeight = sizeList.get(i).height;

      if (currentWidth == bestSize1.getWidth() && currentHeight == bestSize1.getHeight()) {
        return bestSize1;
      } else if (currentWidth == bestSize2.getWidth() && currentHeight == bestSize2.getHeight()) {
        return bestSize2;
      }

      if (currentWidth * currentHeight > bestSizeFallback.width * bestSizeFallback.height) {
        bestSizeFallback = sizeList.get(i);
      }
    }

    // Fall back if none matches with bestSize1, bestSize2
    return new Size(bestSizeFallback.width, bestSizeFallback.height);
  }

  private void setFocusMode(Parameters parameters) {
    List<String> focusModes = parameters.getSupportedFocusModes();
    String manufactures = android.os.Build.MANUFACTURER;
    if (manufactures.equalsIgnoreCase(Manufacturer.SAMSUNG) && Build.VERSION.SDK_INT < 28) {
      if (focusModes != null && focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
      }

    } else {
      if (focusModes != null && focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
        parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
      }
    }
  }

  private boolean isOnFocus = false;
  private int mCountFocus = 0;
  private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
    @Override
    public void onAutoFocus(boolean arg0, Camera arg1) {
      // Assign isOnFocus to false after focus done
      isOnFocus = false;
      // Check if focus is success, if true, reset mCountFocus = 0, if false, increase it and focus again
      if (arg0) {
        mCountFocus = 0;
      } else {
        // If mCountFocus = 3, stop focus again
        if (mCountFocus < 3) {
          focus();
          mCountFocus++;
        } else {
          mCountFocus = 0;
        }
      }
    }
  };

  private void focus() {
    try {
      Parameters p = mCamera.getParameters();
      List<String> focusModes = p.getSupportedFocusModes();
      if (focusModes != null && focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
        if (!isOnFocus && mCamera != null) {
          isOnFocus = true;
          mCamera.cancelAutoFocus();
          Parameters parameters = mCamera.getParameters();
          parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
          mCamera.setParameters(parameters);
          if (!android.os.Build.MANUFACTURER.equals(Manufacturer.SONY)) {
            mCamera.autoFocus(new AutoFocusCallback() {
              @Override
              public void onAutoFocus(boolean success, Camera camera) {
                mCamera.cancelAutoFocus();
                Parameters params = mCamera.getParameters();
                params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(params);
                mCamera.autoFocus(myAutoFocusCallback);
              }
            });
          } else {
            mCamera.autoFocus(myAutoFocusCallback);

          }

        }
      }
    } catch (Exception e) {
    }
  }

  // Interface OpenCVHelper.OnFocusListener
  public void onFocus() {
    focus();
  }

  public void setFlashMode(@NonNull final MethodChannel.Result result, FlashMode mode) {
    switch (mode) {
      case off:
        updateFlashMode(Parameters.FLASH_MODE_OFF);
        break;
      case always:
        updateFlashMode(Parameters.FLASH_MODE_ON);
        break;
      case torch:
        updateFlashMode(Parameters.FLASH_MODE_TORCH);
        break;
      case auto:
        updateFlashMode(Parameters.FLASH_MODE_AUTO);
        break;
    }
    result.success(null);
  }

  private void updateFlashMode(final String flashModeStr) {
    Parameters parameters = mCamera.getParameters();
    List<String> flashModes = parameters.getSupportedFlashModes();
    if (flashModes != null && flashModes.contains(flashModeStr)) {
      parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
      mCamera.setParameters(parameters);
      parameters.setFlashMode(flashModeStr);
      mCamera.setParameters(parameters);
    }
  }

  public void startPreview() {
    mCamera.startPreview();
  }

  public void startPreviewWithImageStream(EventChannel imageStreamChannel) {
    imageStreamChannel.setStreamHandler(
            new EventChannel.StreamHandler() {
              @Override
              public void onListen(Object o, EventChannel.EventSink imageStreamSink) {
                startBackgroundThread();

                Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
                  @Override
                  public void onPreviewFrame(byte[] data, Camera camera) {
                    if (data == null) {
                      return;
                    }

                    if (isPrevent) return;
                    isPrevent = true;

                    ByteBuffer bufferY = ByteBuffer.wrap(data);
                    ByteBuffer buffer = ByteBuffer.allocateDirect(bufferY.capacity());
                    buffer.put(bufferY);
                    buffer.compact();

                    Camera.Parameters parameters = camera.getParameters();
                    final int width = parameters.getPreviewSize().width;
                    final int height = parameters.getPreviewSize().height;
                    mBackgroundHandler.post(new Runnable() {
                      @Override
                      public void run() {
                        processImage(buffer.array(), width, height, imageStreamSink);
                        System.gc();
                      }
                    });
                  }
                };

                mCamera.setPreviewCallback(mPreviewCallback);
              }

              @Override
              public void onCancel(Object o) {
                stopBackgroundThread();
                mCamera.setPreviewCallback(null);
              }
            });
  }

  protected int mLumiaAverage = 0;
  private int brightness = 0;
  private boolean isPrevent = false;

  private HandlerThread mBackgroundThread;
  private Handler mBackgroundHandler;

  protected void startBackgroundThread() {
    mBackgroundThread = new HandlerThread("Camera Background");
    mBackgroundThread.start();
    mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
  }

  protected void stopBackgroundThread() {
    if (mBackgroundHandler != null) {
      mBackgroundThread.quitSafely();

      try {
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void processImage(byte[] data, int width, int height, EventChannel.EventSink imageStreamSink) {
    byte[] buffer = Arrays.copyOfRange(data, 0, width * height);

    // Calculate the offset of square sideSquare x sideSquare
    int threshold = 0;
    int biggest = 0;
    int smallest = 255;

    // Convert to OpenCV
    Mat bufferFrame = new Mat(height, width, CvType.CV_8UC1);
    bufferFrame.put(0, 0, buffer);

    // saveImgLogMat("bufferFrame", bufferFrame);

    Scalar sca = Core.sumElems(bufferFrame);
    Core.MinMaxLocResult res = Core.minMaxLoc(bufferFrame);

    threshold = (int) sca.val[0];
    biggest = (int) res.maxVal;
    smallest = (int) res.minVal;

    mLumiaAverage = threshold / (bufferFrame.cols() * bufferFrame.rows());

    if (biggest - smallest < 130) {
      // Low brightness -> Increase contrast
      if (biggest - smallest > 0) {
        if (mLumiaAverage >= 170) {
          bufferFrame.convertTo(bufferFrame, -1, 1.7f, -100);
        } else {
          bufferFrame.convertTo(bufferFrame, -1, 1.7f, -60);
        }
      }
    }
    final int finalBiggest = biggest;
    final int finalSmallest = smallest;

    if (finalBiggest - finalSmallest > 255) {
      brightness = 1;
    } else {
      brightness = 0;
      if ((mLumiaAverage > 0 && mLumiaAverage < 70) || (finalBiggest > 0 && finalBiggest < 90)) {
        brightness = -1;
      } else {
        brightness = 0;
      }
    }

    Map<String, Object> points = new HashMap<>();
    Mat outputFrame = mOpenCVHelper.detectEdges(bufferFrame);
    if (outputFrame == null) {
      points.put("tlx", 0.0);
      points.put("tly", 0.0);
      points.put("trx", 0.0);
      points.put("try", 0.0);
      points.put("brx", 0.0);
      points.put("bry", 0.0);
      points.put("blx", 0.0);
      points.put("bly", 0.0);
      points.put("error", "No error");
      points.put("brightness", brightness);
      points.put("result", "");
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          imageStreamSink.success(points);
        }
      });
      isPrevent = false;
      return;
    }

    // Apply CLAHE (Contrast Limited Adaptive Histogram Equalization)
    CLAHE clahe = Imgproc.createCLAHE(2.0, new org.opencv.core.Size(8, 8));
    clahe.setClipLimit(2.0);
    clahe.apply(outputFrame, outputFrame);

    byte[] output = new byte[(int) (outputFrame.total() * outputFrame.channels())];
    outputFrame.get(0, 0, output);

    Decode a = new Decode(applicationContext, output, outputFrame.width(), outputFrame.height());
    String result = a.decodeImage(mLumiaAverage);

    // saveImgLogMat("outputFrame", outputFrame);

    if (mOpenCVHelper != null && mOpenCVHelper.mPointsOfRectangle != null && mOpenCVHelper.mPointsOfRectangle.length > 3) {
      points.put("tlx", mOpenCVHelper.mPointsOfRectangle[0].x);
      points.put("tly", mOpenCVHelper.mPointsOfRectangle[0].y);
      points.put("trx", mOpenCVHelper.mPointsOfRectangle[1].x);
      points.put("try", mOpenCVHelper.mPointsOfRectangle[1].y);
      points.put("brx", mOpenCVHelper.mPointsOfRectangle[2].x);
      points.put("bry", mOpenCVHelper.mPointsOfRectangle[2].y);
      points.put("blx", mOpenCVHelper.mPointsOfRectangle[3].x);
      points.put("bly", mOpenCVHelper.mPointsOfRectangle[3].y);
      points.put("error", "No error");
      points.put("brightness", brightness);

      if (result != null) {
        points.put("result", result);
        String codeHeader = a.getAppID();
        if (codeHeader != null) {
          points.put("codeHeader", codeHeader);
          Log.d("native", codeHeader);
        }
      } else {
        points.put("result", "");
      }
    }
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        imageStreamSink.success(points);
      }
    });
    isPrevent = false;
  }

  private void saveImgLogMat(String logType, Mat mat) {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    File path = applicationContext.getExternalFilesDir(null);
    File file = new File(path, logType + "_" + timeStamp + ".bmp");
    String filename = file.toString();
    Log.d("Decode", filename);

    if (!Imgcodecs.imwrite(filename, mat)) {
      Log.d("Decode", "error creating file:");
    }
  }

  public void close() {
    if (mCamera != null) {
      mCamera.stopPreview();
      mCamera.setPreviewCallback(null);
      mCamera.release();
      mCamera = null;
    }
  }

  public void dispose() {
    close();
    surfaceTextureEntry.release();
  }
}

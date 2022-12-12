// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camera;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.embedding.engine.systemchannels.PlatformChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugins.camera.CameraPermissions.PermissionsRegistry;
import io.flutter.plugins.camera.types.ExposureMode;
import io.flutter.plugins.camera.types.FlashMode;
import io.flutter.plugins.camera.types.FocusMode;
import io.flutter.view.TextureRegistry;
import java.util.HashMap;
import java.util.Map;

final class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {
  private final Activity activity;
  private final BinaryMessenger messenger;
  private final CameraPermissions cameraPermissions;
  private final PermissionsRegistry permissionsRegistry;
  private final TextureRegistry textureRegistry;
  private final MethodChannel methodChannel;
  private final EventChannel imageStreamChannel;
  private @Nullable MyCamera1 myCamera1;
  // private @Nullable MyCamera2 myCamera2;

  static {
    System.loadLibrary("opencv_java3");
  }

  MethodCallHandlerImpl(
      Activity activity,
      BinaryMessenger messenger,
      CameraPermissions cameraPermissions,
      PermissionsRegistry permissionsAdder,
      TextureRegistry textureRegistry) {
    this.activity = activity;
    this.messenger = messenger;
    this.cameraPermissions = cameraPermissions;
    this.permissionsRegistry = permissionsAdder;
    this.textureRegistry = textureRegistry;

    methodChannel = new MethodChannel(messenger, "plugins.flutter.io/camera");
    imageStreamChannel = new EventChannel(messenger, "plugins.flutter.io/camera/imageStream");
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    switch (call.method) {
      case "availableCameras":
        try {
          result.success(CameraUtils.getAvailableCameras(activity));
        } catch (Exception e) {
          handleException(e, result);
        }
        break;
      case "create":
        {
          if (myCamera1 != null) {
            myCamera1.close();
          }

          cameraPermissions.requestPermissions(
              activity,
              permissionsRegistry,
              false,
              (String errCode, String errDesc) -> {
                if (errCode == null) {
                  try {
                    instantiateCamera(call, result);
                  } catch (Exception e) {
                    handleException(e, result);
                  }
                } else {
                  result.error(errCode, errDesc, null);
                }
              });
          break;
        }
      case "initialize":
        {
          if (myCamera1 != null) {
            try {
              myCamera1.open(call.argument("imageFormatGroup"));
              result.success(null);
            } catch (Exception e) {
              handleException(e, result);
            }
          } else {
            result.error(
                "cameraNotFound",
                "Camera not found. Please call the 'create' method before calling 'initialize'.",
                null);
          }
          break;
        }
      // case "takePicture":
      //   {
      //     myCamera2.takePicture(result);
      //     break;
      //   }
      // case "prepareForVideoRecording":
      //   {
      //     // This optimization is not required for Android.
      //     result.success(null);
      //     break;
      //   }
      // case "startVideoRecording":
      //   {
      //     myCamera2.startVideoRecording(result);
      //     break;
      //   }
      // case "stopVideoRecording":
      //   {
      //     myCamera2.stopVideoRecording(result);
      //     break;
      //   }
      // case "pauseVideoRecording":
      //   {
      //     myCamera2.pauseVideoRecording(result);
      //     break;
      //   }
      // case "resumeVideoRecording":
      //   {
      //     myCamera2.resumeVideoRecording(result);
      //     break;
      //   }
      case "setFlashMode":
        {
          String modeStr = call.argument("mode");
          FlashMode mode = FlashMode.getValueForString(modeStr);
          if (mode == null) {
            result.error("setFlashModeFailed", "Unknown flash mode " + modeStr, null);
            return;
          }
          try {
            myCamera1.setFlashMode(result, mode);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      // case "setExposureMode":
      //   {
      //     String modeStr = call.argument("mode");
      //     ExposureMode mode = ExposureMode.getValueForString(modeStr);
      //     if (mode == null) {
      //       result.error("setExposureModeFailed", "Unknown exposure mode " + modeStr, null);
      //       return;
      //     }
      //     try {
      //       myCamera2.setExposureMode(result, mode);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "setExposurePoint":
      //   {
      //     Boolean reset = call.argument("reset");
      //     Double x = null;
      //     Double y = null;
      //     if (reset == null || !reset) {
      //       x = call.argument("x");
      //       y = call.argument("y");
      //     }
      //     try {
      //       myCamera2.setExposurePoint(result, x, y);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "getMinExposureOffset":
      //   {
      //     try {
      //       result.success(myCamera2.getMinExposureOffset());
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "getMaxExposureOffset":
      //   {
      //     try {
      //       result.success(myCamera2.getMaxExposureOffset());
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "getExposureOffsetStepSize":
      //   {
      //     try {
      //       result.success(myCamera2.getExposureOffsetStepSize());
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "setExposureOffset":
      //   {
      //     try {
      //       myCamera2.setExposureOffset(result, call.argument("offset"));
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "setFocusMode":
      //   {
      //     String modeStr = call.argument("mode");
      //     FocusMode mode = FocusMode.getValueForString(modeStr);
      //     if (mode == null) {
      //       result.error("setFocusModeFailed", "Unknown focus mode " + modeStr, null);
      //       return;
      //     }
      //     try {
      //       myCamera2.setFocusMode(result, mode);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "setFocusPoint":
      //   {
      //     Boolean reset = call.argument("reset");
      //     Double x = null;
      //     Double y = null;
      //     if (reset == null || !reset) {
      //       x = call.argument("x");
      //       y = call.argument("y");
      //     }
      //     try {
      //       myCamera2.setFocusPoint(result, x, y);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      case "startImageStream":
        {
          try {
            myCamera1.startPreviewWithImageStream(imageStreamChannel);
            result.success(null);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      case "stopImageStream":
        {
          try {
            myCamera1.startPreview();
            result.success(null);
          } catch (Exception e) {
            handleException(e, result);
          }
          break;
        }
      // case "getMaxZoomLevel":
      //   {
      //     assert myCamera2 != null;
      //
      //     try {
      //       float maxZoomLevel = myCamera2.getMaxZoomLevel();
      //       result.success(maxZoomLevel);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "getMinZoomLevel":
      //   {
      //     assert myCamera2 != null;
      //
      //     try {
      //       float minZoomLevel = myCamera2.getMinZoomLevel();
      //       result.success(minZoomLevel);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "setZoomLevel":
      //   {
      //     assert myCamera2 != null;
      //
      //     Double zoom = call.argument("zoom");
      //
      //     if (zoom == null) {
      //       result.error(
      //           "ZOOM_ERROR", "setZoomLevel is called without specifying a zoom level.", null);
      //       return;
      //     }
      //
      //     try {
      //       myCamera2.setZoomLevel(result, zoom.floatValue());
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "lockCaptureOrientation":
      //   {
      //     PlatformChannel.DeviceOrientation orientation =
      //         CameraUtils.deserializeDeviceOrientation(call.argument("orientation"));
      //
      //     try {
      //       myCamera2.lockCaptureOrientation(orientation);
      //       result.success(null);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      // case "unlockCaptureOrientation":
      //   {
      //     try {
      //       myCamera2.unlockCaptureOrientation();
      //       result.success(null);
      //     } catch (Exception e) {
      //       handleException(e, result);
      //     }
      //     break;
      //   }
      case "dispose":
        {
          if (myCamera1 != null) {
            myCamera1.dispose();
          }
          result.success(null);
          break;
        }
      default:
        result.notImplemented();
        break;
    }
  }

  void stopListening() {
    methodChannel.setMethodCallHandler(null);
  }

  private void instantiateCamera(MethodCall call, Result result) throws CameraAccessException {
    String resolutionPreset = call.argument("resolutionPreset");
    TextureRegistry.SurfaceTextureEntry flutterSurfaceTexture =
        textureRegistry.createSurfaceTexture();
    DartMessenger dartMessenger =
            new DartMessenger(
                    messenger, flutterSurfaceTexture.id(), new Handler(Looper.getMainLooper()));
    myCamera1 =
        new MyCamera1(
            activity,
            flutterSurfaceTexture,
            dartMessenger,
            resolutionPreset);

    // String cameraName = call.argument("cameraName");
    // boolean enableAudio = call.argument("enableAudio");
    // myCamera2 =
    //         new MyCamera2(
    //                 activity,
    //                 flutterSurfaceTexture,
    //                 dartMessenger,
    //                 cameraName,
    //                 resolutionPreset,
    //                 enableAudio);

    Map<String, Object> reply = new HashMap<>();
    reply.put("cameraId", flutterSurfaceTexture.id());
    result.success(reply);
  }

  // We move catching CameraAccessException out of onMethodCall because it causes a crash
  // on plugin registration for sdks incompatible with Camera2 (< 21). We want this plugin to
  // to be able to compile with <21 sdks for apps that want the camera and support earlier version.
  @SuppressWarnings("ConstantConditions")
  private void handleException(Exception exception, Result result) {
    if (exception instanceof CameraAccessException) {
      result.error("CameraAccess", exception.getMessage(), null);
      return;
    }

    // CameraAccessException can not be cast to a RuntimeException.
    throw (RuntimeException) exception;
  }
}

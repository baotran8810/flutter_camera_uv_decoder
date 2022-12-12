
package jp.co.uv.blind.helper;

import android.content.Context;
import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OpenCVHelper extends Imgproc {
	public interface OnFocusListener {
		public void onFocus();
	}

	public void setOnFocusListener(OnFocusListener onFocusListener) {
		mOnFocusListener = onFocusListener;
	}

	public static final String TAG = OpenCVHelper.class.getSimpleName();
	public MADrawRect _adjustRect = new MADrawRect();
	public OnFocusListener mOnFocusListener;
	public Point[] mPointsOfRectangle;
	public float mTouchPointx = -1;
	public float mTouchPointy = -1;
	private int retry = 0;

	public Mat findLargestRectangle(Context context, Mat original_image, int videoWidth, int videoHeight) {
		Mat imgSource = original_image.clone();
		// Mat untouched = original_image.clone();

		// Convert the image to black and white does (8 bit)
		if (retry == 1) {
			Imgproc.Canny(imgSource, imgSource, 50, 50);
			retry = 0;
		} else {
			Imgproc.Canny(imgSource, imgSource, 100, 255);
			retry++;
		}

//	    File path = new File(Environment.getExternalStorageDirectory() + "/Images/");
//		path.mkdirs();
//		File file = new File(path, "image" + System.currentTimeMillis() + "_afterCanny.png");
//		String filename = file.toString();
//		Imgcodecs.imwrite(filename, imgSource);

		// Apply Gaussian blur to smoothen lines of dots
		Imgproc.GaussianBlur(imgSource, imgSource, new Size(9, 9), 5);

//	    path = new File(Environment.getExternalStorageDirectory() + "/Images/");
//		path.mkdirs();
//		file = new File(path, "image" + System.currentTimeMillis() + "_afterBlur.png");
//		filename = file.toString();
//		Imgcodecs.imwrite(filename, imgSource);

		// Find the contours
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(imgSource, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//	    Imgproc.findContours(imgSource, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		if (contours.size() == 0) {
			mPointsOfRectangle = null;
			return null;
		}
		double maxArea = -1;
//	    int maxAreaIdx = -1;
		MatOfPoint temp_contour = contours.get(0); // The largest is at the index 0 for starting point
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		MatOfPoint2f maxCurve = new MatOfPoint2f();
		List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>();
		for (int idx = 0; idx < contours.size(); idx++) {
			temp_contour = contours.get(idx);
			double contourarea = Imgproc.contourArea(temp_contour);
			// Compare this contour to the previous largest contour found
			if (contourarea > maxArea) {
				// Check if this contour is a square
				MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
				int contourSize = (int) temp_contour.total();
				Imgproc.approxPolyDP(new_mat, approxCurve, contourSize * 0.09, true);
				if (approxCurve.total() == 4) {
					boolean codeInsideRectangle = true;
					// Restrict area detecting voice code by touch coordinates
					if (mTouchPointx != -1 && mTouchPointy != -1) {
						Log.d(TAG, "Touch point: " + mTouchPointx + " " + mTouchPointy);
						int hSize = videoWidth / 4;
						Log.d(TAG, "Half size = " + hSize);
						int previewSizeHeight = videoHeight - 1;
						int previewSizeWidth = videoWidth - 1;
						for (int i = 0; i < approxCurve.total(); i++) {
							if (previewSizeWidth - mTouchPointx < hSize) {
								if (mTouchPointy < previewSizeHeight / 2) {
									if (approxCurve.toArray()[i].x < previewSizeWidth - hSize * 2 || approxCurve.toArray()[i].y > previewSizeHeight * 0.8) {
										codeInsideRectangle = false;
										break;
									}
								} else {
									if (approxCurve.toArray()[i].x < previewSizeWidth - hSize * 2 || approxCurve.toArray()[i].y < previewSizeHeight * 0.2) {
										codeInsideRectangle = false;
										break;
									}
								}
							} else if (mTouchPointx < hSize) {
								if (mTouchPointy < previewSizeHeight / 2) {
									if (approxCurve.toArray()[i].x > hSize * 2 || approxCurve.toArray()[i].y > previewSizeHeight * 0.8) {
										codeInsideRectangle = false;
										break;
									}
								} else {
									if (approxCurve.toArray()[i].x > hSize * 2 || approxCurve.toArray()[i].y < previewSizeHeight * 0.2) {
										codeInsideRectangle = false;
										break;
									}
								}
							} else {
								if (mTouchPointy < previewSizeHeight / 2) {
									if (approxCurve.toArray()[i].x < mTouchPointx - hSize || approxCurve.toArray()[i].x > mTouchPointx + hSize || approxCurve.toArray()[i].y > previewSizeHeight * 0.8) {
										codeInsideRectangle = false;
										break;
									}
								} else {
									if (approxCurve.toArray()[i].x < mTouchPointx - hSize || approxCurve.toArray()[i].x > mTouchPointx + hSize || approxCurve.toArray()[i].y < previewSizeHeight * 0.2) {
										codeInsideRectangle = false;
										break;
									}
								}
							}
						}
					}
					if (codeInsideRectangle) {
						// Check width and height should be similarly equal
						Rect rectangle = boundingRect(temp_contour);

						int tempWidth = rectangle.width;
						int tempHeight = rectangle.height;

						double balance = (tempWidth * 1.0) / (tempHeight * 1.0);
						Log.d(TAG, "Balance = " + balance);

						if (balance >= 0.9 && balance <= 1.1) {
							maxCurve = approxCurve;
							maxArea = contourarea;
//			                maxAreaIdx = idx;
							largest_contours.add(temp_contour);
						}
					}
				}
			}
		}

		Log.d(TAG, "Number of squares = " + largest_contours.size());
		Log.d(TAG, "Number of contours = " + contours.size());

		if (maxCurve.toArray().length != 4) {
			mPointsOfRectangle = null;
			return null;
		}

		mPointsOfRectangle = maxCurve.toArray();

		if (contours.size() > 200) {
			mOnFocusListener.onFocus();
		}

		// Create the new image here using the largest detected square
//	    Mat new_image = new Mat(imgSource.size(), CvType.CV_8U); // We will create a new black blank image with the largest contour
//	    Imgproc.cvtColor(new_image, new_image, Imgproc.COLOR_BayerBG2RGB);
//	    Imgproc.drawContours(new_image, contours, maxAreaIdx, new Scalar(255, 255, 255), 1); // Will draw the largest square/rectangle

		double temp_double[] = maxCurve.get(0, 0);
		Point p1 = new Point(temp_double[0], temp_double[1]);
//	    Core.circle(new_image, new Point(p1.x, p1.y), 20, new Scalar(255, 0, 0), 5); // p1 is colored red
//	    String temp_string = "Point 1: (" + p1.x + ", " + p1.y + ")";

		temp_double = maxCurve.get(1, 0);
		Point p2 = new Point(temp_double[0], temp_double[1]);
//	    Core.circle(new_image, new Point(p2.x, p2.y), 20, new Scalar(0, 255, 0), 5); // p2 is colored green
//	    temp_string += "\nPoint 2: (" + p2.x + ", " + p2.y + ")";

		temp_double = maxCurve.get(2, 0);
		Point p3 = new Point(temp_double[0], temp_double[1]);
//	    Core.circle(new_image, new Point(p3.x, p3.y), 20, new Scalar(0, 0, 255), 5); // p3 is colored blue
//	    temp_string += "\nPoint 3: (" + p3.x + ", " + p3.y + ")";

		temp_double = maxCurve.get(3, 0);
		Point p4 = new Point(temp_double[0], temp_double[1]);
//	    Core.circle(new_image, new Point(p4.x, p4.y), 20, new Scalar(0, 255, 255), 5); // p1 is colored violet
//	    temp_string += "\nPoint 4: (" + p4.x + ", " + p4.y + ")";

//	    Log.d("Find successful", temp_string);

		Mat undistorted = new Mat(new Size(Def.STANDARD_SIZE, Def.STANDARD_SIZE), CvType.CV_8UC1);
		Point pa = new Point(0, 0);
		Point pb = new Point(Def.STANDARD_SIZE - 1, 0);
		Point pc = new Point(Def.STANDARD_SIZE - 1, Def.STANDARD_SIZE - 1);
		Point pd = new Point(0, Def.STANDARD_SIZE - 1);
		Point tl, tr, br, bl;
		int d = 20;
		if (p1.x > p3.x) {
			tl = new Point(p3.x - d, p3.y + d);
			tr = new Point(p4.x + d, p4.y + d);
			br = new Point(p1.x + d, p1.y - d);
			bl = new Point(p2.x - d, p2.y - d);
		} else {
			tl = new Point(p2.x - d, p2.y + d);
			tr = new Point(p3.x + d, p3.y + d);
			br = new Point(p4.x + d, p4.y - d);
			bl = new Point(p1.x - d, p1.y - d);
		}

		warpPerspective(original_image, undistorted, getPerspectiveTransform(
				new MatOfPoint2f(new Point[]{bl, br, tr, tl}),
				new MatOfPoint2f(new Point[]{pa, pb, pc, pd})),
				new Size(Def.STANDARD_SIZE, Def.STANDARD_SIZE));

//		path = new File(Environment.getExternalStorageDirectory() + "/Images/");
//		path.mkdirs();
//		file = new File(path, "image" + System.currentTimeMillis() + "_afterWrap.png");
//		filename = file.toString();
//		Imgcodecs.imwrite(filename, undistorted);

		return undistorted;
	}

	public Mat detectEdges(Mat orginal) {
		List<List<Point>> squares = new ArrayList<>();
		List<Point> largestSquare = new ArrayList<>();
		Mat blurredImg = orginal.clone();

		findSquares(blurredImg, squares);
		if (squares.size() > 0) {
			largestSquare = squares.get(0);
		}

		if (largestSquare != null && largestSquare.size() == 4) {
			List<Map<String, Object>> points = new ArrayList<Map<String, Object>>();
			Map<String, Point> sortedPoints = new HashMap<String, Point>();

			double min = largestSquare.get(0).x + largestSquare.get(0).y;
			double max = largestSquare.get(0).x + largestSquare.get(0).y;

			for (int i = 0; i < 4; i++) {
				Map<String, Object> dic = new HashMap<String, Object>();
				dic.put("point", new Point(largestSquare.get(i).x, largestSquare.get(i).y));
				dic.put("value", largestSquare.get(i).x + largestSquare.get(i).y);
				points.add(dic);
				double minI = largestSquare.get(i).x + largestSquare.get(i).y;
				double maxI = largestSquare.get(i).x + largestSquare.get(i).y;
				if (minI < min) min = minI;
				if (maxI > max) max = maxI;
			}

			int minIndex = 0;
			int maxIndex = 0;

			int missingIndexOne = 0;
			int missingIndexTwo = 0;

			for (int i = 0; i < 4; i++) {
				Map<String, Object> dic = points.get(i);
				if ((Double) dic.get("value") == min) {
					sortedPoints.put("0", (Point) dic.get("point"));
					minIndex = i;
					if (sortedPoints.get("2") != null) {
						continue;
					}
				}
				if ((Double) dic.get("value") == max) {
					sortedPoints.put("2", (Point) dic.get("point"));
					maxIndex = i;
					continue;
				}
				missingIndexOne = i;
			}

			for (int i = 0; i < 4; i++) {
				if (missingIndexOne != i && minIndex != i && maxIndex != i) {
					missingIndexTwo = i;
				}
			}

			if (largestSquare.get(missingIndexOne).x < largestSquare.get(missingIndexTwo).x) {
				// 2nd Point Found
				sortedPoints.put("3", (Point) points.get(missingIndexOne).get("point"));
				sortedPoints.put("1", (Point) points.get(missingIndexTwo).get("point"));
			} else {
				// 4rd Point Found
				sortedPoints.put("1", (Point) points.get(missingIndexOne).get("point"));
				sortedPoints.put("3", (Point) points.get(missingIndexTwo).get("point"));
			}

			double x1, x2, x3, x4, y1, y2, y3, y4;
			int d = 0; // don't need extend here
			Point tl, tr, bl, br;
			x1 = ((Point) sortedPoints.get("0")).x;
			x2 = ((Point) sortedPoints.get("1")).x;
			x3 = ((Point) sortedPoints.get("2")).x;
			x4 = ((Point) sortedPoints.get("3")).x;

			y1 = ((Point) sortedPoints.get("0")).y;
			y2 = ((Point) sortedPoints.get("1")).y;
			y3 = ((Point) sortedPoints.get("2")).y;
			y4 = ((Point) sortedPoints.get("3")).y;

			tl = new Point(x1 - d, y1 - d);
			tr = new Point(x2 + d, y2 - d);
			br = new Point(x3 + d, y3 + d);
			bl = new Point(x4 - d, y4 + d);

			_adjustRect.topLeftCornerToCGPoint(tl);
			_adjustRect.topRightCornerToCGPoint(tr);
			_adjustRect.bottomLeftCornerToCGPoint(bl);
			_adjustRect.bottomRightCornerToCGPoint(br);

			// Save images
//			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//			File path = new File(Environment.getExternalStorageDirectory() + "/Images/");
//			path.mkdirs();
//			File file = new File(path, "image" + timeStamp + "_orginal.png");
//			String filename = file.toString();
//			Highgui.imwrite(filename, orginal);
			// Save images

			return confirmedImage(orginal);
		}

		orginal.release();
		return null;
	}

	private Mat confirmedImage(Mat original) {
		float scaleFactor =  1.0f;
		Point ptBottomLeft = _adjustRect.coordinatesForPoint(1, scaleFactor);
		Point ptBottomRight = _adjustRect.coordinatesForPoint(2, scaleFactor);
		Point ptTopRight = _adjustRect.coordinatesForPoint(3, scaleFactor);
		Point ptTopLeft = _adjustRect.coordinatesForPoint(4, scaleFactor);

		float w1 = (float) Math.sqrt(Math.pow(ptBottomRight.x - ptBottomLeft.x, 2) + Math.pow(ptBottomRight.x - ptBottomLeft.x, 2));
		float w2 = (float) Math.sqrt(Math.pow(ptTopRight.x - ptTopLeft.x, 2) + Math.pow(ptTopRight.x - ptTopLeft.x, 2));

		float h1 = (float) Math.sqrt(Math.pow(ptTopRight.y - ptBottomRight.y, 2) + Math.pow(ptTopRight.y - ptBottomRight.y, 2));
		float h2 = (float) Math.sqrt(Math.pow(ptTopLeft.y - ptBottomLeft.y, 2) + Math.pow(ptTopLeft.y - ptBottomLeft.y, 2));

		float maxWidth = (w1 < w2) ? w1 : w2;
		float maxHeight = (h1 < h2) ? h1 : h2;

		Point[] src = new Point[4];
		Point[] dst = new Point[4];
		src[0] = new Point();
		src[0].x = ptTopLeft.x;
		src[0].y = ptTopLeft.y;
		src[1] = new Point();
		src[1].x = ptTopRight.x;
		src[1].y = ptTopRight.y;
		src[2] = new Point();
		src[2].x = ptBottomRight.x;
		src[2].y = ptBottomRight.y;
		src[3] = new Point();
		src[3].x = ptBottomLeft.x;
		src[3].y = ptBottomLeft.y;

		dst[0] = new Point();
		dst[0].x = 0;
		dst[0].y = 0;
		dst[1] = new Point();
		dst[1].x = maxWidth - 1;
		dst[1].y = 0;
		dst[2] = new Point();
		dst[2].x = maxWidth - 1;
		dst[2].y = maxHeight - 1;
		dst[3] = new Point();
		dst[3].x = 0;
		dst[3].y = maxHeight - 1;

		Mat undistorted = new Mat(new Size(maxWidth, maxHeight), CvType.CV_8UC1);

		warpPerspective(original, undistorted,
				getPerspectiveTransform(new MatOfPoint2f(src), new MatOfPoint2f(dst)), new Size(maxWidth, maxHeight));
		original.release();

		return undistorted;
	}

	// Convert point2f list to Point2i list
	private List<Point> point2fsToPoint2is(List<Point> vecs) {
		List<Point> points = new ArrayList<>();
		int count = vecs.size();
		for (int i = 0; i < count; i++) {
			points.add(new Point((int) vecs.get(i).x, (int) vecs.get(i).y));
		}
		return points;
	}

	// Convert point2i list to Point2f list
	private List<Point> point2isToPoint2fs(List<Point> vecs) {
		List<Point> points = new ArrayList<>();
		int count = vecs.size();
		for (int i = 0; i < count; i++) {
			points.add(new Point(vecs.get(i).x * 1f, vecs.get(i).y * 1f));
		}
		return points;
	}

	// Length of vector
	private float vecLength(Point vec) {
		return (float) Math.sqrt(vec.x * vec.x + vec.y * vec.y);
	}

	// Normalize vector
	private void vecNormalize(Point vec) {
		float length = vecLength(vec);
		if (length < 1.19209290E-07F) {
			return;
		}
		float invLength = 1.0f / length;
		vec.x *= invLength;
		vec.y *= invLength;
	}

	// Dot product
	private float vecDot(Point a, Point b) {
		return (float) (a.x * b.x + a.y * b.y);
	}

	// Reduce points with small angle from triple points
	private void reducePoints(List<Point> points) {
		int i = 0;
		while (i < points.size() - 2) {
			Point p0 = points.get(i);
			Point p1 = points.get(i + 1);
			Point p2 = points.get(i + 2);
			Point n1 = new Point(p0.x - p1.x, p0.y - p1.y);
			Point n2 = new Point(p2.x - p1.x, p2.y - p1.y);
			vecNormalize(n1);
			vecNormalize(n2);
			if (vecDot(n1, n2) < -0.966f) {
				points.remove(i + 1);
			} else {
				i++;
			}
		}
	}

	// Find intersect point between two line
	boolean findIntersectPoint(Point A, Point B, Point C, Point D, Point result) {
		float A0 = (float) (A.y - B.y);
		float B0 = (float) (B.x - A.x);
		float C0 = - (float) (A.x * A0 + A.y * B0);

		float A1 = (float) (C.y - D.y);
		float B1 = (float) (D.x - C.x);
		float C1 = - (float) (C.x * A1 + C.y * B1);

		float d = A0 * B1 - B0 * A1;
		if (d != 0) {
			result.x = (B0 * C1 - C0 * B1) / d;
			result.y = (C0 * A1 - A0 * C1) / d;
			return true;
		}
		return false;
	}

	private List<Point> findSquaresPoints(List<Point> hullPoints) {
		List<List<Point>> maxPairList = new ArrayList<>();
		List<Float> maxList = new ArrayList<>(); // list of length between 2 continuous points
		List<Integer> maxIDList = new ArrayList<>(); // list of id between 2 continuous points
		hullPoints.add(hullPoints.get(0)); // loop the hullPoints list
		int hullPointsCount = hullPoints.size();
		for (int i = 0; i < hullPointsCount - 1; i++) {
			Point hullPoint1 = hullPoints.get(i);
			Point hullPoint2 = hullPoints.get(i + 1);
			float length = vecLength(new Point(hullPoint1.x - hullPoint2.x, hullPoint1.y - hullPoint2.y));
			if (maxList.size() == 0) {
				maxList.add(length);
				maxIDList.add(i);
				List<Point> pair = new ArrayList<>();
				pair.add(hullPoint1);
				pair.add(hullPoint2);
				maxPairList.add(pair);
			} else {
				// Make the list in order with maximum 4 point
				int maxListCount = maxList.size();
				for (int j = 0; j < maxListCount; j++) {
					if (length > maxList.get(j)) {
						maxList.add(j, length);
						maxIDList.add(j, i);
						List<Point> pair = new ArrayList<>();
						pair.add(hullPoint1);
						pair.add(hullPoint2);
						maxPairList.add(j, pair);
						break;
					} else if (maxListCount < 4 && j == maxListCount - 1) {
						maxList.add(length);
						maxIDList.add(i);
						List<Point> pair = new ArrayList<>();
						pair.add(hullPoint1);
						pair.add(hullPoint2);
						maxPairList.add(pair);
					}
				}
				if (maxList.size() > 4) {
					maxList.remove(maxList.size() - 1);
					maxIDList.remove(maxIDList.size() - 1);
					maxPairList.remove(maxPairList.size() - 1);
				}
			}
		}

		List<Point> points = new ArrayList<>();
		if (maxPairList.size() != 4) return points; // not enough 4 point

		// sort id to make all edge in to order
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 4; j++) {
				if (maxIDList.get(j) < maxIDList.get(i)) {
					int temp0 = maxIDList.get(j);
					maxIDList.set(j, maxIDList.get(i));
					maxIDList.set(i, temp0);
					List<Point> temp1 = maxPairList.get(j);
					maxPairList.set(j, maxPairList.get(i));
					maxPairList.set(i, temp1);
				}
			}
		}

		// extend edge a little to get more precise
		float extend = 3;
		for (int i = 0; i < 4; i++) {
			Point point0 = maxPairList.get(i).get(0);
			Point point1 = maxPairList.get(i).get(1);
			Point normal = new Point(point1.x - point0.x, point1.y - point0.y);
			vecNormalize(normal);
			normal = new Point(normal.y * extend, -normal.x * extend);
			point0.x += normal.x;
			point0.y += normal.y;
			point1.x += normal.x;
			point1.y += normal.y;
			List<Point> pair = new ArrayList<>();
			pair.add(point0);
			pair.add(point1);
			maxPairList.set(i, pair);
		}

		// calculate square points
		Point p1 = new Point(), p2 = new Point(), p3 = new Point(), p4 = new Point();
		if (!findIntersectPoint(maxPairList.get(0).get(0), maxPairList.get(0).get(1), maxPairList.get(1).get(0), maxPairList.get(1).get(1), p1)) return points;
		if (!findIntersectPoint(maxPairList.get(1).get(0), maxPairList.get(1).get(1), maxPairList.get(2).get(0), maxPairList.get(2).get(1), p2)) return points;
		if (!findIntersectPoint(maxPairList.get(2).get(0), maxPairList.get(2).get(1), maxPairList.get(3).get(0), maxPairList.get(3).get(1), p3)) return points;
		if (!findIntersectPoint(maxPairList.get(3).get(0), maxPairList.get(3).get(1), maxPairList.get(0).get(0), maxPairList.get(0).get(1), p4)) return points;
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);

		// do some filter to make sure this is square point
		MatOfPoint matPoints = new MatOfPoint();
		matPoints.fromList(points);
		if (!isContourConvex(matPoints)) points.clear();
		if (points.size() > 0) {
			maxList.clear();
			maxList.add(vecLength(new Point(p2.x - p1.x, p2.y - p1.y)));
			maxList.add(vecLength(new Point(p3.x - p2.x, p3.y - p2.y)));
			maxList.add(vecLength(new Point(p4.x - p3.x, p4.y - p3.y)));
			maxList.add(vecLength(new Point(p1.x - p4.x, p1.y - p4.y)));
			float minLength = maxList.get(0), maxLength = maxList.get(0);
			for (int i = 1; i < 4; i++) {
				if (maxList.get(i) < minLength) {
					minLength = maxList.get(i);
				} else if (maxList.get(i) > maxLength) {
					maxLength = maxList.get(i);
				}
			}
			if (maxLength > 1.5f * minLength) {
				points.clear();
			}
		}
		if (points.size() > 0) {
			float intersectRatio = vecLength(new Point(p3.x - p1.x, p3.y - p1.y)) / vecLength(new Point(p4.x - p2.x, p4.y - p2.y));
			if (intersectRatio < 1) intersectRatio = 1 / intersectRatio;
			if (intersectRatio > 1.5f) points.clear();
		}

		return points;
	}

	int MAX_VALUABLE_CONTOUR = 4; // maximum number of contour valuable for checking
	List<List<Point>> _backingSquares = new ArrayList<>();
	private void findSquares(Mat image, List<List<Point>> squares) {
		if (image == null) {
			return;
		}

		// In case there is still potential region of qr-code then get result from _backingSquares
		if (_backingSquares.size() > 0) {
			mPointsOfRectangle = _backingSquares.get(0).toArray(new Point[_backingSquares.get(0).size()]);
			squares.add(_backingSquares.get(0));
			_backingSquares.remove(0);
			return;
		}

		// Blur will enhance edge detection
		Imgproc.GaussianBlur(image, image, new Size(11, 11), 5, 0);

		// Use Canny instead of zero threshold level!
		// Canny helps to catch squares with gradient shading
		Canny(image, image, 10, 20, 3, false);

		// Dilate helps to remove potential holes between edge segments
		dilate(image, image, new Mat(), new Point(-1, -1), 3);

		// Find contours and store them in a list
		// [IVC][LuanTran] RETR_EXTERNAL or RETR_LIST
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		if (contours.size() == 0) {
			mPointsOfRectangle = null;
		}

		// Find contour which pretty like square
		List<Float> contoursArea = new ArrayList<>();
		int contoursCount = contours.size();

		for (int i = contoursCount - 1; i >= 0; i--) {
			float area = (float) Imgproc.contourArea(contours.get(i));
			RotatedRect trackBox = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
			float ratio = (float) (trackBox.size.width / trackBox.size.height);
			if (ratio < 1) ratio = 1 / ratio;
			// Filter rect too small
			if (ratio < 1.5f && area > 8000) {
				contoursArea.add(0, area);
			} else {
				contours.remove(i);
			}
		}

		// find MAX_VALUABLE_CONTOUR contour to check
		int maxValuableContour = MAX_VALUABLE_CONTOUR;
		contoursCount = contours.size();
		if (maxValuableContour > contoursCount) maxValuableContour = contoursCount;
		for (int i = 0; i < maxValuableContour; i++) {
			float maxArea = contoursArea.get(i);
			int maxAreaId = i;
			for (int j = i + 1; j < contoursCount; j++) {
				if (contoursArea.get(j) > maxArea) {
					maxArea = contoursArea.get(j);
					maxAreaId = j;
				}
			}
			float temp0 = contoursArea.get(maxAreaId);
			contoursArea.set(maxAreaId, contoursArea.get(i));
			contoursArea.set(i, temp0);
			MatOfPoint temp1 = contours.get(maxAreaId);
			contours.set(maxAreaId, contours.get(i));
			contours.set(i, temp1);
		}

		// Find square points of valid contour
		for (int i = 0; i < maxValuableContour; i++) {
			MatOfPoint contour = contours.get(i);
			MatOfInt hullIndexes = new MatOfInt();
			convexHull(contour, hullIndexes, false);
			List<Point> hullPoints = new ArrayList<>();
			int hullPointsCount = hullIndexes.toArray().length;
			for (int j = 0; j < hullPointsCount; j++) {
				hullPoints.add(contour.toArray()[hullIndexes.toArray()[j]]);
			}
			// Reduce points with small angle from triple points
			reducePoints(hullPoints);
			// Find square points
			List<Point> squarePoints = findSquaresPoints(hullPoints);
			if (squarePoints.size() != 4) {
				mPointsOfRectangle = null;
				continue; // not enough points
			}
			_backingSquares.add(point2fsToPoint2is(squarePoints)); // squarePoints is potential for qr-code
		}

		// Get first item to check qr-code
		if (_backingSquares.size() > 0) {
			mPointsOfRectangle = _backingSquares.get(0).toArray(new Point[_backingSquares.get(0).size()]);
			squares.add(_backingSquares.get(0));
			_backingSquares.remove(0);
		}

		if (contours.size() > 200) {
			mOnFocusListener.onFocus();
		}
	}
}
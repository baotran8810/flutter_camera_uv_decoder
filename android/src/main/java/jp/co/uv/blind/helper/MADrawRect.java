package jp.co.uv.blind.helper;

import org.opencv.core.Point;

public class MADrawRect {
	public Point a, b, c, d;
	
	public void bottomLeftCornerToCGPoint(Point point) {
	    a = point;
	}

	public void bottomRightCornerToCGPoint(Point point) {
	    b = point;
	}

	public void topRightCornerToCGPoint(Point point) {
	    c = point;
	}

	public void topLeftCornerToCGPoint(Point point) {
	    d = point;
	}
	
	public Point coordinatesForPoint(int point, float scaleFactor) {
		Point tmp = new Point(0, 0);
	    
	    switch (point) {
	        case 1:
	            tmp = new Point(a.x / scaleFactor, a.y / scaleFactor);
	            break;
	        case 2:
	            tmp = new Point(b.x / scaleFactor, b.y / scaleFactor);
	            break;
	        case 3:
	            tmp = new Point(c.x / scaleFactor, c.y / scaleFactor);
	            break;
	        case 4:
	            tmp = new Point(d.x / scaleFactor, d.y / scaleFactor);
	            break;
	    }
	    	    
	    return tmp;
	}
}
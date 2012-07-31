package de.bundestag.android.customviews;

import android.view.MotionEvent;

public interface CustomScrollViewGestureInterceptor {

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
	
	public boolean onSingleTapConfirmed(MotionEvent e);
	
}

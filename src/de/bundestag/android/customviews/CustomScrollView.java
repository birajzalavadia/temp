package de.bundestag.android.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

	/**
	 * This private gestureDetector will be used to intercept touch events of
	 * default scroll view. This will be implemented outside of this class's
	 * context.
	 */
	private GestureDetector gestureDetector;

	/**
	 * All the touch gestures will be intercepted and forwarded to this
	 * instance, this instance is null, custom scrollview will react as normal
	 * scrollview and will handle all the gestures in default manner.
	 */
	private CustomScrollViewGestureInterceptor targetForCallback;

	public CustomScrollView(Context context) {
		super(context);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (gestureDetector != null) {
			if (!gestureDetector.onTouchEvent(ev))
				return super.onTouchEvent(ev);
			else
				return true;
		} else {
			return super.onTouchEvent(ev);
		}
		// return super.onTouchEvent(ev);
		// return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (gestureDetector != null) {
			if (!gestureDetector.onTouchEvent(ev))
				return super.onInterceptTouchEvent(ev);
			else
				return true;
		} else {
			return super.onInterceptTouchEvent(ev);
			// return false;
		}

	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	public CustomScrollViewGestureInterceptor getTargetForCallback() {
		return targetForCallback;
	}

}

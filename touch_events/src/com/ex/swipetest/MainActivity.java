package com.ex.swipetest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
//import android.widget.Toast;

public class MainActivity extends Activity {
	int x1, x2;
	int y1, y2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// onTouchEvent () method gets called when User performs any touch event on
	// screen
	// Method to handle touch event like left to right swap and right to left
	// swap
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		// when user first touches the screen we get x and y coordinate
		case MotionEvent.ACTION_DOWN: {
			x1 = (int) touchevent.getX();
			y1 = (int) touchevent.getY();
		    Log.w("TEST", String.format("*** DOWN *** x1 = %d, y1=%d", x1, y1));
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			int x = (int) touchevent.getX();
			int y = (int) touchevent.getY();
		    Log.w("TEST", String.format("<<< MOVE >>> x1 = %d, y1=%d", x, y));
			break;
		}
		
		case MotionEvent.ACTION_UP: {
			x2 = (int) touchevent.getX();
			y2 = (int) touchevent.getY();
		    Log.w("TEST", String.format("*** UP *** x2 = %d, y2=%d", x2, y2));

//			// if left to right sweep event on screen
//			if (x1 < x2) {
//				Toast.makeText(this, "Left to Right Swap Performed",
//						Toast.LENGTH_LONG).show();
//			}
//
//			// if right to left sweep event on screen
//			if (x1 > x2) {
//				Toast.makeText(this, "Right to Left Swap Performed",
//						Toast.LENGTH_LONG).show();
//			}
//
//			// if UP to Down sweep event on screen
//			if (y1 < y2) {
//				Toast.makeText(this, "UP to Down Swap Performed",
//						Toast.LENGTH_LONG).show();
//			}
//
//			// if Down to UP sweep event on screen
//			if (y1 > y2) {
//				Toast.makeText(this, "Down to UP Swap Performed",
//						Toast.LENGTH_LONG).show();
//			}
			break;
		}
		}
		return false;
	}
}

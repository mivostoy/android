package com.s1nn.swiper;

import com.ex.swipetest.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
//import android.widget.Toast;
import android.view.WindowManager;
import android.widget.TextView;
import java.lang.Math;

public class MainActivity extends Activity {
    TextView txt;
	int x1, x2;
	int y1, y2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// make full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.txt);
	}

	// onTouchEvent () called when User performs any touch event on screen
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {

		case MotionEvent.ACTION_DOWN: {
			x1 = (int) touchevent.getX();
			y1 = (int) touchevent.getY();
			Log.w("TEST", String.format("*** DOWN *** x1 = %d, y1=%d", x1, y1));
//            txt.setRotation(90);
//            txt.setTop(0);
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
			Log.w("TEST", String.format("***  UP  *** x2 = %d, y2=%d", x2, y2));

            long dx = x2-x1;
            long dy = y2-y1;
            if (java.lang.Math.abs(dx) < java.lang.Math.abs(dy))
            {
                // vertical
                if (dy>0)
                {
                    txt.setText("DOWN");
//                    txt.setRotation(0);
//                    txt.setTop(20);
                }
                else
                {
                    txt.setText("UP");
//                    txt.setRotation(0);
//                    txt.setBottom(20);
                }
            }
            else
            {
                // horizontal
                if (dx<0)
                {
                    txt.setText("LEFT");
//                    txt.setRotation(90);
//                    txt.setRight(20);
                }
                else
                {
                    txt.setText("RIGHT");
//                    txt.setRotation(180);
//                    txt.setLeft(20);
                }

            }
			break;
		}
		}
		return false;
	}
}

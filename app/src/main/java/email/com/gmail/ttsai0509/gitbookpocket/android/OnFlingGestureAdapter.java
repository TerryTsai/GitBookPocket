package email.com.gmail.ttsai0509.gitbookpocket.android;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class OnFlingGestureAdapter implements GestureDetector.OnGestureListener {

    public interface OnFlingListener {
        boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
    }

    private final OnFlingListener callback;

    public OnFlingGestureAdapter(OnFlingListener callback) {
        this.callback = callback;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return callback.onFling(e1, e2, velocityX, velocityY);
    }

}

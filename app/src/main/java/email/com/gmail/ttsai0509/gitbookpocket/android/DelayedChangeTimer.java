package email.com.gmail.ttsai0509.gitbookpocket.android;

import android.os.CountDownTimer;
import email.com.gmail.ttsai0509.gitbookpocket.service.Logger;

public class DelayedChangeTimer<T> {

    public interface OnDelayedChangeListener<T> {
        void onDelayedChange(T value);
    }

    private volatile T value;
    private final CountDownTimer timer;

    public DelayedChangeTimer(final OnDelayedChangeListener<T> listener, long millisInFuture, long countDownInterval) {
        final DelayedChangeTimer<T> context = this;
        this.value = null;
        this.timer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Logger.Service.d(context, "Notifying new value: " + value.toString());
                listener.onDelayedChange(value);
            }
        };
    }

    public synchronized void setValue(T value) {
        this.value = value;
        timer.cancel();
        timer.start();
    }

}

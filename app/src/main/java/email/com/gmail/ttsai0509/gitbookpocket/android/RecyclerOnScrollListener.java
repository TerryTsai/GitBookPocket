package email.com.gmail.ttsai0509.gitbookpocket.android;

import android.support.v7.widget.RecyclerView;

public class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public interface Callback {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    private final Callback callback;

    public RecyclerOnScrollListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        callback.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        callback.onScrolled(recyclerView, dx, dy);
    }
}

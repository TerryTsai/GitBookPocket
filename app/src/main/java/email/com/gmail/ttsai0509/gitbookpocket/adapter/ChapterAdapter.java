package email.com.gmail.ttsai0509.gitbookpocket.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import email.com.gmail.ttsai0509.gitbookpocket.model.content.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private final Callback callback;
    private final List<Chapter> data;

    public ChapterAdapter(Callback callback) {
        this.callback = callback;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TextView layout = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ChapterAdapter.ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Chapter getChapter(int position) {
        return data.get(position);
    }

    public List<Chapter> getChapters() {
        return data;
    }

    public void setChapters(List<Chapter> chapters) {
        data.addAll(chapters);
        notifyItemRangeInserted(0, chapters.size());
    }

    public interface Callback {
        void onChapterSelect(Chapter chapter, int position);
    }

    private static int[] COLORS = new int[] {
            0xFF0d47a1, 0xFF01579b, 0xFF006064, 0xFF004d40, 0xFF1b5e20
    };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView;
            title.setEllipsize(TextUtils.TruncateAt.END);
            title.setLines(1);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Chapter chapter = data.get(position);
            callback.onChapterSelect(chapter, position);
        }

        void bind(Chapter chapter) {
            if (chapter == null) {
                title.setText("");
            } else {
                int count = 0;
                String level = chapter.getLevel();
                for (int i = 0; i < level.length(); i++)
                    count += (level.charAt(i) == '.') ? 1 : 0;
                title.setText(chapter.getTitle());
                title.setPadding(count * 20, 0, 0, 0);
                //title.setBackgroundColor(COLORS[count]);
            }
        }
    }

}

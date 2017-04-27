package email.com.gmail.ttsai0509.gitbookpocket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ViewHolder> {

    private final Callback callback;
    private final List<Author> data;

    public AuthorAdapter(Callback callback) {
        this.callback = callback;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_author, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAuthors(List<Author> authors) {
        int position = data.size();
        int count = authors.size();
        data.addAll(authors);
        notifyItemRangeInserted(position, count);
    }

    public void clearAuthors() {
        int count = data.size();
        data.clear();
        notifyItemRangeRemoved(0, count);
    }

    public interface Callback {
        void onSelectAuthor(Author author);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView avatar;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.author_avatar);
            avatar.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bind(Author author) {
            if (author.getUrls() != null && author.getUrls().getAvatar() != null)
                Picasso.with(itemView.getContext()).load(author.getUrls().getAvatar()).into(avatar);
        }

        @Override
        public void onClick(View v) {
            Author author = data.get(getAdapterPosition());
            callback.onSelectAuthor(author);
        }
    }
}

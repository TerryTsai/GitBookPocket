package email.com.gmail.ttsai0509.gitbookpocket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.utils.TimeAgo;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;

import java.util.ArrayList;
import java.util.List;

public class SimpleBookAdapter extends RecyclerView.Adapter<SimpleBookAdapter.ViewHolder> {

    private final Callback callback;
    private final List<Book> data;

    public SimpleBookAdapter(Callback callback) {
        this.callback = callback;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_book_simple, parent, false);
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

    public List<Book> getBooks() {
        return data;
    }

    public void addBooks(List<Book> books) {
        int position = data.size();
        int count = books.size();
        data.addAll(books);
        notifyItemRangeInserted(position, count);
    }

    public void clearBooks() {
        int count = data.size();
        data.clear();
        notifyItemRangeRemoved(0, count);
    }

    public interface Callback {
        void onSelectBook(Book book);
        void onSelectAuthor(Author author);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.book_title)
        TextView title;
        @BindView(R.id.book_author)
        TextView author;
        @BindView(R.id.book_description)
        TextView description;
        @BindView(R.id.author_avatar)
        ImageView avatar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            avatar.setOnClickListener(this);
        }

        void bind(Book book) {
            title.setText(book.getTitle());
            description.setText(book.getDescription());
            if (book.getAuthor() != null)
                author.setText(book.getAuthor().getUsername());
            if (book.getDates() != null && book.getDates().getBuild() != null)
                author.setText(author.getText() + " | " + TimeAgo.format(book.getDates().getBuild()));
            if (book.getAuthor() != null && book.getAuthor().getUrls() != null && book.getAuthor().getUrls().getAvatar() != null)
                Picasso.with(itemView.getContext()).load(book.getAuthor().getUrls().getAvatar()).into(avatar);
        }

        @Override
        public void onClick(View v) {
            Book book = data.get(getAdapterPosition());
            if (v == itemView) {
                callback.onSelectBook(book);
            } else if (v == avatar) {
                callback.onSelectAuthor(book.getAuthor());
            }
        }
    }
}

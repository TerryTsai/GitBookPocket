package email.com.gmail.ttsai0509.gitbookpocket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;

import java.util.ArrayList;
import java.util.List;

public class DetailBookAdapter extends RecyclerView.Adapter<DetailBookAdapter.ViewHolder> {

    private final Callback callback;
    private final List<Book> data;

    public DetailBookAdapter(Callback callback) {
        this.callback = callback;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_book_detail, parent, false);
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
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.book_title)
        TextView title;
        @BindView(R.id.book_author)
        TextView author;
        @BindView(R.id.book_description)
        TextView description;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Book book) {
            title.setText(book.getTitle());
            description.setText(book.getDescription());
            if (book.getAuthor() != null)
                author.setText(book.getAuthor().getUsername());
        }

        @Override
        public void onClick(View v) {
            Book book = data.get(getAdapterPosition());
            if (v == itemView) {
                callback.onSelectBook(book);
            }
        }
    }
}

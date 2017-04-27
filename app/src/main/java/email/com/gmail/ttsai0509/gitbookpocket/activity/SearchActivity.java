package email.com.gmail.ttsai0509.gitbookpocket.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import org.parceler.Parcels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.adapter.SimpleBookAdapter;
import email.com.gmail.ttsai0509.gitbookpocket.android.RecyclerOnScrollListener;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;
import email.com.gmail.ttsai0509.gitbookpocket.model.search.Search;
import email.com.gmail.ttsai0509.gitbookpocket.service.GitBook;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.SearchSort;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Type;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchActivity extends GitBookActivity implements SimpleBookAdapter.Callback, SearchView.OnQueryTextListener, RecyclerOnScrollListener.Callback, SwipeRefreshLayout.OnRefreshListener {

    private int page;
    private String query;
    private AtomicBoolean lock;
    private SimpleBookAdapter adapter;
    private LinearLayoutManager layout;
    private SearchView search;
    private RecyclerView searchContent;
    private SwipeRefreshLayout refresher;
    private SearchSort sort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        page = 0;
        query = "";
        sort = SearchSort.stars;
        lock = new AtomicBoolean(false);
        layout = new LinearLayoutManager(this);
        adapter = new SimpleBookAdapter(this);
        searchContent = (RecyclerView) findViewById(R.id.search_content);
        searchContent.setAdapter(adapter);
        searchContent.setLayoutManager(layout);
        searchContent.addOnScrollListener(new RecyclerOnScrollListener(this));
        refresher = (SwipeRefreshLayout) findViewById(R.id.search_container);
        refresher.setOnRefreshListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_search, menu);
        search = (SearchView) menu.findItem(R.id.search_query).getActionView();
        search.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("page", page);
        outState.putParcelable("books", Parcels.wrap(adapter.getBooks()));
        outState.putString("query", query);
        outState.putString("sort", sort.name());
        outState.putInt("scrollX", searchContent.getScrollX());
        outState.putInt("scrollY", searchContent.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<Book> books = Parcels.unwrap(savedInstanceState.getParcelable("books"));
        adapter.addBooks(books);
        page = savedInstanceState.getInt("page");
        query = savedInstanceState.getString("query");
        sort = SearchSort.valueOf(savedInstanceState.getString("sort"));
        searchContent.scrollTo(savedInstanceState.getInt("scrollX"), savedInstanceState.getInt("scrollY"));
        setTitle(query);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String title = item.getTitle().toString();
        switch (title) {
            case "Dark":
            case "Light":
            case "Ido":
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                settings.edit().putString("theme", title).apply();
                finish();
                startActivity(new Intent(this, this.getClass()));
                return true;
            case "Defaults":
            case "Stars":
            case "Updated":
                sort = SearchSort.valueOf(title.toLowerCase());
                return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        if (!query.isEmpty()) {
            clearBooks();
            loadBooks();
        } else {
            refresher.setRefreshing(false);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int first = layout.findFirstVisibleItemPosition();
        int visible = searchContent.getChildCount();
        int total = layout.getItemCount();
        if (first + visible >= total)
            loadBooks();
    }

    @Override
    public boolean onQueryTextSubmit(String aQuery) {
        if (!aQuery.isEmpty()) {
            search.setQuery("", false);
            search.setIconified(true);
            setTitle(aQuery);
            query = aQuery;
            clearBooks();
            loadBooks();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onSelectBook(Book book) {
        Intent intent = new Intent(this, ReaderActivity.class);
        intent.putExtra("book", Parcels.wrap(book));
        startActivity(intent);
    }

    @Override
    public void onSelectAuthor(Author author) {
        Intent intent = new Intent(this, AuthorActivity.class);
        intent.putExtra("author", Parcels.wrap(author));
        startActivity(intent);
    }

    private void clearBooks() {
        page = 0;
        adapter.clearBooks();
    }

    private void loadBooks() {
        if (!lock.compareAndSet(false, true))
            return;

        refresher.setRefreshing(true);
        GitBook.Service.searchBooks(query, page, Type.books, sort).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search body = response.body();
                if (body != null && body.getResults() != null && body.getResults().getList() != null) {
                    adapter.addBooks(body.getResults().getList());
                    if (body.getResults().getList().size() > 0)
                        page++;
                }
                lock.set(false);
                refresher.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                lock.set(false);
                refresher.setRefreshing(false);
            }
        });
    }
}

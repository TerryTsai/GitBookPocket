package email.com.gmail.ttsai0509.gitbookpocket.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.adapter.AuthorAdapter;
import email.com.gmail.ttsai0509.gitbookpocket.adapter.DetailBookAdapter;
import email.com.gmail.ttsai0509.gitbookpocket.adapter.SimpleBookAdapter;
import email.com.gmail.ttsai0509.gitbookpocket.android.MappedViewPager;
import email.com.gmail.ttsai0509.gitbookpocket.android.RecyclerOnScrollListener;
import email.com.gmail.ttsai0509.gitbookpocket.utils.TimeAgo;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.AuthorType;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Page;
import email.com.gmail.ttsai0509.gitbookpocket.model.member.Members;
import email.com.gmail.ttsai0509.gitbookpocket.model.starred.Starred;
import email.com.gmail.ttsai0509.gitbookpocket.service.GitBook;
import org.parceler.Parcels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileReader;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("FieldCanBeLocal")
public class AuthorActivity extends GitBookActivity implements SimpleBookAdapter.Callback, DetailBookAdapter.Callback, RecyclerOnScrollListener.Callback, AuthorAdapter.Callback {

    private Author author;

    private int booksPage;
    private AtomicBoolean booksLock;
    private LinearLayoutManager booksLayout;
    private DetailBookAdapter booksAdapter;
    private RecyclerView booksRecycler;

    private int starredPage;
    private AtomicBoolean starredLock;
    private LinearLayoutManager starredLayout;
    private SimpleBookAdapter starredAdapter;
    private RecyclerView starredRecycler;

    private int membersPage;
    private AtomicBoolean membersLock;
    private GridLayoutManager membersLayout;
    private AuthorAdapter membersAdapter;
    private RecyclerView membersRecycler;

    private ViewPager pager;
    private TextView username;
    private TextView location;
    private TextView website;
    private TextView bio;
    private TextView date;
    private TextView github;
    private ImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        author = Parcels.unwrap(getIntent().getParcelableExtra("author"));

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle(author.getUsername());
        }

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        username = (TextView) findViewById(R.id.author_username);
        username.setText(author.getUsername());
        location = (TextView) findViewById(R.id.author_location);
        location.setText(author.getLocation());
        website = (TextView) findViewById(R.id.author_website);
        website.setText(author.getWebsite());
        bio = (TextView) findViewById(R.id.author_bio);
        bio.setText(author.getBio());

        date = (TextView) findViewById(R.id.author_date);
        if (author.getDates() != null && author.getDates().getCreated() != null)
            date.setText("Joined " + TimeAgo.format(author.getDates().getCreated()));

        github = (TextView) findViewById(R.id.author_github);
        github.setTypeface(font);
        if (author.getGithub() != null)
            github.setText(getResources().getString(R.string.icon_github) + " " + author.getGithub().getUsername());

        avatar = (ImageView) findViewById(R.id.author_avatar);
        avatar.setImageDrawable(getDrawable(R.drawable.avatar));
        if (author.getUrls() != null && author.getUrls().getAvatar() != null) {
            ViewGroup.LayoutParams avatarParams = avatar.getLayoutParams();
            Picasso.with(this)
                    .load(author.getUrls().getAvatar())
                    .resize(avatarParams.width, avatarParams.height)
                    .into(avatar);
        }

        booksLock = new AtomicBoolean(false);
        booksAdapter = new DetailBookAdapter(this);
        booksLayout = new LinearLayoutManager(this);
        booksRecycler = (RecyclerView) findViewById(R.id.author_books);
        booksRecycler.setAdapter(booksAdapter);
        booksRecycler.setLayoutManager(booksLayout);
        booksRecycler.addOnScrollListener(new RecyclerOnScrollListener(this));

        starredLock = new AtomicBoolean(false);
        starredAdapter = new SimpleBookAdapter(this);
        starredLayout = new LinearLayoutManager(this);
        starredRecycler = (RecyclerView) findViewById(R.id.author_starred);
        starredRecycler.setAdapter(starredAdapter);
        starredRecycler.setLayoutManager(starredLayout);
        starredRecycler.addOnScrollListener(new RecyclerOnScrollListener(this));

        membersLock = new AtomicBoolean(false);
        membersAdapter = new AuthorAdapter(this);
        membersLayout = new GridLayoutManager(this, 3);
        membersRecycler = (RecyclerView) findViewById(R.id.author_members);
        membersRecycler.setAdapter(membersAdapter);
        membersRecycler.setLayoutManager(membersLayout);
        membersRecycler.addOnScrollListener(new RecyclerOnScrollListener(this));

        pager = (ViewPager) findViewById(R.id.author_pager);
        pager.setOffscreenPageLimit(3);
        if (author.getType() == AuthorType.User) {
            pager.setAdapter(new MappedViewPager(
                    new MappedViewPager.Entry(R.id.author_profile, "Profile"),
                    new MappedViewPager.Entry(R.id.author_books, "Books"),
                    new MappedViewPager.Entry(R.id.author_starred, "Starred")));
        }
        if (author.getType() == AuthorType.Organization) {
            pager.setAdapter(new MappedViewPager(
                    new MappedViewPager.Entry(R.id.author_profile, "Profile"),
                    new MappedViewPager.Entry(R.id.author_books, "Books"),
                    new MappedViewPager.Entry(R.id.author_members, "Members")));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadBooks();
        if (author.getType() == AuthorType.User)
            loadStarred();
        if (author.getType() == AuthorType.Organization)
            loadMembers();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView == booksRecycler) {
            int first = booksLayout.findFirstVisibleItemPosition();
            int visible = recyclerView.getChildCount();
            int total = booksLayout.getItemCount();
            if (first + visible >= total) loadBooks();
        } else if (recyclerView == starredRecycler) {
            int first = starredLayout.findFirstVisibleItemPosition();
            int visible = recyclerView.getChildCount();
            int total = starredLayout.getItemCount();
            if (first + visible >= total) loadStarred();
        } else if (recyclerView == membersRecycler) {
            int first = membersLayout.findFirstVisibleItemPosition();
            int visible = recyclerView.getChildCount();
            int total = membersLayout.getItemCount();
            if (first + visible >= total) loadMembers();
        }
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

    private void loadBooks() {
        if (!booksLock.compareAndSet(false, true))
            return;

        GitBook.Service.getAuthorBooks(author.getUsername(), booksPage).enqueue(new Callback<Page<Book>>() {
            @Override
            public void onResponse(Call<Page<Book>> call, Response<Page<Book>> response) {
                Page<Book> booksLast = response.body();
                booksAdapter.addBooks(booksLast.getList());
                if (booksLast.getList().size() > 0) {
                    booksPage++;
                    booksLock.set(false);
                }
            }

            @Override
            public void onFailure(Call<Page<Book>> call, Throwable t) {
            }
        });
    }

    private void loadStarred() {
        if (!starredLock.compareAndSet(false, true))
            return;

        GitBook.Service.getStarredBooks(author.getUsername(), starredPage).enqueue(new Callback<Starred>() {
            @Override
            public void onResponse(Call<Starred> call, Response<Starred> response) {
                Starred starredLast = response.body();
                starredAdapter.addBooks(starredLast.getStarred());
            }

            @Override
            public void onFailure(Call<Starred> call, Throwable t) {
            }
        });
    }

    private void loadMembers() {
        if (!membersLock.compareAndSet(false, true))
            return;

        GitBook.Service.getMembers(author.getUsername()).enqueue(new Callback<Members>() {
            @Override
            public void onResponse(Call<Members> call, Response<Members> response) {
                Members members = response.body();
                membersAdapter.addAuthors(members.getVisibleCollaborators());
            }

            @Override
            public void onFailure(Call<Members> call, Throwable t) {
            }
        });
    }
}

package email.com.gmail.ttsai0509.gitbookpocket.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.adapter.ChapterAdapter;
import email.com.gmail.ttsai0509.gitbookpocket.android.DelayedChangeTimer;
import email.com.gmail.ttsai0509.gitbookpocket.android.OnFlingGestureAdapter;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;
import email.com.gmail.ttsai0509.gitbookpocket.model.content.Chapter;
import email.com.gmail.ttsai0509.gitbookpocket.model.content.Content;
import email.com.gmail.ttsai0509.gitbookpocket.model.content.Language;
import email.com.gmail.ttsai0509.gitbookpocket.model.content.Section;
import email.com.gmail.ttsai0509.gitbookpocket.service.GitBook;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import org.parceler.Parcels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ReaderActivity extends GitBookActivity implements ChapterAdapter.Callback, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, OnFlingGestureAdapter.OnFlingListener, DelayedChangeTimer.OnDelayedChangeListener<Integer> {

    private final long delay = 1000;
    private final int swipeDistance = 250;
    private final int swipeVelocity = 2500;

    private boolean restore;
    private boolean initialized;
    private Book book;
    private DrawerLayout drawer;
    private LinearLayoutManager manager;
    private RecyclerView menu;
    private MaterialProgressBar progress;
    private WebView content;
    private ChapterAdapter adapter;
    private Language language;
    private GestureDetectorCompat gesture;
    private int currentPage;
    private AppCompatSeekBar seek;
    private DelayedChangeTimer<Integer> seekTimer;
    private ActionBarDrawerToggle drawerToggle;
    private SparseArray<Content> memory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        book = Parcels.unwrap(getIntent().getParcelableExtra("book"));
        drawer = (DrawerLayout) findViewById(R.id.reader_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                // Gets unlocked when clicking hamburger menu.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (!drawer.isDrawerOpen(GravityCompat.START))
                    manager.scrollToPositionWithOffset(currentPage, 0);
                super.onDrawerStateChanged(newState);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(drawerToggle);
        progress = (MaterialProgressBar) findViewById(R.id.reader_progress);
        manager = new LinearLayoutManager(this);
        adapter = new ChapterAdapter(this);
        menu = (RecyclerView) findViewById(R.id.reader_menu);
        menu.setAdapter(adapter);
        menu.setLayoutManager(manager);
        content = (WebView) findViewById(R.id.reader_contents);
        content.setWebChromeClient(new WebChromeClient());
        content.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        content.setOnTouchListener(this);
        gesture = new GestureDetectorCompat(this, new OnFlingGestureAdapter(this));
        seek = (AppCompatSeekBar) findViewById(R.id.reader_seek);
        seek.setOnSeekBarChangeListener(this);
        seekTimer = new DelayedChangeTimer<>(this, delay, 200);
        memory = new SparseArray<>();
        currentPage = 0;
        initialized = false;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (restore) {
            selectChapterByPosition(currentPage);
        } else {
            progress.setVisibility(ProgressBar.VISIBLE);
            retrieveContent(book, 0, "");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPage", currentPage);
        outState.putParcelable("book", Parcels.wrap(book));
        outState.putParcelable("chapters", Parcels.wrap(adapter.getChapters()));
        outState.putParcelable("language", Parcels.wrap(language));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<Chapter> chapters = Parcels.unwrap(savedInstanceState.getParcelable("chapters"));
        adapter.setChapters(chapters);
        currentPage = savedInstanceState.getInt("currentPage");
        book = Parcels.unwrap(savedInstanceState.getParcelable("book"));
        language = Parcels.unwrap(savedInstanceState.getParcelable("language"));
        seek.setMax(chapters.size() - 1);
        seek.setProgress(currentPage);
        restore = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_reader, menu);
        // TODO : ActionBar colors are all messed up.
        MenuItem item = menu.findItem(R.id.download_group);
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(this, R.color.ido_icons));
        item.setIcon(wrapDrawable);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;

        if (item != null) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    handled = drawerToggle.onOptionsItemSelected(item);
                    break;
                case R.id.download_epub:
                    Intent epubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getUrls().getDownload().getEpub()));
                    startActivity(epubIntent);
                    handled = true;
                    break;
                case R.id.download_mobi:
                    Intent mobiIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getUrls().getDownload().getMobi()));
                    startActivity(mobiIntent);
                    handled = true;
                    break;
                case R.id.download_pdf:
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getUrls().getDownload().getPdf()));
                    startActivity(pdfIntent);
                    handled = true;
                    break;
            }
        }

        return handled || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gesture.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float speed = Math.abs(velocityX);
        float distance = e2.getX() - e1.getX();
        if (currentPage < adapter.getItemCount() - 1 && distance < -swipeDistance && speed > swipeVelocity) {
            selectChapterByPosition(currentPage + 1);
            return true;
        } else if (currentPage > 0 && distance > swipeDistance && speed > swipeVelocity) {
            selectChapterByPosition(currentPage - 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onDelayedChange(Integer value) {
        selectChapterByPosition(value);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            setTitle(adapter.getChapter(progress).getTitle());
            seekTimer.setValue(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onChapterSelect(final Chapter chapter, final int position) {
        drawer.closeDrawer(GravityCompat.START);
        progress.setVisibility(ProgressBar.VISIBLE);
        currentPage = position;
        seek.setProgress(position);

        if (chapter == null) {
            renderError("Unexpected chapter.");
            return;
        }
        String chapterPath = chapter.getPath();
        if (chapterPath == null) {
            renderError("Chapter path expected.");
            return;
        }
        int extStart = chapterPath.lastIndexOf('.');
        if (extStart <= 0 || extStart >= chapterPath.length()) {
            renderError("Chapter path unexpected format.");
            return;
        }
        setTitle(chapter.getTitle());
        chapterPath = chapterPath.substring(0, extStart) + ".json";
        retrieveContent(book, position, chapterPath);
    }

    private void selectChapterByPosition(final int position) {
        onChapterSelect(adapter.getChapter(position), position);
    }

    private void renderError(String message) {
        progress.setVisibility(ProgressBar.VISIBLE);
        content.loadData("Unable to load data: " + message, "text/plain", "UTF-8");
        progress.setVisibility(ProgressBar.INVISIBLE);
    }

    private void renderContent(Content data) {
        progress.setVisibility(ProgressBar.VISIBLE);
        if (data == null || data.getSections() == null || data.getSections().size() == 0) {
            renderError("No section data.");
        } else {
            StringBuilder html = new StringBuilder();
            html.append("<html><head><style>");
            html.append("pre{width:100%;overflow-x:scroll;background-color:#eeeeee;}");
            html.append("img, p, span{width:100%;}");
            html.append("</style></head><body>");
            for (Section section : data.getSections())
                html.append(section.getContent());
            html.append("<script>document.onload=function(){window.scrollTo(0, 0);};</script>");
            html.append("</body></html>");
            content.loadDataWithBaseURL(null, html.toString(), "text/html", "UTF-8", null);
        }
        progress.setVisibility(ProgressBar.INVISIBLE);
    }

    private void retrieveContent(final Book book, final int position, final String chapterPath) {
        if (memory.get(position) != null) {
            renderContent(memory.get(position));
            return;
        }

        Call<Content> call = (language == null)
                ? GitBook.Service.getBookContentsFile(book.getAuthor().getUsername(), book.getName(), chapterPath)
                : GitBook.Service.getBookContentsFileLanguage(book.getAuthor().getUsername(), book.getName(), language.getLang(), chapterPath);

        Callback<Content> callback = new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                Content content = response.body();

                if (!initialized) {
                    List<Chapter> chapters = content.getProgress().getChapters();
                    adapter.setChapters(chapters);
                    seek.setMax(chapters.size() - 1);
                    if (content.getLangs() != null && content.getLangs().size() != 0)
                        language = content.getLangs().get(0);
                    initialized = true;
                }

                memory.put(position, content);
                Content current = memory.get(currentPage);
                renderContent(current != null ? current : content);
            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {
                if (memory.get(currentPage) != null)
                    renderContent(memory.get(currentPage));
                else
                    renderError(t.getLocalizedMessage());
            }
        };

        call.enqueue(callback);
    }

}
package email.com.gmail.ttsai0509.gitbookpocket.android;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MappedViewPager extends PagerAdapter {

    private final Entry[] entries;

    public MappedViewPager(Entry... entries) {
        this.entries = entries;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return (position < entries.length)
                ? container.findViewById(entries[position].resId)
                : null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position < entries.length)
                ? entries[position].title
                : "";
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    public static class Entry {
        private int resId;
        private String title;

        public Entry(int resId, String title) {
            this.resId = resId;
            this.title = title;
        }
    }

}

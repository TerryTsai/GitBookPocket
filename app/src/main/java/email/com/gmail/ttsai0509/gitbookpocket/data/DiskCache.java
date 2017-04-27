package email.com.gmail.ttsai0509.gitbookpocket.data;

import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class DiskCache<K, V> implements DataLayer<K, V> {

    public interface KeyHash<K> {
        String hash(K object);
    }

    private final Gson gson;
    private final Class<V> type;
    private final long maxCacheSize;
    private final File cacheDir;
    private final Comparator<File> comparator;
    private final DataLayer<K, V> nextDataLayer;
    private final KeyHash<K> keyHash;

    public DiskCache(File cacheDir, long maxCacheSize, Class<V> type, DataLayer<K, V> nextDataLayer, KeyHash<K> keyHash) throws IOException {
        this.gson = new Gson();
        this.type = type;
        this.keyHash = keyHash;
        this.maxCacheSize = maxCacheSize;
        this.nextDataLayer = nextDataLayer;
        this.cacheDir = new File(cacheDir, ".cache");
        if (!this.cacheDir.exists())
            this.cacheDir.mkdir();
        if (!this.cacheDir.isDirectory())
            throw new IOException("Unable to find cache directory: " + this.cacheDir.getPath());
        this.comparator  = new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return (int) (o1.lastModified() - o2.lastModified());
            }
        };
    }

    @Override
    public void get(K key, final DataListener<K, V> callback) {
        try {
            V val = findFrom(keyHash.hash(key));

            if (val != null) {
                callback.onDataGet(key, val);
                return;
            }

            nextDataLayer.get(key, new DataListener<K, V>() {
                @Override
                public void onDataGet(K key, V val) {
                    try {
                        writeTo(keyHash.hash(key), val);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onDataGet(key, val);
                }

                @Override
                public void onDataFail(Throwable error) {
                    callback.onDataFail(error);
                }
            });

        } catch (IOException e) {
            callback.onDataFail(e);
        }
    }

    private V findFrom(final String key) throws IOException {
        if (!cacheDir.isDirectory()) {
            throw new IOException("Cache is not a directory: " + cacheDir.getName());
        }

        File[] files = cacheDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().equals(key);
            }
        });

        if (files == null || files.length <= 0) {
            return null;
        } else try (FileReader fr = new FileReader(files[0])) {
            return gson.fromJson(fr, type);
        }
    }

    private <T> long writeTo(String key, T object) throws IOException {
        if (!cacheDir.isDirectory()) {
            throw new IOException("Cache is not a directory: " + cacheDir.getName());
        }

        File file = new File(cacheDir, key);
        if (file.isDirectory() || (file.isFile() && !file.delete())) {
            throw new IOException("Unable to remove previous entry: " + file.getName());
        }

        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(object, fw);
            fw.flush();
            return purge();
        }
    }

    private long purge() {
        if (!cacheDir.isDirectory())
            return 0L;

        File[] files = cacheDir.listFiles();
        Arrays.sort(files, comparator);

        long cacheSize = 0L;
        for (File file : files)
            cacheSize += file.length();

        int idx = 0;
        long currCacheSize = cacheSize;
        while (currCacheSize > maxCacheSize) {
            File file = files[idx++];
            System.out.println("DELETING : " + file.getName());
            currCacheSize -= file.length();
            file.delete();
        }

        return cacheSize - currCacheSize;
    }
}

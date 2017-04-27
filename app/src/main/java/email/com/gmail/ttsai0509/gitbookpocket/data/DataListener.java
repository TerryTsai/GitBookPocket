package email.com.gmail.ttsai0509.gitbookpocket.data;

public interface DataListener<K, V> {

    void onDataGet(K key, V val);
    void onDataFail(Throwable error);

}

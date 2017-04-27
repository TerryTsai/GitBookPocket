package email.com.gmail.ttsai0509.gitbookpocket.data;

public interface DataLayer<K, V> {

    void get(K key, DataListener<K, V> callback);

}

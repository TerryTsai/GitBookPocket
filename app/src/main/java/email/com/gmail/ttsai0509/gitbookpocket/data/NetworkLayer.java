package email.com.gmail.ttsai0509.gitbookpocket.data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkLayer<K, V> implements DataLayer<K, V> {

    public interface CallFactory<K, V> {
        Call<V> getCall(K key);
    }

    private final CallFactory<K, V> callFactory;

    public NetworkLayer(CallFactory<K, V> callFactory) {
        this.callFactory = callFactory;
    }

    @Override
    public void get(final K key, final DataListener<K, V> callback) {
        Call<V> call = callFactory.getCall(key);
        call.enqueue(new Callback<V>() {
            @Override
            public void onResponse(Call<V> call, Response<V> response) {
                V val = response.body();
                if (val != null) {
                    callback.onDataGet(key, val);
                } else {
                    callback.onDataFail(null);
                }
            }

            @Override
            public void onFailure(Call<V> call, Throwable t) {
                callback.onDataFail(t);
            }
        });
    }


}

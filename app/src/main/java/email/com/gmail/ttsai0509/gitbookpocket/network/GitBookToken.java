package email.com.gmail.ttsai0509.gitbookpocket.network;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GitBookToken {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("token_type")
    String tokenType;

    @Override
    public String toString() {
        return tokenType + " " + accessToken;
    }

}

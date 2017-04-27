package email.com.gmail.ttsai0509.gitbookpocket.model.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

@Data
@Parcel
@NoArgsConstructor
@AllArgsConstructor
public class AuthorUrls {

    String profile;
    String stars;
    String avatar;

}

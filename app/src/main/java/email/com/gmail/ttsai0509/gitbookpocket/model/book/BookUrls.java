package email.com.gmail.ttsai0509.gitbookpocket.model.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

@Data
@Parcel
@NoArgsConstructor
@AllArgsConstructor
public class BookUrls {

    String access;
    String content;
    String edit;
    String git;
    String homepage;
    String read;
    BookUrlsDownload download;

}

package email.com.gmail.ttsai0509.gitbookpocket.model.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class BookUrlsDownload {

    String epub;
    String mobi;
    String pdf;

}

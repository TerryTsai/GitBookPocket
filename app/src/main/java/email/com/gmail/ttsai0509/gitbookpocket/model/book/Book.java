package email.com.gmail.ttsai0509.gitbookpocket.model.book;

import com.google.gson.annotations.SerializedName;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Counts;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Language;
import org.parceler.Parcel;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Parcel
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    String id;
    String status;
    String name;
    String title;
    String description;

    @SerializedName("public")
    Boolean isPublic;
    String template;
    List<String> topics;
    String license;
    Language language;
    Boolean locked;
    BookCover cover;
    BookUrls urls;
    Counts counts;
    BookDates dates;
    BookPermissions permissions;
    BookPublish publish;
    Author author;


}

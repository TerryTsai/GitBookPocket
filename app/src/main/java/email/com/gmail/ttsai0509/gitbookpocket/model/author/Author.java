package email.com.gmail.ttsai0509.gitbookpocket.model.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Counts;

@Data
@Parcel
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    String id;
    AuthorType type;
    String username;
    String location;
    String website;
    String bio;
    Boolean verified;
    Boolean locked;
    Boolean site_admin;
    AuthorUrls urls;
    AuthorPermissions permissions;
    AuthorDates dates;
    Counts counts;
    AuthorGithub github;

}

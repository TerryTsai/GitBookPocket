package email.com.gmail.ttsai0509.gitbookpocket.model.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;

import java.util.List;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class Members {

    Author author;
    String tab;
    List<Author> visibleCollaborators;

}

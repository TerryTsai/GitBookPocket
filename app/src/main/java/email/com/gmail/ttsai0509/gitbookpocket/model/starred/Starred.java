package email.com.gmail.ttsai0509.gitbookpocket.model.starred;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;
import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;

import java.util.List;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class Starred {

    Author author;
    String tab;
    List<Book> starred;

}

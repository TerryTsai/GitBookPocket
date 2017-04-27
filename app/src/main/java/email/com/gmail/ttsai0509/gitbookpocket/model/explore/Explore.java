package email.com.gmail.ttsai0509.gitbookpocket.model.explore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Page;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;
import email.com.gmail.ttsai0509.gitbookpocket.model.topic.Topic;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Explore {

    List<Topic> topics;
    Page<Book> books;

}

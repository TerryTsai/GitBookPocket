package email.com.gmail.ttsai0509.gitbookpocket.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Page;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search {

    SearchTotal total;
    Page<Book> results;
    String type;
    String sort;

}

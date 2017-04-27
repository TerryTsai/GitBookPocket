package email.com.gmail.ttsai0509.gitbookpocket.model.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

import java.util.Date;

@Data
@Parcel
@NoArgsConstructor
@AllArgsConstructor
public class BookDates {

    Date build;
    Date created;

}

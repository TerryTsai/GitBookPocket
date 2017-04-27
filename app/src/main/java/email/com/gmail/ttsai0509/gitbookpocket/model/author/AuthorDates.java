package email.com.gmail.ttsai0509.gitbookpocket.model.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

import java.util.Date;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDates {

    Date created;

}

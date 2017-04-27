package email.com.gmail.ttsai0509.gitbookpocket.model.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class Section {

    String type;
    String content;

}

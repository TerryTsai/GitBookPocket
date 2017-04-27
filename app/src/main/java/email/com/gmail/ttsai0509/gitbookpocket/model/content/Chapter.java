package email.com.gmail.ttsai0509.gitbookpocket.model.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {

    Integer index;
    String title;
    Boolean introduction;
    Next next;
    String level;
    String path;
    Float percent;
    Boolean done;

}

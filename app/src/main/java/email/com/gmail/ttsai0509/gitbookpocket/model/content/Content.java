package email.com.gmail.ttsai0509.gitbookpocket.model.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

import java.util.List;

@Data
@Parcel
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    String version;
    List<Section> sections;
    Progress progress;
    List<Language> langs;

}

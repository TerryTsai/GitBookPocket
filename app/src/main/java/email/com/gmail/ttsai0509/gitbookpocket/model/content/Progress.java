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
public class Progress {

    List<Chapter> chapters;
    Float percent;
    Float prevPercent;
    Chapter current;

}

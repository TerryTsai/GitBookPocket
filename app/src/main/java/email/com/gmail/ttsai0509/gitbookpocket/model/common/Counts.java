package email.com.gmail.ttsai0509.gitbookpocket.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parceler.Parcel;

@Data
@Parcel
@NoArgsConstructor
@AllArgsConstructor
public class Counts {

    Integer stars;
    Integer subscriptions;
    Integer updates;
    Integer discussions;
    Integer changeRequests;

}

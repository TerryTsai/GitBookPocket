package email.com.gmail.ttsai0509.gitbookpocket.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

    List<T> list;
    Integer limit;
    Integer page;
    Integer pages;
    Integer total;

}

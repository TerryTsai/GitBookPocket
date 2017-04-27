package email.com.gmail.ttsai0509.gitbookpocket.network.interceptor;

import okhttp3.*;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class SiteScraper implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        ResponseBody newBody = ResponseBody.create(body.contentType(), extractResults(body.charStream()));
        return response.newBuilder().body(newBody).build();
    }

    // TODO: Very fragile extraction
    private String extractResults(Reader reader) throws IOException {
        PushbackReader input = new PushbackReader(reader, 1);

        int progress = 0;
        char[] target = "props:".toCharArray();
        while (progress < target.length)
            progress = (input.read() == target[progress]) ? progress + 1 : 0;
        while ((progress = input.read()) != '{')
            ; // do nothing
        input.unread(progress);

        char read;
        int arrays = 0;
        int objects = 0;
        StringBuilder result = new StringBuilder();
        do {
            switch ((read = (char) input.read())) {
                case '{': objects++; break;
                case '[': arrays++; break;
                case '}': objects--; break;
                case ']': arrays--; break;
            }
            result.append(read);
        } while (objects != 0 || arrays != 0);

        String output = result.toString();
        System.out.println(output);
        return output;
    }

}

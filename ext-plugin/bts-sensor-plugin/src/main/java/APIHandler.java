import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class APIHandler {

    public static String get(String urlBase, String path) throws IOException, ParseException {
        urlBase += path;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlBase)
                .build();

        Response response = client.newCall(request).execute();
        String body = response.body().string();

        return body;
    }

}

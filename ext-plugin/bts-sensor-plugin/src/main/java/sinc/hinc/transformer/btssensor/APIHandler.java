package sinc.hinc.transformer.btssensor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class APIHandler {
    static Logger logger = LoggerFactory.getLogger(APIHandler.class.getSimpleName());
    public static String get(String urlBase, String path) throws IOException, ParseException {
        urlBase += path;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlBase)
                .build();

        logger.info("fetching data from "+urlBase);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logger.info("successfully fetched from "+urlBase);
        logger.debug(body);

        return body;
    }

}

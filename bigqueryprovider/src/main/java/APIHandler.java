import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class APIHandler {
    static Logger logger = LoggerFactory.getLogger(APIHandler.class.getSimpleName());
    public static String get(String urlBase, String path) throws IOException {
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

    public static String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        logger.info("fetching data from "+url);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logger.info("successfully fetched from "+url);
        logger.debug(body);

        return body;
    }


    public static String post(String url, String body) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        logger.info("post request to "+url);
        logger.debug(body);
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()) {
            logger.error("POST request error");
            logger.error(response.body().string());
            throw new IOException("Unexpected code "+response.code());
        }

        logger.info("successfully posted to "+url);
        String responseBody = response.body().string();
        logger.debug(responseBody);
        return responseBody;
    }

}

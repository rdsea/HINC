/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used by HINC, where the Adaptor call TestRigProvider REST
 *
 * @author hungld
 */
public class RestHandler {

    static Logger logger = LoggerFactory.getLogger("TestRig");
    private URL url = null;
    private HttpURLConnection conn = null;
    private String data;

    public static RestHandler build(String url) throws MalformedURLException, IOException {
        RestHandler rest = new RestHandler();
        rest.url = new URL(url);
        rest.conn = (HttpURLConnection) rest.url.openConnection();
        return rest;
    }

    public RestHandler header(String key, String value) {
        conn.setRequestProperty(key, value);
        return this;
    }

    public RestHandler accept(String accept) {
        return header("Accept", accept);
    }

    public RestHandler contentType(String type) {
        return header("Content-Type", type);
    }

    public RestHandler data(String data) {
        this.data = data;
        return this;
    }

    public String callGet() throws ProtocolException {
        conn.setRequestMethod("GET");
        return call();
    }

    public String callPost() throws ProtocolException {
        conn.setRequestMethod("POST");
        return call();
    }

    public String callPut() throws ProtocolException {
        conn.setRequestMethod("PUT");
        return call();
    }

    public String callDELETE() throws ProtocolException {
        conn.setRequestMethod("DELETE");
        return call();
    }

    private String call() {
        try {
            if (data != null) {
                conn.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    logger.debug("Writing XML to stream\n: " + data);
                    wr.write(data.getBytes());
                }
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
                String output;
                String result = "";

                while ((output = br.readLine()) != null) {
                    result += output + "\n";
                }
                conn.disconnect();
                return result.trim();
            }
        } catch (ConnectException e) {
            logger.debug("Fail to connect the URL:" + url);
            logger.debug("Error message: " + e.getMessage());
            return null;
        } catch (IOException ex) {
            logger.debug("Failed in reading results. Error: " + ex.getMessage());
            return null;
        } finally {

        }
    }
}

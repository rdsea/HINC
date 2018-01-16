/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used by HINC, where the Adaptor call TestRigProvider REST
 *
 * @author linhsolar
 */
//Every RestHandler might have something different but how do we make them generic
public class RestHandler {

    static Logger logger = LoggerFactory.getLogger("TestRig");

 
    private URL url = null;
    private HttpURLConnection conn = null;
    private String data;
    //public static RestHandler build(String url) throws IOException {
    //    return build(url,null,null);
    //}
    public static RestHandler build(String url, String username, String password) throws MalformedURLException, IOException {
        RestHandler rest = new RestHandler();
        rest.url = new URL(url);
        if (username !=null) {
        //very simple authentication, we need a better way
        String authenticationPass = username+":"+password;
        String basicAuth = "Basic " + Arrays.toString(Base64.encodeBase64(authenticationPass.getBytes()));
        rest.conn = (HttpURLConnection) rest.url.openConnection();
        rest.conn.setRequestProperty("Authorization", basicAuth);
        }
        else {
            rest.conn = (HttpURLConnection) rest.url.openConnection();
        }
        rest.conn.setInstanceFollowRedirects(true);
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
            try (BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)conn.getContent()))) {
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
    public static void main (String args[]) throws IOException {
        //just a test to see if it is ok or not
        if (args.length==1) {
        logger.debug(RestHandler.build(args[0], null,null).callGet());
        }
        else {
            if (args.length==3) {
            logger.debug(RestHandler.build(args[0],args[1],args[2]).callGet());
                    }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.common.utils;

//import sinc.hinc.abstraction.ResourceDriver.InfoSourceSettings;
//import sinc.hinc.communication.Utils.HincUtils;
//import sinc.hinc.API.metadata.HincLocalMeta;
//import sinc.hinc.communication.protocol.HincMessageTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.HincMessageTopic;

/**
 *
 * @author hungld
 */
public class HincConfiguration {

    static Logger logger = LoggerFactory.getLogger("HINC");

    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final String CONFIG_FILE = CURRENT_DIR + "/hinc.conf";
    private static final String myUUID = UUID.randomUUID().toString();

    static HincLocalMeta localMeta;
    static HINCGlobalMeta globalMeta;

    // only HINC Local use this
    public static HincLocalMeta getLocalMeta() {
        if (localMeta == null) {
            localMeta = new HincLocalMeta(myUUID, HincUtils.getEth0Address(), HincMessageTopic.getHINCPrivateTopic(myUUID));
            localMeta.setGroupName(getGroupName());
            localMeta.setBroker(getBroker());
            localMeta.setBrokerType(getBrokerType());

            IPLocationData locationMeta = getIpLocationMeta();
            localMeta.setCity(locationMeta.getCity());
            localMeta.setCountry(locationMeta.getCountry());
            localMeta.setIsp(locationMeta.getIsp());
            localMeta.setLat(locationMeta.getLat());
            localMeta.setLon(locationMeta.getLon());

        }
        return localMeta;
    }

    // only HINC Global use this
    public static HINCGlobalMeta getGlobalMeta() {
        if (globalMeta == null) {
            globalMeta = new HINCGlobalMeta(getGroupName(), getBroker(), getBrokerType());
            IPLocationData locationMeta = getIpLocationMeta();
            globalMeta.setCity(locationMeta.getCity());
            globalMeta.setCountry(locationMeta.getCountry());
            globalMeta.setIsp(locationMeta.getIsp());
            globalMeta.setLat(locationMeta.getLat());
            globalMeta.setLon(locationMeta.getLon());
        }
        return globalMeta;
    }

    public static String getDataForward() {
        return getGenericParameter("DATA_FORWARD", "tcp://localhost:1883");
    }

    public static String getBroker() {
        return getGenericParameter("BROKER_HOST", "localhost");
    }

    public static String getBrokerType() {
        return getGenericParameter("BROKER_TYPE", "ampq");
    }

    public static String getGroupName() {
        return getGenericParameter("GROUP", "DEFAULT");
    }



    public static boolean detectLocation() {
        String isDetected = getGenericParameter("AUTO_LOCATION", "false");
        return Boolean.parseBoolean(isDetected);
    }

    public static String getCURRENT_DIR() {
        return CURRENT_DIR;
    }

    public static String getMyUUID() {
        return myUUID;
    }

    public static Logger getLogger() {
        return logger;
    }

    private static String getGenericParameter(String key, String theDefault) {
        Properties prop = new Properties();
        InputStream input;
        File myFile = new File(CONFIG_FILE);

        try {
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            input = new FileInputStream(CONFIG_FILE);
            // load a properties file
            prop.load(input);
            String param = prop.getProperty(key);
            if (param != null) {   // just return default MQTT
                return param;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return theDefault;
    }

    private static IPLocationData getIpLocationMeta() {
        if (!detectLocation()){
            return new IPLocationData();
        }
        try {
            URL url = new URL("http://ip-api.com/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder json = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                json.append(line + "\n");
            }
            rd.close();

            if (!json.toString().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                IPLocationData ipLocation = mapper.readValue(json.toString(), IPLocationData.class);
                return ipLocation;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            System.out.println("Cannot enhance the metadata, this is not an error.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Cannot enhance the metadata, this is not an error.");
        }
        return new IPLocationData(); // aviod error
    }
}

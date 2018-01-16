/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.ext.calvin;

import sinc.hinc.ext.calvin.nodeproperties.CalvinNodeProperties;
import sinc.hinc.ext.calvin.model.CalvinIdResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ServiceDetector;
import sinc.hinc.abstraction.ResourceDriver.utils.RestHandler;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;

/**
 *
 * @author hungld
 */
public class CalvinDetector implements ServiceDetector {

    @Override
    public MicroserviceArtifact detect(Map<String, String> settings) {
        String ip = getEth0Address();
        try {
            System.out.println("  -- CalvinDetector - IP :" + ip);
            String idJson = RestHandler.build("http://" + ip + ":5001/id").callGet();
            System.out.println("  -- CalvinDetector - idJSON :" + idJson);
            ObjectMapper mapper = new ObjectMapper();
            CalvinIdResponse id = mapper.readValue(idJson, CalvinIdResponse.class);
            if (id != null) {
                System.out.println("  -- CalvinDetector - Id in string :" + id.getId());
                MicroserviceArtifact calvinService = new MicroserviceArtifact();
                calvinService.setEndpoint("http://" + ip + ":5001");
                calvinService.setName("calvin");
                calvinService.setResourceID(id.getId());

                // this is a hack, to read from file, not from Calvin API
                CalvinNodeProperties prop = CalvinNodeProperties.fromJsonFile(new File("calvin/node.attributes"));
                Map<String, String> meta = new HashMap<>();
                meta.put("floor", prop.getIndexed_public().getAddress().getFloor());
                meta.put("room", prop.getIndexed_public().getAddress().getRoom());
                meta.put("name", prop.getIndexed_public().getNode_name().getName());
                meta.put("purpose", prop.getIndexed_public().getNode_name().getPurpose());
                meta.put("organization", prop.getIndexed_public().getNode_name().getOrganization());
                calvinService.setMeta(meta);

                System.out.println("  -- CalvinDetector - detect 1 calvin");
                return calvinService;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return "calvin";
    }

    public static String getEth0Address() {
        System.out.println("Geting eth0");
        Enumeration en;
        try {
            en = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            return null;
        }
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            System.out.println("Interface name: " + i.getName());
            if (i.getName().equals("eth0") || i.getName().equals("wlan0")) {
                for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                    InetAddress addr = (InetAddress) en2.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        if (addr instanceof Inet4Address) {
                            return addr.getHostAddress();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "unknown";
        }
    }

}

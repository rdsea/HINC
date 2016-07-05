/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import static java.lang.System.out;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import sinc.hinc.common.metadata.InfoSourceSettings;
import sinc.hinc.abstraction.ResourceDriver.PluginFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.common.utils.HincUtils;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;

/**
 * This thread is spawn to collect information of IoT provider and save to DB. This class will wrap all the capabilities (can be from multiple provider) into a
 * single software defined gateway to save.
 *
 * @author hungld
 */
public class CollectResourceIoT implements Runnable {

    InfoSourceSettings.InfoSource source;

    public CollectResourceIoT(InfoSourceSettings.InfoSource source) {
        this.source = source;
    }

    @Override
    public void run() {
//        System.out.println("Collecting information from source: " + source.getName());
//
//        // check each source that configured for HINC
//        if (!source.getType().equals(InfoSourceSettings.ProviderType.IoT)) {
//            System.out.println("The source " + source.getType() + " is not IoT provider, you are calling wrong HINC's method.");
//            return;
//        }
//        ProviderAdaptor adaptor = PluginFactory.getProviderAdaptor(source.getAdaptorClass());
//        IoTResourceTransformation transformer = PluginFactory.getIoTResourceTransformer(source.getTransformerClass());
//
//        System.out.println("Loading plugin done: " + adaptor.getClass().getSimpleName() + ", and: " + transformer.getClass().getSimpleName());
//        // query provider to get information
//        Collection<Object> rawInfo = adaptor.getItems(source.getSettings());
//        System.out.println("Got raw info from provider: " + rawInfo.size() + " items");
//
//        // TODO: add execution and connectivity query and check the exception. E.g. a transformation can be not available.
//        while (true) {
//            // create a software defined gateway to carry capabilities
//            SoftwareDefinedGateway gw = new SoftwareDefinedGateway();
//            gw.setUuid(HincConfiguration.getMyUUID());
//            gw.setName(HincUtils.getHostName());
//            for (Object domain : rawInfo) {
//                DataPoint dp = transformer.updateDataPoint(domain);
//                List<ControlPoint> cps = transformer.updateControlPoint(domain);
//                gw.hasCapability(dp);
//                gw.hasCapabilities(cps);
//            }
//            System.out.println("Transform GW done, number of datapoint: " + gw.getDataPoints().size() +", controlpoint:" + gw.getControlPoints().size());
//            SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
//            gwDAO.save(gw);
//
//            // query 1 time or continuously
//            if (source.getInterval() == 0) {
//                System.out.println("Interval equals 0, query done!");
//                break;
//            } else if (source.getInterval() > 0) {
//                try {
//                    System.out.println("Sleeping " + source.getInterval() + " before next query.");
//                    Thread.sleep(source.getInterval() * 1000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
    }
    
    
    
    

    /**
     * This get the network information of current machine/container This is the connectivity on the machine of the collector, but we assume that the collector
     * is deployed on gateways, thus it is the information of the gateway.
     *
     * @return
     */
    @Deprecated
    private static List<CloudConnectivity> getGatewayConnectivity() {
        List<CloudConnectivity> cons = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                if (!netint.isLoopback()) {
                    out.println("No, it is not a loopback. it is: " + netint.getName());
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

                    // ok, create a new connection, geting IPv4 and MAC
                    String ipv4 = "";
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        System.out.println("Checking ip: " + inetAddress);
                        if (inetAddress instanceof Inet4Address) {
                            ipv4 = inetAddress.toString();
                            ipv4 = ipv4.substring(ipv4.indexOf("/") + 1);
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    byte[] mac = netint.getHardwareAddress();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    String macStr = sb.toString();  // get MAC
                    CloudConnectivity c = new CloudConnectivity(HincConfiguration.getMyUUID(), "Interface-" + HincConfiguration.getLocalMeta().getIp(), "SD Gateway", ipv4, macStr);
                    cons.add(c);

                } else {
                    out.print("Loop back");
                }
                out.printf("\n+++++++++++++\n");

            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        return cons;
    }

}

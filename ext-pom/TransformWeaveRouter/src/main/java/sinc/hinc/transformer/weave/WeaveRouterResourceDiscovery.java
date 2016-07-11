/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.weave;


import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weaveworks.weave.WeaveRouter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sinc.hinc.abstraction.transformer.NetworkResourceTranformationInterface;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 *
 * @author hungld
 */
public class WeaveRouterResourceDiscovery implements NetworkResourceTranformationInterface<WeaveRouter> {

    @Override
    public WeaveRouter validateAndConvertToDomainModel(String rawData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(rawData, WeaveRouter.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public VNF toVNF(WeaveRouter weave) {
        if (weave == null) {
            System.out.println("Error: WeaveRouter information is null");
            return null;
        }

        String id = weave.getRouter().getName();
        String name = weave.getRouter().getNickName();
        String protocol = weave.getRouter().getProtocol();
        AccessPoint ap = new AccessPoint(Utils.getEth0Address());

        VNF vnf = new VNF(name, protocol, ap);
        System.out.println("Should not be null !");

        List<ControlPoint> cps = new ArrayList<>();

        cps.add(new ControlPoint(id, name, "stop", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, "/usr/local/bin/weave stop"));
        System.out.println("cps size: " + cps.size());
        cps.add(new ControlPoint(id, name, "launch", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, "/usr/local/bin/weave launch"));
        System.out.println("cps size: " + cps.size());
        cps.add(new ControlPoint(id, name, "connect", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, "/usr/local/bin/weave connect")); //, "peerIP"
        System.out.println("cps size: " + cps.size());
        cps.add(new ControlPoint(id, name, "forget", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, "/usr/local/bin/weave forget")); //, "peerIP"
        System.out.println("cps size: " + cps.size());
        cps.add(new ControlPoint(id, name, "attach", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, "/usr/local/bin/weave attach")); // , "deviceIP"
        System.out.println("cps size: " + cps.size());
        cps.add(new ControlPoint(id, name, "detach", ControlPoint.InvokeProtocol.LOCAL_EXECUTE, "/usr/local/bin/weave attach")); //, "deviceIP"
        System.out.println("cps size: " + cps.size());

        vnf.setControlPoints(cps);
        if (vnf.getControlPoints() == null) {
            System.out.println("ControlPoint list is null, not initiated");
        }
        System.out.println("vnf controlpoint size: " + vnf.getControlPoints().size());
        return vnf;
    }

}

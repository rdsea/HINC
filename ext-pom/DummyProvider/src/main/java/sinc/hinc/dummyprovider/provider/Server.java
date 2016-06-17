/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

/**
 *
 * @author hungld
 */
public class Server {

    protected Server(String port, int numberOfSensor) throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(DummyREST.class);
        sf.setResourceProvider(DummyREST.class, new SingletonResourceProvider(new DummyREST(numberOfSensor)));
        sf.setResourceClasses(DummyREST.class);
        sf.setAddress("http://localhost:"+port+"/");
        sf.create();        
    }

    public static void main(String[] args) throws Exception {
        if (args.length!=2){
            System.out.println("Must provide 2 parameter: http-port and number-of-sensor. QUIT !");
            return;
        }
        new Server(args[0], Integer.parseInt(args[1]));
        System.out.println("Server ready...");
    }

}

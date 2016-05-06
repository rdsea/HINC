/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.API;


import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import sinc.hinc.common.API.HINCGlobalAPI;

/**
 *
 * @author hungld
 */
public class Main {
    public static void main(String args[]) throws Exception {
	        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
	        sf.setResourceClasses(HINCGlobalAPI.class);
	        sf.setResourceProvider(HINCGlobalAPI.class, 
	            new SingletonResourceProvider(new HINCGlobalAPIImpl()));
	        sf.setAddress("http://localhost:8080/");
	
	        sf.create();
	    }
}

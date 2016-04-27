/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sinc.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import sinc.hinc.abstraction.ResourceDriver.PluginFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.abstraction.transformer.NetworkResourceTranformationInterface;
import sinc.hinc.communication.Utils.HincUtils;
import sinc.hinc.local.utils.HincConfiguration;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 * This class is for spawning thread, which get information from Network provider and save to DB.
 * @author hungld
 */
public class CollectResourceNetwork implements Runnable {

    InfoSourceSettings.InfoSource source;

    public CollectResourceNetwork(InfoSourceSettings.InfoSource source) {
        this.source = source;
    }

    @Override
    public void run() {
        System.out.println("Collect information from network provider: " + source.getName());

        System.out.println("Checking resource: " + source.getName() + ", interval: " + source.getInterval());

        if (!source.getType().equals(InfoSourceSettings.ProviderType.Network)) {
            return;
        }
        ProviderAdaptor rawCollector = PluginFactory.getProviderAdaptor(source);
        Collection<Object> rawInfo = rawCollector.getItems(source.getSettings());
        for (Object routerInfo : rawInfo) {
            VNF vnf = new VNF();
            vnf.setUuid(HincConfiguration.getMyUUID());
            vnf.setName(HincUtils.getHostName());
            NetworkResourceTranformationInterface transformer = PluginFactory.getNetworkResourceTransformer(source);
            System.out.println("Created tranformer instance done: " + transformer.getClass());
            vnf = transformer.toVNF(routerInfo);
            AbstractDAO<VNF> vnfDAO = new AbstractDAO<>(VNF.class);
            vnfDAO.save(vnf);
        }

    }

}

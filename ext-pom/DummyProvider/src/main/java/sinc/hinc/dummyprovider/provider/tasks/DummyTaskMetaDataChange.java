/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import sinc.hinc.dummyprovider.controller.ChangePolicy;
import sinc.hinc.dummyprovider.provider.DummyData;
import sinc.hinc.dummyprovider.provider.DummyMetadataItem;

/**
 *
 * @author hungld
 */
public class DummyTaskMetaDataChange implements Runnable {

    ChangePolicy changePolicy;
    DummyData dummyData;

    public DummyTaskMetaDataChange(ChangePolicy changePolicy, DummyData dummyData) {
        System.out.println("New DummyTaskMetaDataChange generated");
        this.changePolicy = changePolicy;
        this.dummyData = dummyData;
    }

    @Override
    public void run() {
        Long st_device_change = new Date().getTime();
        System.out.println("===========================================");
        System.out.println("DummyTaskMetaDataChange: " + changePolicy.toJson() + "\n");
        int numberOfChange = this.changePolicy.getNumberOfChange();
        int size = this.dummyData.getDataItems().size();
        List<DummyMetadataItem> dataItemsToSend = new ArrayList<>();
        for (int i = 1; i <= numberOfChange; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, size);
            System.out.println("Change metadata of sensor: " + randomNum + " from:" + dummyData.getDataItems().get(randomNum).toJson());
            dummyData.getDataItems().get(randomNum).reGenerateMetadata();
            System.out.println("Change metadata of sensor: " + randomNum + " to  :" + dummyData.getDataItems().get(randomNum).toJson() + "\n");
            dataItemsToSend.add(dummyData.getDataItems().get(randomNum));
        }
        Map<String, String> meta = new HashMap<>();
        meta.put("st_device_change", st_device_change + "");
        new DummyTaskPushHINCLocal(dummyData, dataItemsToSend, meta).push();
    }

}

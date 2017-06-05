/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import sinc.hinc.dummyprovider.controller.ChangePolicy;

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
        System.out.println("===========================================");
        System.out.println("DummyTaskMetaDataChange: " + changePolicy.toJson() + "\n");
        int numberOfChange = this.changePolicy.getNumberOfChange();
        int size = this.dummyData.getDataItems().size();
        for (int i = 1; i <= numberOfChange; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, size);
            System.out.println("Change metadata of sensor: " + randomNum + " from:" + dummyData.getDataItems().get(randomNum).toJson());
            dummyData.getDataItems().get(randomNum).reGenerateMetadata();
            System.out.println("Change metadata of sensor: " + randomNum + " to  :" + dummyData.getDataItems().get(randomNum).toJson() + "\n");
        }
    }

}

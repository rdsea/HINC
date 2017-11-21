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
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.dummyprovider.controller.ChangePolicy;
import sinc.hinc.dummyprovider.provider.DummyData;
import sinc.hinc.dummyprovider.provider.DummyMetadataItem;

/**
 *
 * @author hungld
 */
public class DummyTaskSensorInOut implements Runnable {

    static Logger logger = LoggerFactory.getLogger("Dummy");
    ChangePolicy changePolicy;
    DummyData dummyData;
    boolean phaseMoveOut = true;

    public DummyTaskSensorInOut(ChangePolicy changePolicy, DummyData dummyData) {
        this.changePolicy = changePolicy;
        this.dummyData = dummyData;
    }

    @Override
    public void run() {
        Long st_device_change = new Date().getTime();
        logger.debug("===========================================");
        logger.debug("DummyTaskSensorInOut: " + changePolicy.toJson());
        int numberOfChange = this.changePolicy.getNumberOfChange();
        int derivation = this.changePolicy.getDerivation();
        Random rand = new Random();
        // do not change everything
        int actualDerivation = rand.nextInt(derivation);
        int numberOfChangeRnd = numberOfChange + actualDerivation;
        if (rand.nextBoolean()) {
            numberOfChangeRnd = numberOfChange - 2 * actualDerivation;
        }

        logger.debug("Phase Moveout: " + this.phaseMoveOut + ", sensor number to move: " + numberOfChangeRnd);
        List<DummyMetadataItem> dataItemsToSend = new ArrayList<>();
        if (this.phaseMoveOut) {
            if (this.dummyData.getBuffers().isEmpty()) {
                numberOfChangeRnd = numberOfChangeRnd * 2;
                logger.debug("Sensor out and the buffer is empty, multiple numberOfChange by 2: " + numberOfChangeRnd);
            }
            for (int i = 1; i <= numberOfChangeRnd; i++) {
                // move out maximum half of total sensors
                if (this.dummyData.getBuffers().size() < this.dummyData.getDataItems().size()) {
                    this.dummyData.getBuffers().offer(this.dummyData.getDataItems().get(0));
                    //logger.debug("A sensor is moving out: " + this.dummyData.getDataItems().get(0).getId());
                    DummyMetadataItem itemToRemove = new DummyMetadataItem(this.dummyData.getDataItems().get(0).getId());
                    this.dummyData.getDataItems().remove(0);
                    dataItemsToSend.add(itemToRemove);
                }
            }
        } else {
            if (this.dummyData.getBuffers().size() >= this.dummyData.getDataItems().size()) {
                numberOfChangeRnd = numberOfChangeRnd * 2;
                logger.debug("sensor in and the buffer is full, multiply numberOfChange by 2: " + numberOfChangeRnd);
            }
            for (int i = 1; i <= numberOfChangeRnd; i++) {
                if (!this.dummyData.getBuffers().isEmpty()) {
                    //logger.debug("A sensor is moving in: " + this.dummyData.getBuffers().peek().getId());
                    DummyMetadataItem dataitem = this.dummyData.getBuffers().poll();
                    this.dummyData.getDataItems().add(dataitem);
                    dataItemsToSend.add(dataitem);
                }
            }
        }
        phaseMoveOut = !phaseMoveOut;

        logger.debug("Current sensors: " + this.dummyData.getDataItems().size() + ", buffer: " + this.dummyData.getBuffers().size());
        Map<String, String> meta = new HashMap<>();
        meta.put("st_device_change", st_device_change + "");
        new DummyTaskPushHINCLocal(dummyData, dataItemsToSend, meta).push();

    }

}

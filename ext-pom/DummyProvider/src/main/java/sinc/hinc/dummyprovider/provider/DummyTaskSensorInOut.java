/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import sinc.hinc.dummyprovider.controller.ChangePolicy;

/**
 *
 * @author hungld
 */
public class DummyTaskSensorInOut implements Runnable {

    ChangePolicy changePolicy;
    DummyData dummyData;
    boolean phaseMoveOut = true;

    public DummyTaskSensorInOut(ChangePolicy changePolicy, DummyData dummyData) {
        this.changePolicy = changePolicy;
        this.dummyData = dummyData;
    }

    @Override
    public void run() {
        System.out.println("===========================================");
        System.out.println("DummyTaskSensorInOut: " + changePolicy.toJson());
        int numberOfChange = this.changePolicy.getNumberOfChange();
        Random rand = new Random();
        // do not change everything
        int numberOfChangeRnd = rand.nextInt(numberOfChange);

        if (this.phaseMoveOut) {
            if (this.dummyData.getBuffers().isEmpty()) {
                numberOfChangeRnd = numberOfChangeRnd * 2;
            }
            for (int i = 1; i <= numberOfChangeRnd; i++) {
                // move out maximum half of total sensors
                if (this.dummyData.getBuffers().size() < this.dummyData.getDataItems().size()) {
                    this.dummyData.getBuffers().offer(this.dummyData.getDataItems().get(0));
                    System.out.println("A sensor is moving out: " + this.dummyData.getDataItems().get(0).getId());
                    this.dummyData.getDataItems().remove(0);
                }
            }
        } else {
            if (this.dummyData.getBuffers().size() >= this.dummyData.getDataItems().size()) {
                numberOfChangeRnd = numberOfChangeRnd * 2;
            }
            for (int i = 1; i <= numberOfChangeRnd; i++) {
                if (!this.dummyData.getBuffers().isEmpty()) {
                    System.out.println("A sensor is moving in: " + this.dummyData.getBuffers().peek().getId());
                    this.dummyData.getDataItems().add(this.dummyData.getBuffers().poll());
                }
            }
        }
        phaseMoveOut = !phaseMoveOut;

        System.out.println("Current sensors: " + this.dummyData.getDataItems().size() + ", buffer: " + this.dummyData.getBuffers().size());

    }

}

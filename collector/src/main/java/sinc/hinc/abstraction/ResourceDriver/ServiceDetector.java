/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import java.util.Map;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;

/**
 *
 * @author hungld
 */
public interface ServiceDetector {

    MicroserviceArtifact detect(Map<String, String> settings);

    String getName();
}

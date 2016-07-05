/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import java.util.List;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;

/**
 *
 * @author hungld
 */
public interface ControlPointTransformer<DomainModel> {

    /**
     * For each datapoint or information items of provider, this extracts
     * several control. E.g. One data point can be control by several actions.
     *
     * @param data Information item captured from provider
     * @return
     */
    public List<ControlPoint> updateControlPoint(DomainModel data);

    public String getName();

}

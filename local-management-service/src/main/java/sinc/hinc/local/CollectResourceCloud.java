/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import sinc.hinc.common.metadata.InfoSourceSettings;



/**
 *
 * @author hungld
 */
public class CollectResourceCloud implements Runnable{
    InfoSourceSettings.InfoSource source;

    public CollectResourceCloud(InfoSourceSettings.InfoSource source) {
        this.source = source;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

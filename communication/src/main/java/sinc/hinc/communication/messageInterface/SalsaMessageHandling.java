/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.messageInterface;

import sinc.hinc.common.metadata.HincMessage;


/**
 *
 * @author Duc-Hung LE
 */
/*
 * Linh @Hung: change the name "Salsa"
 */
public interface SalsaMessageHandling {
    
    // handling incoming message, and reply another
    public void handleMessage(HincMessage message);
}

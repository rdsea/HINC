/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.communication.processing;


/**
 *
 * @author Duc-Hung LE
 */
/*
 * Linh @Hung: change the name "Salsa"
 */
public interface HINCMessageHander {
    
    // handling incoming message, and reply another
    public HincMessage handleMessage(HincMessage message);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible.reading;

import sinc.hinc.model.Extensible.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class CommandLineReading extends ExtensibleModel{
    String command;
    
    public CommandLineReading() {
        super(CommandLineReading.class);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    
    
}

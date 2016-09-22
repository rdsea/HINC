/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import java.io.File;
import java.util.TimerTask;

/**
 *
 * @author hungld
 */
public abstract class FileWatcherWithTimer extends TimerTask {

    private long timeStamp = Long.MAX_VALUE;
    private File file;

    public FileWatcherWithTimer(File file) {
        this.file = file;
    }

    @Override
    public final void run() {
        long currentTimeStamp = file.lastModified();

        if (this.timeStamp != currentTimeStamp) {
            System.out.println("File timestamp is changed. Last time: " + this.timeStamp + ". Current:" + currentTimeStamp);
            this.timeStamp = currentTimeStamp;
            onChange(file);
        }

    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    protected abstract void onChange(File file);
}

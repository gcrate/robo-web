package com.gcrate.robo.control;

/**
 *
 * @author gcrate
 */
public class ExecutorWait implements MotorCommand {
    private int waitTimeMs;

    public ExecutorWait(int waitTimeMs) {
        this.waitTimeMs = waitTimeMs;
    }

    public int getWaitTimeMs() {
        return waitTimeMs;
    }
    
}

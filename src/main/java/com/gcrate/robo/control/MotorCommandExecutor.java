package com.gcrate.robo.control;

import java.util.List;

/**
 *
 * @author gcrate
 */
public class MotorCommandExecutor {
    
    private final List<MotorCommand> commands;
    private Motor motor;
    private boolean started = false;
    private boolean complete = false;
    
    
    public MotorCommandExecutor(Motor motor, List<MotorCommand> commands) {
        this.commands = commands;
        this.motor = motor;
    }
    
    public void startProgram() {
        if(!started) {
            started = true;
            Thread thread = new Thread(new MotorCommandExecutorThread());
            thread.start();
        }
    }
    
    
    public boolean isComplete() {
        return complete;
    }
    
    public class MotorCommandExecutorThread implements Runnable {

        @Override
        public void run() {
            for(MotorCommand mc : commands) {
                if(mc instanceof MotorState) {
                    motor.setState((MotorState)mc);
                } else if (mc instanceof ExecutorWait) {
                    try {
                        Thread.sleep(((ExecutorWait)mc).getWaitTimeMs());
                    } catch (InterruptedException ex) {
                        System.err.println("unable to sleep thread:" + ex.getMessage());
                    }
                }
            }
        }
        
    }
}

package com.gcrate.robo.web;

import com.gcrate.robo.control.ExecutorWait;
import com.gcrate.robo.control.MotorCommand;
import com.gcrate.robo.control.MotorCommandExecutor;
import com.gcrate.robo.control.MotorId;
import com.gcrate.robo.control.MotorManager;
import com.gcrate.robo.control.MotorState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author gcrate
 */
@RestController
public class RoboRestController {
    @Autowired
    MotorManager motorManager;

    public static final String WAIT_PREFIX = "WAIT-";
    
    private Map<MotorId, MotorCommandExecutor> runningPrograms;
    
    @RequestMapping("/execute")
    public void executeProgram(@RequestBody List<MotorProgram> programs) throws RoboWebException {
        //Validate
        Set<MotorId> programMotorIds = new HashSet<>();
        for(MotorProgram program : programs) {
            MotorCommandExecutor existingExecutor = runningPrograms.get(program.motorId);
            
            if(existingExecutor != null && !existingExecutor.isComplete()) {
                throw new RoboWebException("Program already running for motor: " + program.motorId.name());
            }
            
            if(programMotorIds.contains(program.motorId)) {
                throw new RoboWebException("Duplicate program for motorId:" + program.motorId.name());
            }
            programMotorIds.add(program.motorId);
            
        }
        //Parse & Setup
        MotorCommandExecutor[] executors = new MotorCommandExecutor[programs.size()];
        for (int i = 0; i < programs.size(); i++) {
            MotorProgram program = programs.get(i);
            
            List<MotorCommand> commands = new ArrayList<>();
            
            for(String cmdStr: program.commands) {
                commands.add(parseCommandString(cmdStr));
            }
            
            MotorCommandExecutor exec = new MotorCommandExecutor(motorManager.getMotor(program.motorId), commands);
            executors[i] = exec;
        }
        
        //Start Execution
        for(MotorCommandExecutor ex : executors) {
            ex.startProgram();
            runningPrograms.put(ex.motor.id, ex);
        }
    }
    
    private MotorCommand parseCommandString(String cmdStr) throws RoboWebException {
        if(cmdStr.startsWith(WAIT_PREFIX)) {
            Integer waitSeconds = null;
            try {
                waitSeconds = Integer.valueOf(cmdStr.substring(WAIT_PREFIX.length()));
            } catch (NumberFormatException ex) {
                throw new RoboWebException("Invalid wait command:" + cmdStr);
            }
            return new ExecutorWait(waitSeconds);
        }
        else {
            try {
                return MotorState.valueOf(cmdStr);
            } catch (IllegalArgumentException ex) {
                throw new RoboWebException(("Invalid command: " + cmdStr));
            }
        }
    }
}

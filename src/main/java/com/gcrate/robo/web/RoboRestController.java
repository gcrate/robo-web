package com.gcrate.robo.web;

import com.gcrate.robo.control.MotorCommandExecutor;
import com.gcrate.robo.control.MotorId;
import com.gcrate.robo.control.MotorManager;
import java.util.List;
import java.util.Map;
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
    
    private Map<MotorId, MotorCommandExecutor> runningPrograms;
    
    @RequestMapping("/execute")
    public boolean executeProgram(@RequestBody List<MotorProgram> programs) {
        for(MotorProgram program : programs) {
            MotorCommandExecutor existingExecutor = runningPrograms.get(program.motorId);
            if(existingExecutor != null && !existingExecutor.isComplete()) {
                return false; //Something using this motor is still running
            }
        }
        for(MotorProgram program : programs) {
            //create executors and start the program
        }
        return true;
    }
}

package com.gcrate.robo.control;

import com.pi4j.io.gpio.RaspiPin;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author gcrate
 */
@Service
public class MotorManager {
    private static final Map<MotorId,Motor> motors = new HashMap<MotorId, Motor>();
    static {
        //TODO: correct pin values
        motors.put(MotorId.LEG_LEFT, new Motor(RaspiPin.GPIO_00, RaspiPin.GPIO_01));
        motors.put(MotorId.LEG_RIGHT, new Motor(RaspiPin.GPIO_00, RaspiPin.GPIO_01));
        motors.put(MotorId.HIPS, new Motor(RaspiPin.GPIO_00, RaspiPin.GPIO_01));
    }
    
    public Motor getMotor(MotorId id) {
        return motors.get(id);
    }
}

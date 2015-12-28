package com.gcrate.robo.web;

import com.gcrate.robo.control.MotorId;
import java.util.List;

/**
 *
 * @author gcrate
 */
public class MotorProgram {
    public MotorId motorId;
    public List<String> commands; //MotorState or WAIT-[timeMs]
}

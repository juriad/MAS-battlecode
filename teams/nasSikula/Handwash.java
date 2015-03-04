package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Handwash extends BaseBot {
    public Handwash(RobotController rc) {
        super(rc);
    }

    public void execute() throws GameActionException {
    	//System.out.println(rc.getID() + ": alive " );
        rc.yield();
    }
}
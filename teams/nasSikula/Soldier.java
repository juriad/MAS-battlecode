package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Soldier extends BaseBot {
    public Soldier(RobotController rc) {
        super(rc);
    }

    public void execute() throws GameActionException {
        transferSupplies();
        rc.yield();
    }
}
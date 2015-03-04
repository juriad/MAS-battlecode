package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Basher extends BaseBot {

    public Basher(RobotController rc) {
        super(rc);
    }

    @Override
    public void execute() throws GameActionException {
        transferSupplies();
        rc.yield();
    }

}
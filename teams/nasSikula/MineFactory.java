package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class MineFactory extends BaseBot {

    public MineFactory(RobotController rc) {
        super(rc);
    }

    @Override
    public void execute() throws GameActionException {
        transferSupplies();
        rc.yield();
    }

}

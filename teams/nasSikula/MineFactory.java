package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class MineFactory extends BaseBot {

    public MineFactory(RobotController rc) {
        super(rc);
    }

    @Override
    public void execute() throws GameActionException {
    	spawn();
        transferSupplies();
        rc.yield();
    }
    private void spawn() throws GameActionException {
        RobotType spawnOrBuild = Objectives.OBJECTIVES.spawnOrBuild(type);
        if (rc.isCoreReady() && spawnOrBuild != null) {
            Direction direction = getSpawnDirection(spawnOrBuild);
            if (direction != null) {
                rc.spawn(direction, spawnOrBuild);
            }
        }
    }

}

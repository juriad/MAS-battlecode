package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQ extends Tower {

    public HQ(RobotController rc) {
        super(rc);
    }

    private void countRobots() {
        int[] counts = new int[RobotType.values().length];
        RobotInfo[] allies = getAllies();
        for (RobotInfo ri : allies) {
            counts[ri.type.ordinal()]++;
        }
        for (RobotType rt : RobotType.values()) {
            Registry.ROBOT_COUNT.setCount(rt, counts[rt.ordinal()]);
        }
    }

    private void spawnBeavers() throws GameActionException {
        RobotType spawnOrBuild = Objectives.OBJECTIVES.spawnOrBuild(type);
        if (rc.isCoreReady() && spawnOrBuild != null) {
            Direction direction = getSpawnDirection(spawnOrBuild);
            if (direction != null) {
                rc.spawn(direction, spawnOrBuild);
            }
        }
    }

    public void execute() throws GameActionException {
        countRobots();
        if (!defend()) {
            spawnBeavers();
        }

        transferSupplies();
        rc.yield();
    }
}
package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class Tower extends BaseBot {
    public Tower(RobotController rc) {
        super(rc);
    }

    public void execute() throws GameActionException {
        defend();
        transferSupplies();
        rc.yield();
    }

    protected boolean defend() throws GameActionException {
        RobotInfo[] enemiesInAttackingRange = getEnemiesInAttackingRange();
        attackLeastHealthEnemy(enemiesInAttackingRange);
        return enemiesInAttackingRange.length > 0;
    }
}
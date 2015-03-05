package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public abstract class AttackingBot extends MovingBot {

	public AttackingBot(RobotController rc) {
		super(rc);

	}

	public void execute() throws GameActionException {
		
		MapLocation target = null;
		if (rc.getType() != RobotType.BASHER){
				attackLeastHealtyEnemyInRange();
		}

		if (target == null) {
			Direction initialMoveDirection = getInitialMoveDirection();
			if (initialMoveDirection != Direction.NONE && rc.isCoreReady()) {
				rc.move(initialMoveDirection);
			} else {
				markDeadEnd();
			}

		}

		transferSupplies();
		rc.yield();
	}
}

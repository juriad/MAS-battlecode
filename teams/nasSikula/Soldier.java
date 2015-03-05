package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Soldier extends MovingBot {

	public Soldier(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
		Direction initialMoveDirection = getInitialMoveDirection();
		if (initialMoveDirection != Direction.NONE && rc.isCoreReady()) {
			rc.move(initialMoveDirection);
		} else {
			markDeadEnd();
		}

		transferSupplies();
		rc.yield();
	}
}

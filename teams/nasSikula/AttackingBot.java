package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public abstract class AttackingBot extends MovingBot {

	public AttackingBot(RobotController rc) {
		super(rc);

	}

	public void execute() throws GameActionException {
		boolean attacking = attackLeastEnemiesInAttackingRange();

		if (!attacking) {
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

package nasSikula;

import nasSikula.context.Registry;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.TerrainTile;

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

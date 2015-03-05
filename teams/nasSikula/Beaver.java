package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Beaver extends Miner {
	public static final double RANDOM_DIRECTION = 0.8;

	public Beaver(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
		if (getAttackDirection() != Direction.NONE) {
			runToSafety();
		} else {
			spawnOrBuild();
			mineOrMoveTowardsOre();
		}

		transferSupplies();
		rc.yield();
	}
}

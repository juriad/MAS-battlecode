package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Beaver extends Miner {
	public static final double RANDOM_DIRECTION = 0.8;

	public Beaver(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
		MapLocation attackLocation = getAttackDirection();
		if (attackLocation != null) {
			runToSafetyOrAttack(attackLocation);// 15 tank

			// Name Cost HP Attack Range
			// Beaver 100 ore, 20 turns 30 4 5
			// Miner 60 ore, 20 turns 50 3 5
			// Soldier 60 ore, 20 turns 40 4 8
			// Basher 80 ore, 20 turns 64 4 2*
			// Tank 250 ore, 50 turns 144 20 15

		} else {
			spawnOrBuild();
			mineOrMoveTowardsOre();
		}

		markDeadEnd();
	}
}

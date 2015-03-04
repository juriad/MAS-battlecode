package nasSikula;

import nasSikula.context.Objectives;
import nasSikula.context.Registry;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Beaver extends Miner {
	public static final double RANDOM_DIRECTION = 0.8;

	public Beaver(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
		if (getAttackDirection() != Direction.NONE) {
			runToSafety();
		} else {
			build();
			if (!mine()) {
				moveTowardsOre();
			}
		}

		transferSupplies();
		rc.yield();
	}

	/**
	 * @return true if building
	 * @throws GameActionException
	 */
	private boolean build() throws GameActionException {
		RobotType spawnOrBuild = Objectives.OBJECTIVES.spawnOrBuild(type);
		if (spawnOrBuild == null) {
			return false;
		}
		System.out.println("Want to build " + spawnOrBuild);// TODO dat do logu
															// tak aby to slo
															// vypnout

		Direction direction = getBuildDirection(spawnOrBuild);
		if (rc.isCoreReady() && direction != null && canBuildHere()) {
			// increase before to prevent others to build it
			Registry.ROBOT_COUNT.setCount(spawnOrBuild,
					Registry.ROBOT_COUNT.getCount(spawnOrBuild));
			rc.build(direction, spawnOrBuild);
			return true;
		}
		return false;
	}

	private boolean canBuildHere() {
		RobotInfo[] nearby = rc.senseNearbyRobots(4);
		int buildings = 0;
		for (RobotInfo ri : nearby) {
			if (ri.type.isBuilding) {
				buildings++;
			}
		}
		return buildings == 0;
	}
}
package nasSikula;

import nasSikula.context.Registry;
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

	public void execute() throws GameActionException {
		countRobots();
		if (!defend()) {
			spawnOrBuild();
		}

		transferSupplies();
		rc.yield();
	}
}
package nasSikula;

import nasSikula.context.Registry;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQ extends Tower {
	// public static List<BuildBuilding> listOfBuildingsProbabilities = null;
	// public static int sumOfBuildings = 0;
	// public static HQ HQ = null;
	public HQ(RobotController rc) {
		
		super(rc);
//		scanSouroundings(rc);
	}
//	private void scanSouroundings(RobotController rc) {
		//		MapLocation[] objects ;
//		System.out.println("Enemy HQ " + rc.senseEnemyHQLocation());
//		for (MapLocation tower : rc.senseEnemyTowerLocations()) {
//			System.out.println("tower "+ tower);
//		}
//	}
	

	private void countRobots() {
		int[] counts = new int[RobotType.values().length];
		RobotInfo[] allies = getAllies();
		for (RobotInfo ri : allies) {
			counts[ri.type.ordinal()]++;
		}
		Registry.ROBOT_COUNT.reset();
		for (RobotType rt : RobotType.values()) {
			Registry.ROBOT_COUNT.setCount(rt, counts[rt.ordinal()]);
		}
	}

	public void execute() throws GameActionException {
		countRobots();
		// System.out.println("Was " + Registry.CAPTAIN_COUNTER.get()
		// + " captains.");
		Registry.CAPTAIN_COUNTER.reset();
		if (!defend()) {
			spawnOrBuild();
		}
		// evaluateBuildingsToBuild();
	}

	// public void evaluateBuildingsToBuild(){
	// //TODO nejde to predavat takhle - nevim jak to ze se jim to povedlo - lze
	// to predat staticky
	// HQ.listOfBuildingsProbabilities = new ArrayList<BuildBuilding>();
	// HQ.sumOfBuildings = 0;
	// for (RobotType rt : Objectives.OBJECTIVES.buildables) {
	// int count = Objectives.OBJECTIVES.howManyToBuild(rt);
	// if (count == 0)
	// continue;
	// HQ.sumOfBuildings += count;
	// HQ.listOfBuildingsProbabilities.add(new BuildBuilding(count, rt));
	// }
	// System.out.println("print" + HQ.sumOfBuildings);
	// }

}
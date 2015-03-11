package nasSikula.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import nasSikula.HQ;
import battlecode.common.Clock;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Objectives {
	public static Objectives OBJECTIVES = null;
	public static Random Rand = null;

	private static final int buildablesCount = 9;
	public static final RobotType[] buildables = new RobotType[buildablesCount];
	private static int numberOfRounds;

	public static void init(RobotController rc) throws Exception {
		OBJECTIVES = new Objectives(rc);
		Rand = new Random();
		numberOfRounds = rc.getRoundLimit();

		int i = 0;
		for (RobotType rt : RobotType.values()) {
			if (rt.isBuildable()) {
				buildables[i] = rt;
				i++;
			}
		}
	}

	public static final double SOLDIER_BASHER_RATE = 0.8;

	private final RobotController rc;

	public Objectives(RobotController rc) {
		this.rc = rc;
	}

	/**
	 * 
	 * @param robot
	 *            type
	 * @return robot type or null
	 */
	private RobotType wantToBuild(RobotType robot) {
		int optimalNumber = getOptimalNumber(robot);
		int currentNumber = Registry.ROBOT_COUNT.getCount(robot);
		if (optimalNumber > currentNumber) {
			System.out.println("want to build " + robot + " we have "
					+ currentNumber + "/" + optimalNumber);
			return robot;
		}
		return null;
	}



	public int howManyToBuild(RobotType robot) {
		int optimalNumber = getOptimalNumber(robot);
		int currentNumber = Registry.ROBOT_COUNT.getCount(robot);
		int difference = optimalNumber - currentNumber;
		if (difference > 0)
			return difference;
		return 0;
	}

	public RobotType getBuildingToBuild2() {
		//this is simple (uniform) distribution
		int index = Rand.nextInt(buildablesCount);
		RobotType rt = buildables[index];
		return rt;
	}
				
	public RobotType getBuildingToBuild(){
		evaluateBuildingsToBuild();//TODO put to HQ
		
		int sum = sumOfBuildings;
		if (sum == 0){//we want to build something
			return null;
		}
		
		int index = Rand.nextInt(sum);
		for (BuildBuilding building : listOfBuildingsProbabilities) {
			index -= building.howMany;
			if (index <= 0){
				//pouze pokud mame zaruceno postaveni...
//				building.howMany--;
//				HQ.HQ.sumOfBuildings--;
				
				System.out.println(building.type);
				
				return building.type;
			}
		}

		
		return null;
	}
	ArrayList<BuildBuilding> listOfBuildingsProbabilities;
	int sumOfBuildings;
	public void evaluateBuildingsToBuild(){
		//TODO nejde to predavat takhle - nevim jak to ze se jim to povedlo - lze to predat staticky
		listOfBuildingsProbabilities = new ArrayList<BuildBuilding>();
		sumOfBuildings = 0;
		for (RobotType rt : Objectives.OBJECTIVES.buildables) {
			int count = Objectives.OBJECTIVES.howManyToBuild(rt);
			if (count == 0)
				continue;
			sumOfBuildings += count;
			listOfBuildingsProbabilities.add(new BuildBuilding(count, rt));
		}
	}
	public RobotType spawnOrBuild(RobotType type) {
		// ze zacatku vyrob factory a beavery
		if (Clock.getRoundNum() < 250) {
			switch (type) {
			case HQ:
				return wantToBuild(RobotType.BEAVER);
			case BEAVER:
				return RobotType.MINERFACTORY;
			case MINERFACTORY:
				return wantToBuild(RobotType.MINER);
			default:
				return null;
			}

		}
		if (Clock.getRoundNum() > 250 && rc.getTeamOre() < 700) {
			return null;
		}
		if (!type.isBuilding) { // prorezat moznosti
			if (type == RobotType.BEAVER) {// only moving thing that builds
				RobotType rt = getBuildingToBuild();
				if (rt != null){
					return wantToBuild(rt);
				}
			}
			return null;
		}

		switch (type) {
		case HQ:
			return wantToBuild(RobotType.BEAVER);
		case TOWER: // nothing
			break;

		case MINERFACTORY:
			return wantToBuild(RobotType.MINER);

		case BARRACKS:
			if (Math.random() < SOLDIER_BASHER_RATE) {
				return wantToBuild(RobotType.SOLDIER);
			} else {
				return wantToBuild(RobotType.BASHER);
			}

		case TANKFACTORY:
			return RobotType.TANK;

		case AEROSPACELAB:
			return RobotType.LAUNCHER;

		case HELIPAD:
			return RobotType.DRONE;
		case LAUNCHER:
			return RobotType.MISSILE;

		case TECHNOLOGYINSTITUTE:
			break;
		case TRAININGFIELD:
			break;

		default:
			break;

		case MINER: // nothing
		case SOLDIER:
		case TANK:
		case BASHER:
		case COMMANDER:
		case DRONE:
		case MISSILE:

		case SUPPLYDEPOT:
		case HANDWASHSTATION:
		case COMPUTER:
			return null;
		}
		return null;
	}

	public int getOptimalNumber(RobotType type) {// kolik by jich ted melo byt
		switch (type) {
		case HQ:
		case TOWER:
			return Integer.MIN_VALUE;

		case BEAVER:
			return 10;// 5 + getOptimalNumber(RobotType.MINER) * 2;
		case SOLDIER:
			return 30;//Integer.MAX_VALUE;
		case BASHER:
			return Integer.MAX_VALUE;
		case TANK:
			return Integer.MAX_VALUE;

		case MINER:
			// System.out.println(10 + Registry.ROBOT_COUNT.getSum() / 5);
			// return 10 + Registry.ROBOT_COUNT.getSum() / 5;
			return 50;

		case MINERFACTORY:
			return (int) Math.log(Registry.ROBOT_COUNT
					.getCount(RobotType.MINER) + 3);

		case BARRACKS:
			return 2 + (int) Math.log(Registry.ROBOT_COUNT
					.getCount(RobotType.SOLDIER) + 3);

		case TANKFACTORY:
			if (rc.hasBuildRequirements(RobotType.TANKFACTORY)) {// it costs 500
				return (int) Math.log(Registry.ROBOT_COUNT
						.getCount(RobotType.TANK) + 3);
			}
			break;

		case SUPPLYDEPOT:
			return getSupplyDepodNbr();

		case HANDWASHSTATION:
			return buildInLastMinute();

		case AEROSPACELAB:
			return 1;
		case COMMANDER:
			return 1;
		case COMPUTER:
			break;
		case DRONE:
			return 1;
		case HELIPAD:
			return 1;
		case LAUNCHER:
			return 5;
		case MISSILE:
			break;
		case TECHNOLOGYINSTITUTE:
			return 1;
		case TRAININGFIELD:
			return 1;
		default:
			break;
		}
		return 0;
	}

	private int getSupplyDepodNbr() {
		int depots = Registry.ROBOT_COUNT.getCount(RobotType.SUPPLYDEPOT);
		int robots = Registry.ROBOT_COUNT.getTotalCount();
		int consumption = robots * 3; // spotreba cca - nevim jak je to s
										// tim Cooldown
		// HQ per turn is 100*(2+supply_depots^0.6)
		double generatedSupplyPerTurn = 100 * (2 + Math.pow(depots, 0.6));
		// prumer na jendotku na tah je ~ 9 ale jsou tam ty cooldown tak
		// pocitam treba 3x
		if ((generatedSupplyPerTurn / consumption) > 1) {
			return depots + 1;
		}

		return 1 + (int) (Math.log(Registry.ROBOT_COUNT
				.getCount(RobotType.SOLDIER) + 3) / 4);
	}

	private int buildInLastMinute() {
		int whenToStart = 200;
		int round = Clock.getRoundNum();
		// if no damage
		if ((round + whenToStart) >= numberOfRounds
				&& round <= (numberOfRounds - 100)) {
			// System.out.println("\n\n\n\n\nround: " + round + " whenToStart: "
			// + whenToStart + " numberOfRounds: "+ numberOfRounds);
			return 5;
		}
		return 0;
	}
}

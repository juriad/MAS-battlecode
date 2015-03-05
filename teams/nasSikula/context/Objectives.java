package nasSikula.context;

import java.util.Random;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Objectives {
	public static Objectives OBJECTIVES = null;
	public static Random Rand = null;
	
	private static final int buildablesCount = 9;
	public static final RobotType[] buildables = new RobotType[buildablesCount];
	

	public static void init(RobotController rc) throws Exception {
		OBJECTIVES = new Objectives(rc);
		Rand = new Random();
		
		int i = 0;
		for (RobotType rt : RobotType.values()) {
			if (rt.isBuildable()){
				buildables[i] = rt;
				i++;
			}
		}		
	}

	public static final double SOLDIER_BASHER_RATE = 0.7;

	private final RobotController rc;

	public Objectives(RobotController rc) {
		this.rc = rc;
	}

	/**
	 * 
	 * @param robot type
	 * @return robot type or null
	 */
	private RobotType wantToBuild(RobotType robot) {
		int optimalNumber = getOptimalNumber(robot);
		int currentNumber = Registry.ROBOT_COUNT.getCount(robot);
		if (optimalNumber > currentNumber) {
			System.out.println("want to build " + robot + " I have "
					+ currentNumber + " I want to have " + optimalNumber);
			return robot;
		}
		return null;
	}

	public RobotType spawnOrBuild(RobotType type) {
		if (Clock.getRoundNum() > 250 && rc.getTeamOre() < 500) {
			return null;
		}

		switch (type) {
		case HQ:
			return wantToBuild(RobotType.BEAVER);
		case TOWER: // nothing
			break;
		case BEAVER:
			int index = Rand.nextInt(buildablesCount);
			RobotType rt = buildables[index];
			return wantToBuild(rt);
		case MINERFACTORY:
			return wantToBuild(RobotType.MINER);
		case MINER: // nothing
			break;
		case BARRACKS:
			if (Math.random() < SOLDIER_BASHER_RATE) {
				return RobotType.SOLDIER;
			} else {
				return RobotType.BASHER;
			}
		case SOLDIER: // nothing
			break;
		case TANKFACTORY:
			return RobotType.TANK;
		case TANK: // nothing
			break;
		case SUPPLYDEPOT: // nothing
			break;
		case HANDWASHSTATION: // nothing
			break;

		case AEROSPACELAB:
			break;
		case BASHER:
			break;
		case COMMANDER:
			break;
		case COMPUTER:
			break;
		case DRONE:
			break;
		case HELIPAD:
			break;
		case LAUNCHER:
			break;
		case MISSILE:
			break;
		case TECHNOLOGYINSTITUTE:
			break;
		case TRAININGFIELD:
			break;
		default:
			break;
		}
		return null;
	}

	public int getOptimalNumber(RobotType type) {// kolik by jich ted melo byt
		switch (type) {
		case HQ:
			return Integer.MIN_VALUE;
		case TOWER:
			return Integer.MIN_VALUE;
		case BEAVER:
			return 30;// 5 + getOptimalNumber(RobotType.MINER) * 2;
		case MINERFACTORY:
			System.out.println("chci mit x MINERFACTORY "
					+ (int) Math.log(Registry.ROBOT_COUNT
							.getCount(RobotType.MINER) + 3));
			return (int) Math.log(Registry.ROBOT_COUNT
					.getCount(RobotType.MINER) + 3);
		case MINER:
			// System.out.println(10 + Registry.ROBOT_COUNT.getSum() / 5);
			// return 10 + Registry.ROBOT_COUNT.getSum() / 5;
			return 100;
		case BARRACKS:
			return 2 + (int) Math.log(Registry.ROBOT_COUNT
					.getCount(RobotType.SOLDIER) + 3);
		case SOLDIER:
			return Integer.MAX_VALUE;
		case BASHER:
			return Integer.MAX_VALUE;
		case TANKFACTORY:
			if (rc.hasBuildRequirements(RobotType.TANKFACTORY)) {// it costs 500
				return (int) Math.log(Registry.ROBOT_COUNT
						.getCount(RobotType.TANK) + 3);
			}
			break;
		case TANK:
			return Integer.MAX_VALUE;
		case SUPPLYDEPOT:
			return 1 + (int) (Math.log(Registry.ROBOT_COUNT
					.getCount(RobotType.SOLDIER) + 3) / 4);
		case HANDWASHSTATION:
			int whenToStart = 150;
			if (Clock.getRoundNum() + whenToStart >= rc.getRoundLimit()) {
				return 5;
			}
			break;

		case AEROSPACELAB:
			break;
		case COMMANDER:
			break;
		case COMPUTER:
			break;
		case DRONE:
			return Integer.MAX_VALUE;
		case HELIPAD:
			break;
		case LAUNCHER:
			return Integer.MAX_VALUE;
		case MISSILE:
			break;
		case TECHNOLOGYINSTITUTE:
			break;
		case TRAININGFIELD:
			break;
		default:
			break;
		}
		return 0;
	}
}

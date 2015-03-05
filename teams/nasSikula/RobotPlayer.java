package nasSikula;

import nasSikula.context.Objectives;
import nasSikula.context.Registry;
import battlecode.common.RobotController;

public class RobotPlayer {

	public static BaseBot convert(RobotController rc) {
		switch (rc.getType()) {
		case HQ:
			return new HQ(rc).init();
		case TOWER:
			return new Tower(rc);
		case BEAVER:
			return new Beaver(rc);
		case MINER:
			return new Miner(rc);
		case SOLDIER:
			return new Soldier(rc);
		case BASHER:
			return new Basher(rc);
		case TANK:
			return new Tank(rc);

		case BARRACKS:
		case MINERFACTORY:
		case TANKFACTORY:
		case HELIPAD:
		case AEROSPACELAB:
			return new Factory(rc);
			
		case SUPPLYDEPOT:
		case HANDWASHSTATION:
			return new NoOpBot(rc);

		case DRONE:
			//break;
		case LAUNCHER:
			//break;
		case MISSILE:
			//break;			
		case COMMANDER:
			//break;
			return new AttackingBot(rc);
			
		case COMPUTER:
			break;
		case TECHNOLOGYINSTITUTE:
			break;
		case TRAININGFIELD:
			break;
		default:
			// won't happen
			break;
		}
		return null;
	}

	public static void run(RobotController rc) throws Exception {
		Registry.init(rc);
		Objectives.init(rc);
		BaseBot myself = convert(rc);
		if (myself == null) {
			return;
		}

		while (true) {
			try {
				myself.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

package nasSikula;

import nasSikula.context.Objectives;
import nasSikula.context.Registry;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class RobotPlayer {

	public static BaseBot convert(RobotController rc) {
		switch (rc.getType()) {
			case HQ :
				return new HQ(rc);
			case TOWER :
				return new Tower(rc);
			case BEAVER :
				return new Beaver(rc);
			case MINER :
				return new Miner(rc);
			case SOLDIER :
				return new Soldier(rc);
			case BASHER :
				return new Basher(rc);
			case TANK :
				return new Tank(rc);

			case BARRACKS :
			case MINERFACTORY :
			case TANKFACTORY :
			case HELIPAD :
			case AEROSPACELAB :

			case TECHNOLOGYINSTITUTE :
			case TRAININGFIELD :
				return new Factory(rc);

			case SUPPLYDEPOT :
			case HANDWASHSTATION :
			case MISSILE :
				return new NoOpBot(rc);

			case LAUNCHER :
				return new Launcher(rc);

			case DRONE :
			case COMMANDER :
				return new AttackingBot(rc);

			case COMPUTER :
				break;
			default :
				// won't happen
				break;
		}
		return null;
	}

	protected static void transferSupplies(RobotController rc)
			throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),
				GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED, rc.getTeam());
		double lowestSupply = rc.getSupplyLevel();
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for (RobotInfo ri : nearbyAllies) {
			if (ri.supplyLevel < lowestSupply) {
				lowestSupply = ri.supplyLevel;
				transferAmount = (rc.getSupplyLevel() - ri.supplyLevel) / 2;
				suppliesToThisLocation = ri.location;
			}
		}
		if (suppliesToThisLocation != null) {
			rc.transferSupplies((int) transferAmount, suppliesToThisLocation);
		}
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
			} finally {
				transferSupplies(rc);
				rc.yield();
			}
		}
	}
}

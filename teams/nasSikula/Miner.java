package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Miner extends MovingBot {

	public Miner(RobotController rc) {
		super(rc);
	}

	private static double ORE_EPSILON = 0.8;

	protected void logic(boolean isBasher) throws GameActionException{
		MapLocation attackLocation = getAttackDirection();
		// TODO remember running from danger
		if (attackLocation != null) {
			runToSafetyOrAttack(attackLocation);

		} else {
			if (isBasher)
				spawnOrBuild();
			mineOrMoveTowardsOre();
		}

		markDeadEnd();
	}
	@Override
	public void execute() throws GameActionException {
		logic(false);
	}

	protected MapLocation towardsOre() throws GameActionException {
		int x = rc.getLocation().x;
		int y = rc.getLocation().y;
		MapLocation[] locations = MapLocation.getAllMapLocationsWithinRadiusSq(
				rc.getLocation(), 8);

		for (MapLocation ml : locations) {
			if (rc.senseRobotAtLocation(ml) == null) {
				Direction directionTo = rc.getLocation().directionTo(ml);
				double ore = rc.senseOre(ml);
				x += directionTo.dx * ore;
				y += directionTo.dy * ore;
			}
		}
		MapLocation mapLocation = new MapLocation(x, y);
		return mapLocation;
	}

	protected boolean mineOrMoveTowardsOre() throws GameActionException {
		double ore = rc.senseOre(rc.getLocation());
		if (ore < ORE_EPSILON) {
			if (moveTowards(towardsOre())) {
				// return true;
			}// moved - or not - anyway try to mine
		}
		if (rc.isCoreReady() && (ore != 0) ) {
			rc.mine();
			return true;
		}
		return false;
	}

	protected void runToSafetyOrAttack(MapLocation attackLocation)
			throws GameActionException {
		if (rc.isCoreReady()) {
			MapLocation loc = rc.getLocation();
			target = new MapLocation(2 * loc.x - attackLocation.x, 2 * loc.y
					- attackLocation.y);
			if (!moveTowards(target)) {
				attackLeastHealtyValuedEnemyInRange();
			}
		}
	}

	@Override
	protected boolean isMiner() {
		return true;
	}

}

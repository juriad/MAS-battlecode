package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Miner extends MovingBot {

	private static final double MIN_ORE_EPSILON = 0.2;
	private static final double ORE_EPSILON = 0.9;

	public Miner(RobotController rc) {
		super(rc);
	}

	@Override
	public void execute() throws GameActionException {
		if (getAttackDirection() != Direction.NONE) {
			runToSafety();
		} else {
			if (!mine()) {
				moveTowards(towardsOre(), true);
			}
		}
		transferSupplies();
		rc.yield();
	}

	protected MapLocation towardsOre() throws GameActionException {
		int x = rc.getLocation().x;
		int y = rc.getLocation().y;
		MapLocation[] locations = MapLocation.getAllMapLocationsWithinRadiusSq(
				rc.getLocation(), 4);

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

	/**
	 * @return true if robot should stay
	 * @throws GameActionException
	 */
	protected boolean mine() throws GameActionException {
		double ore = rc.senseOre(rc.getLocation());
		// if (ore < MIN_ORE_EPSILON) {
		if (ore < ORE_EPSILON) {
			return false;
		}

		// boolean isInFirstLine = true; //TODO implement
		// if (ore < ORE_EPSILON && isInFirstLine)
		// {
		// return false;
		// }

		if (rc.isCoreReady()) {
			rc.mine();
		}
		return true;
	}

	protected void runToSafety() throws GameActionException {
		Direction attackDirection = getAttackDirection();
		if (rc.isCoreReady()) {
			Direction dir = getMoveDir(attackDirection.opposite());
			if (dir != null) {
				rc.move(dir);
			}
		}
	}

}

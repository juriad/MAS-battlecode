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
				moveTowardsOre();
			}
		}
		transferSupplies();
		rc.yield();
	}

	protected void moveTowardsOre() throws GameActionException {
		Direction towardsOre = towardsOre();
		Direction moveDir = getMoveDir(towardsOre);
		if (rc.isCoreReady() && moveDir != null) {
			rc.move(moveDir);
		}
	}

	protected Direction towardsOre() throws GameActionException {
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
		MapLocation mapLocation2 = mapLocation
				.add(Direction.values()[((int) (Math.random() * 100)) % 8]);

		Direction dir = rc.getLocation().directionTo(mapLocation2);
		if (dir == Direction.OMNI) {
			dir = rc.getLocation().directionTo(mapLocation);
		}
		return dir;
	}

	/**
	 * @return true if robot should stay
	 * @throws GameActionException
	 */
	protected boolean mine() throws GameActionException {
		double ore = rc.senseOre(rc.getLocation());
		if (ore < MIN_ORE_EPSILON) {
			return false;
		}
		
//		boolean isInFirstLine = true;	//TODO implement
//		if (ore < ORE_EPSILON && isInFirstLine)
//		{
//			return false;
//		}
		
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

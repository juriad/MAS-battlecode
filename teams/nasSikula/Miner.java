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
	
	@Override
	public void execute() throws GameActionException {
		MapLocation attackLocation = getAttackDirection();
		//TODO remember running from danger
		if (attackLocation != null) {
			runToSafetyOrAttack(rc.getLocation().directionTo(attackLocation));
			
		} else {
			mineOrMoveTowardsOre();
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

	@Override
	protected double get_ore_epsilon(){
		return ORE_EPSILON;
	}
	
	
	protected void mineOrMoveTowardsOre() throws GameActionException {
		double ore = rc.senseOre(rc.getLocation());
		if (ore < ORE_EPSILON) {
			if (moveTowards(towardsOre(), true)){
				//return true;
			}//moved - or not - anyway try to mine
		}
		if (rc.isCoreReady()) {
			rc.mine();
		}
		//return false;
	}

	protected void runToSafety(Direction attackDirection) throws GameActionException {
		if (rc.isCoreReady()) {
			Direction dir = getMoveDir(attackDirection.opposite());
			if (dir != null) {
				rc.move(dir);
			}
		}
	}
	
	protected void runToSafetyOrAttack(Direction attackDirection) throws GameActionException {
		if (rc.isCoreReady()) {
			Direction dir = getMoveDir(attackDirection.opposite());
			if (dir != null) {
				rc.move(dir);
			} else {
				attackLeastHealtyEnemyInRange();
			}
		}
	}

}

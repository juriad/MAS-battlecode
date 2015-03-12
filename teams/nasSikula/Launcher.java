package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Launcher extends AttackingBot {

	public Launcher(RobotController rc) {
		super(rc);
	}

	@Override
	protected void attack() throws GameActionException {
		// if (rc.senseNearbyRobots(rt.attackRadiusSquared, theirTeam));
		MapLocation tower = getNearestTower(rc, myTeam);
		if (rc.getLocation().distanceSquaredTo(tower) > 3) {
			moveTowards(tower);
		}
		target = getAttackDirection();
		Direction direction = rc.getLocation().directionTo(target);
		if (rc.canLaunch(direction))
			if (rc.getMissileCount() > 0)
				rc.launchMissile(direction);
	}

}

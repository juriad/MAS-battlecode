package nasSikula;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class AttackingBot extends MovingBot {

	public AttackingBot(RobotController rc) {
		super(rc);

	}

	public void execute() throws GameActionException {
		
		MapLocation target = null;
		RobotType rt = rc.getType();
		
		switch (rt){
			case BASHER:
				//TODO pri attacku musi byt na 2 kroky blizko (na konci tahu) od mista na ktere utoci
				//soucasna strategie neutoci...
				break;
			case LAUNCHER:
				//if (rc.senseNearbyRobots(rt.attackRadiusSquared, theirTeam));
				MapLocation tower = getNearestTower(rc, myTeam);
				if (rc.getLocation().distanceSquaredTo(tower) > 3){
					moveTowards(tower, false);
				}
				Direction dir = getAttackDirection();
				if(rc.canLaunch(dir))
					if (rc.getMissileCount() > 0)
						rc.launchMissile(dir);
				
			default:
				attackLeastHealtyEnemyInRange();
				break;
		}
		


		if (target == null) {
			Direction initialMoveDirection = getInitialMoveDirection();
			if (initialMoveDirection != Direction.NONE && rc.isCoreReady()) {
				rc.move(initialMoveDirection);
			} else {
				markDeadEnd();
			}

		}

		transferSupplies();
		rc.yield();
	}


}

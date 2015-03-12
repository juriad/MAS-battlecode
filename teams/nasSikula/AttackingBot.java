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
		
		RobotType rt = rc.getType();
		MapLocation target = null;
		switch (rt){
			case BASHER:
				//MapLocation loc = getNearestEnemy(rc);
				//pri attacku musi byt na 2 kroky blizko (na konci tahu) od mista na ktere utoci
				target = getAttackDirection();
				if (target == null)
				{
					target = getNearestTower(rc, theirTeam);
				}
				if (rc.isCoreReady())
					//rc.move(dir);
					moveTowards(target, false);
				//TODO ulozit si ID toho po kom jdu - a nasleduj ho i dalsi tahy


				break;
			case LAUNCHER:
				//if (rc.senseNearbyRobots(rt.attackRadiusSquared, theirTeam));
				MapLocation tower = getRandomTower(rc, myTeam);
				if (rc.getLocation().distanceSquaredTo(tower) > 3){
					moveTowards(tower, false);
				}
				target = getAttackDirection();
				if (target != null){ 
					Direction direction = rc.getLocation().directionTo(target);
					if(rc.canLaunch(direction))
						if (rc.getMissileCount() > 0)
							rc.launchMissile(direction);
				}
			case MISSILE:
				break;
				//rc.explode();
				
			default:
				attackLeastHealtyValuedEnemyInRange();
				break;
		}
		


		if (target == null) {
			Direction initialMoveDirection = getInitialMoveDirection();
			if (initialMoveDirection != Direction.NONE && rc.isCoreReady()) {
				moveTowards(
						rc.getLocation().add(initialMoveDirection),
						//theirHQ,
						false);
				
			} else {
				markDeadEnd();
			}

		}

		transferSupplies();
		rc.yield();
	}


}

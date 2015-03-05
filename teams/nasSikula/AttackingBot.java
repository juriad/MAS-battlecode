package nasSikula;

import nasSikula.context.Registry;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.TerrainTile;

public abstract class AttackingBot extends MovingBot {

	public AttackingBot(RobotController rc) {
		super(rc);
		
	}

	public void execute() throws GameActionException {
		Direction initialMoveDirection = getInitialMoveDirection();
		if (initialMoveDirection != Direction.NONE && rc.isCoreReady()) {
			rc.move(initialMoveDirection);
		} else {
			markDeadEnd();
		}
		
		RobotInfo[] enemies = getEnemiesInAttackingRange();

		if (enemies.length > 0) {
			// attack!
			if (rc.isWeaponReady()) {
				attackLeastHealthEnemy(enemies);
			}
		} 

		transferSupplies();
		rc.yield();
	}
	
	public void attackLeastHealthEnemy(RobotInfo[] enemies)
			throws GameActionException {
		if (enemies.length == 0) {
			return;
		}

		double minEnergon = Double.MAX_VALUE;
		MapLocation toAttack = null;
		for (RobotInfo info : enemies) {
			if (info.health < minEnergon) {
				toAttack = info.location;
				minEnergon = info.health;
			}
		}

		rc.attackLocation(toAttack);
	}
}

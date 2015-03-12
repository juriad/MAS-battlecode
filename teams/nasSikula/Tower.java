package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class Tower extends BaseBot {
	public Tower(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
		defend();
	}

	/**
	 * @return true if defended itself
	 * @throws GameActionException
	 */
	protected boolean defend() throws GameActionException {
		RobotInfo[] enemiesInAttackingRange = getEnemiesInAttackingRange();
		if (enemiesInAttackingRange.length > 0 && rc.isWeaponReady()) {
			attackLeastHealthValuedEnemy(enemiesInAttackingRange);
			// TODO return false???? nemuzu utocit, pomuze mi vyrobit beavra?
			// nebo to stoji cas? (jedna se o HQ....)
			return true;
		}
		return false;
	}
}
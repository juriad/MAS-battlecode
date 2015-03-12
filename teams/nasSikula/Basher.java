package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Basher extends AttackingBot {

	public Basher(RobotController rc) {
		super(rc);
	}

	@Override
	protected void attack() throws GameActionException {
		// MapLocation loc = getNearestEnemy(rc);
		// pri attacku musi byt na 2 kroky blizko (na konci tahu) od mista na
		// ktere utoci
		target = getAttackDirection();
		if (target == null) {
			target = getNearestTower(rc, theirTeam);
		}
		if (rc.isCoreReady())
			// rc.move(dir);
			moveTowards(target);
		// TODO ulozit si ID toho po kom jdu - a nasleduj ho i dalsi tahy
	}
}

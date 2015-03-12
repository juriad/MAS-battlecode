package nasSikula;

import nasSikula.context.Registry;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class AttackingBot extends MovingBot {

	public AttackingBot(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
		if (!initialMove()) {
			attack();
		}

		if (!Registry.CAPTAIN.amICaptain()) {
			followCaptain(Registry.CAPTAIN.getCaptain());
		} else {
			Registry.CAPTAIN_COUNTER.increase();
			lead();
		}

		markDeadEnd();
	}

	private void lead() throws GameActionException {
		moveTowards(getTarget());
	}
	private void followCaptain(RobotInfo captain) throws GameActionException {
		moveTowards(captain.location);
	}

	protected void attack() throws GameActionException {
		attackLeastHealtyValuedEnemyInRange();
	}

	@Override
	protected boolean isMiner() {
		return false;
	}

	private MapLocation getTarget() {
		if (target == null) {
			target = getRandomTowerOrHQ(rc, theirTeam);
			return target;
		} else {
			if (rc.senseEnemyHQLocation().equals(target)) {
				return target;
			} else {
				MapLocation[] towerLocations = rc.senseEnemyTowerLocations();
				for (MapLocation ml : towerLocations) {
					if (ml.equals(target)) {
						return target;
					}
				}
				target = getRandomTowerOrHQ(rc, theirTeam);
				return target;
			}
		}

	}

}

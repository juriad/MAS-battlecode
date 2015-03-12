package nasSikula;

import nasSikula.context.Registry;
import battlecode.common.GameActionException;
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
			lead();
		}

		markDeadEnd();
	}

	private void lead() throws GameActionException {
		moveTowards(getRandomTower(rc, theirTeam));
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

}

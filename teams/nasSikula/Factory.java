package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Factory extends BaseBot {

	public Factory(RobotController rc) {
		super(rc);
	}

	@Override
	public void execute() throws GameActionException {
		spawnOrBuild();
		transferSupplies();
		rc.yield();
	}

}

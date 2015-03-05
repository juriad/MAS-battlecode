package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Tank extends MovingBot {

	public Tank(RobotController rc) {
		super(rc);
	}

	@Override
	public void execute() throws GameActionException {
		transferSupplies();
		rc.yield();
	}

}

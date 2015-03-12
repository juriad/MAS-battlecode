package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class NoOpBot extends BaseBot {
	public NoOpBot(RobotController rc) {
		super(rc);
	}

	public void execute() throws GameActionException {
	}
}
package nasSikula;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class ProductionBuilding extends BaseBot {

	public ProductionBuilding(RobotController rc) {
		super(rc);
	}

	@Override
	public void execute() throws GameActionException {
		spawnOrBuild();
		transferSupplies();
		rc.yield();
	}

}

package nasSikula;

import battlecode.common.RobotController;

public class Basher extends AttackingBot {

	public Basher(RobotController rc) {
		super(rc);
	}

	private static double ORE_EPSILON = 4;
	
	@Override
	protected double get_ore_epsilon(){
		return ORE_EPSILON;
	}
}

package nasSikula.context;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Registry {
	public static RobotCount ROBOT_COUNT = null;
	public static Map MAP = null;
	public static Captain CAPTAIN = null;

	public static void init(RobotController rc) {
		ROBOT_COUNT = new RobotCount(rc, 0);
		MAP = new Map(rc, ROBOT_COUNT.getSize() + ROBOT_COUNT.getOffset());
		CAPTAIN = new Captain(rc, MAP.getSize() + MAP.getOffset()) {
			@Override
			protected int getCount() {
				return Registry.ROBOT_COUNT.getCount(RobotType.SOLDIER);
			}
		};
	}
}

package nasSikula.context;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class RobotCount extends RegistryClass {
	public RobotCount(RobotController rc, int offset) {
		super(rc, offset);
	}

	public void setCount(RobotType type, int count) {
		write(type.ordinal(), count);
	}

	public int getCount(RobotType type) {
		return read(type.ordinal());
	}

	@Override
	public int getSize() {
		return RobotType.values().length;
	}
}
package nasSikula.context;

import battlecode.common.RobotController;

public class Counter extends RegistryClass {

	public Counter(RobotController rc, int offset) {
		super(rc, offset);
	}

	@Override
	int getSize() {
		return 1;
	}

	public void increase() {
		int read = read(0);
		write(0, read + 1);
	}

	public int get() {
		return read(0);
	}

	public void reset() {
		write(0, 0);
	}

}

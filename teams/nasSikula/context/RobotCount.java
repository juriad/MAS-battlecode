package nasSikula.context;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class RobotCount extends RegistryClass {
	private int sum;

	public RobotCount(RobotController rc, int offset) {
		super(rc, offset);
	}

	/**
	 * Callable only by HQ
	 * 
	 * @param type
	 * @param count
	 */
	public void setCount(RobotType type, int count) {
		write(type.ordinal(), count);

		sum += count;
		write(RobotType.values().length, sum);
	}

	public void increase(RobotType type, int inc) {
		write(type.ordinal(), getCount(type) + inc);

		sum += inc;
		write(RobotType.values().length, sum);
	}

	/**
	 * Callable by HQ
	 */
	public void reset() {
		sum = 0;
	}

	public int getCount(RobotType type) {
		return read(type.ordinal());
	}

	@Override
	public int getSize() {
		return RobotType.values().length + 1;
	}

	public int getSum() {
		return read(RobotType.values().length);
	}
}
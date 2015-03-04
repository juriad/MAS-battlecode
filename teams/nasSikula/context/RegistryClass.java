package nasSikula.context;

import battlecode.common.RobotController;

public abstract class RegistryClass {
	protected RobotController rc;
	private final int offset;

	public RegistryClass(RobotController rc, int offset) {
		this.rc = rc;
		this.offset = offset;
	}

	abstract int getSize();

	public void write(int where, int what) {
		try {
			rc.broadcast(where + getOffset(), what);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int read(int where) {
		try {
			return rc.readBroadcast(where + getOffset());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getOffset() {
		return offset;
	}
}
package nasSikula.context;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public abstract class Captain extends RegistryClass {

	protected abstract int getCount();

	public Captain(RobotController rc, int offset) {
		super(rc, offset);
	}

	@Override
	int getSize() {
		return 400; // to be sure :-)
	}

	private int group = -1;

	public boolean amICaptain() {
		return getCaptain().ID == rc.getID();
	}

	public int getGroup() {
		if (group == -1) {
			group = (int) (rc.getID() % Math.sqrt(getCount()) / 3);
		}
		return group;
	}

	public RobotInfo getCaptain() {
		int capId = read(getGroup() * 2);
		try {
			return rc.senseRobot(capId);
		} catch (Exception e) {
			RobotInfo me = null;
			try {
				me = rc.senseRobot(rc.getID());
			} catch (GameActionException e1) {
				// won't happen; that's me
			}
			write(getGroup() * 2, rc.getID());
			return me;
		}
	}

	public void writeInfo(int info) {
		if (amICaptain()) {
			write(getGroup() * 2 + 1, info);
		}
	}

	public int readInfo() {
		return read(getGroup() * 2 + 1);
	}
}
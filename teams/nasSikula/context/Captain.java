package nasSikula.context;

import battlecode.common.RobotController;

public abstract class Captain extends RegistryClass {

	protected abstract int getCount();

	public Captain(RobotController rc, int offset) {
		super(rc, offset);
	}

	@Override
	int getSize() {
		return 200; // to be sure :-)
	}

	private boolean captain = false;

	public boolean amICaptain() {
		return captain;
	}
/*
	public int readMessageFromCaptain() {

	}

	public void writeMessageAsCaptain() {
		if (!amICaptain()) {
			return;
		}
	}

	public int getGroupSize() {
	}

	public int getCaptainsNumber() {
	}

	public int resign() {
		
	}
	*/
}
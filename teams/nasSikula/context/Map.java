package nasSikula.context;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Map extends RegistryClass {

	private final int tlx;
	private final int tly;

	public Map(RobotController rc, int offset) {
		super(rc, offset);

		MapLocation hq = rc.senseHQLocation();
		tlx = hq.x - 122;
		tly = hq.y - 122;
	}

	@Override
	public int getSize() {
		// a little bigger to be sure
		// I am lazy to count the ones
		return 244 * 244;
	}

	private int get(MapLocation ml) {
		return read((ml.y - tly) * 244 + ml.x - tlx);
	}

	private void set(MapLocation ml, int val) {
		write((ml.y - tly) * 244 + ml.x - tlx, val);
	}

	public void setAttackUnitDirection(MapLocation ml, Direction d) {
		int i = get(ml);
		i &= ~(255 << 8);
		i |= (d.ordinal() + 1) << 8;
		set(ml, i);
	}

	public Direction getAttackUnitDirection(MapLocation ml) {
		int i = get(ml);
		int j = (i & (255 << 8)) >>> 8;
		if (j == 0) {
			return Direction.NONE;
		} else {
			return Direction.values()[j - 1];
		}
	}

	public void setMinerUnitDirection(MapLocation ml, Direction d) {
		int i = get(ml);
		i &= ~(255 << 16);
		i |= (d.ordinal() + 1) << 16;
		set(ml, i);
	}

	public Direction getMinerUnitDirection(MapLocation ml) {
		int i = get(ml);
		int j = (i & (255 << 16)) >>> 16;
		if (j == 0) {
			return Direction.NONE;
		} else {
			return Direction.values()[j - 1];
		}
	}

	public void setAttackerDeadEnd(MapLocation ml) {
		int i = get(ml);
		i |= 0b000_00001;
		set(ml, i);
	}

	public boolean isAttackerDeadEnd(MapLocation ml) {
		int i = get(ml);
		return (i & 0b000_00001) == 1;
	}

	public void setMiningDeadEnd(MapLocation ml) {
		int i = get(ml);
		i |= 0b0000_0010;
		set(ml, i);
	}

	public boolean isMiningDeadEnd(MapLocation ml) {
		int i = get(ml);
		return (i & 0b000_00010) == 1;
	}

	public void setDirection(MapLocation ml, boolean miner, Direction d) {
		if (miner) {
			setMinerUnitDirection(ml, d);
		} else {
			setAttackUnitDirection(ml, d);
		}
	}

	public Direction getDirection(MapLocation ml, boolean miner) {
		if (miner) {
			return getMinerUnitDirection(ml);
		} else {
			return getAttackUnitDirection(ml);
		}
	}

	public void setDeadEnd(MapLocation ml, boolean miner) {
		if (miner) {
			setMiningDeadEnd(ml);
		} else {
			setAttackerDeadEnd(ml);
		}
	}

	public boolean isDeadEnd(MapLocation ml, boolean miner) {
		if (miner) {
			return isMiningDeadEnd(ml);
		} else {
			return isAttackerDeadEnd(ml);
		}
	}
}
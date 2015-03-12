package nasSikula.context;

import battlecode.common.RobotType;

public class BuildBuilding implements Comparable<BuildBuilding> {
	int howMany = 0;
	RobotType type = null;

	public BuildBuilding(int howMany, RobotType type) {
		this.howMany = howMany;
		this.type = type;
	}

	@Override
	public int compareTo(BuildBuilding o) {
		return new Integer(howMany).compareTo(o.howMany);
	}

}

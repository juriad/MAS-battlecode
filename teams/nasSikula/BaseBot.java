package nasSikula;

import java.util.Random;

import nasSikula.context.Objectives;
import nasSikula.context.Registry;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public abstract class BaseBot {
	protected RobotController rc;
	protected MapLocation myHQ, theirHQ;
	protected Team myTeam, theirTeam;
	protected RobotType type;
	protected Random rand;

	public BaseBot(RobotController rc) {
		this.rc = rc;
		this.type = rc.getType();
		this.rand = new Random(rc.getID());
		this.myHQ = rc.senseHQLocation();
		this.theirHQ = rc.senseEnemyHQLocation();
		this.myTeam = rc.getTeam();
		this.theirTeam = this.myTeam.opponent();
	}

	public Direction[] getDirectionsToward(Direction toDest) {
		Direction[] dirs = { toDest, toDest.rotateLeft(), toDest.rotateRight(),
				toDest.rotateLeft().rotateLeft(),
				toDest.rotateRight().rotateRight() };

		return dirs;
	}

	public Direction getMoveDir(Direction dir) {
		Direction[] dirs = getDirectionsToward(dir);
		for (Direction d : dirs) {
			if (rc.canMove(d)) {
				return d;
			}
		}
		return null;
	}

	public Direction getMoveDir(MapLocation dest) {
		return getMoveDir(rc.getLocation().directionTo(dest));
	}

	public Direction getSpawnDirection(RobotType type) {//TODO na kazdou stranu muzou vylezt - tohle asi ze zacatku zdrzuje
		Direction dir = rc.getLocation().directionTo(theirHQ);
		Direction[] dirs = getDirectionsToward(dir);
		for (Direction d : dirs) {
			if (rc.canSpawn(d, type)) {
				return d;
			}
		}
		dir = dir.opposite();
		dirs = getDirectionsToward(dir);
		for (Direction d : dirs) {
			if (rc.canSpawn(d, type)) {
				return d;
			}
		}
		return null;
	}

	public Direction getBuildDirection(RobotType type) {
		Direction dir = rc.getLocation().directionTo(theirHQ);
		Direction[] dirs = getDirectionsToward(dir);
		for (Direction d : dirs) {
			if (rc.canBuild(d, type)) {
				return d;
			}
		}
		return null;
	}

	public RobotInfo[] getAllies() {
		RobotInfo[] allies = rc.senseNearbyRobots(Integer.MAX_VALUE, myTeam);
		return allies;
	}

	public RobotInfo[] getEnemiesInAttackingRange() {
		RobotInfo[] enemies = rc.senseNearbyRobots(type.attackRadiusSquared,
				theirTeam);
		return enemies;
	}
	
	public RobotInfo[] getEnemiesInTheirPossibleAttackingRange() {
		RobotInfo[] enemies = rc.senseNearbyRobots(15,
				theirTeam);
		return enemies;
	}

	public MapLocation getLeastHealthEnemy(RobotInfo[] enemies)
			throws GameActionException {
		if (enemies.length == 0) {
			return null;
		}

		double minEnergon = Double.MAX_VALUE;
		MapLocation toAttack = null;
		for (RobotInfo info : enemies) {
			if (info.health < minEnergon) {
				toAttack = info.location;
				minEnergon = info.health;
			}
		}
		return toAttack;

	}

	public MapLocation attackLeastHealthEnemy(RobotInfo[] enemies)
			throws GameActionException {
		MapLocation toAttack = getLeastHealthEnemy(enemies);
		if (toAttack != null)
			rc.attackLocation(toAttack);
		return toAttack;
	}

	/**
	 * @return true if spawned or built
	 * @throws GameActionException
	 */
	protected boolean spawnOrBuild() throws GameActionException {
		if (rc.isCoreReady()) {
			RobotType spawnOrBuild = Objectives.OBJECTIVES.spawnOrBuild(type);
			if (spawnOrBuild != null) {

				if (!spawnOrBuild.isBuilding) {
					Direction direction = getSpawnDirection(spawnOrBuild);
					if (direction != null) {
						rc.spawn(direction, spawnOrBuild);
						
						Registry.ROBOT_COUNT.increase(spawnOrBuild, 1);
						return true;
					}
				} else if (canBuildHere()) {
					Direction direction = getBuildDirection(spawnOrBuild);
					if (direction != null) {
						rc.build(direction, spawnOrBuild);
						
						Registry.ROBOT_COUNT.increase(spawnOrBuild, 1);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean canBuildHere() {
		RobotInfo[] nearby = rc.senseNearbyRobots(4);
		int buildings = 0;
		for (RobotInfo ri : nearby) {
			if (ri.type.isBuilding) {
				buildings++;
			}
		}
		return buildings == 0;
	}

	/**
	 * @return location of core of a nearby attack or null
	 */
	protected MapLocation getAttackDirection() {
		RobotInfo[] enemies = rc.senseNearbyRobots(
				RobotType.TANK.attackRadiusSquared, theirTeam);
		if (enemies.length == 0) {
			return null;
		}

		double x = rc.getLocation().x;
		double y = rc.getLocation().y;

		for (RobotInfo ri : enemies) {
			x += (ri.location.x - rc.getLocation().x) * ri.type.attackPower;
			y += (ri.location.y - rc.getLocation().y) * ri.type.attackPower;
		}
		return new MapLocation((int) x, (int) y);
	}

	protected void transferSupplies() throws GameActionException {
		RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getLocation(),
				GameConstants.SUPPLY_TRANSFER_RADIUS_SQUARED, rc.getTeam());
		double lowestSupply = rc.getSupplyLevel();
		double transferAmount = 0;
		MapLocation suppliesToThisLocation = null;
		for (RobotInfo ri : nearbyAllies) {
			if (ri.supplyLevel < lowestSupply) {
				lowestSupply = ri.supplyLevel;
				transferAmount = (rc.getSupplyLevel() - ri.supplyLevel) / 2;
				suppliesToThisLocation = ri.location;
			}
		}
		if (suppliesToThisLocation != null) {
			rc.transferSupplies((int) transferAmount, suppliesToThisLocation);
		}
	}

	protected Direction getRandomDirection() {
		return Direction.values()[rand.nextInt(8)];
	}

	public abstract void execute() throws GameActionException;
}
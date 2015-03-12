package nasSikula;

import nasSikula.context.Registry;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import battlecode.common.TerrainTile;

public abstract class MovingBot extends BaseBot {
	// ORE s 4 ma uz se tezi stejne pomalu jako s 0.0001 (beaver) a 0.8 pro
	// minera

	protected static final double DEAD_ORE_EPSILON = 1;
	protected static final double ORE_EPSILON = 0.8;

	private static final int SINCE_SPAWN_RUN_AWAY = 8;
	private int sinceSpawn = 0;
	private MapLocation initialTargetLocation = null;

	public MovingBot(RobotController rc) {
		super(rc);
	}

	/**
	 * must be called exactly once a round
	 * 
	 * @return if initial move was applied
	 * @throws GameActionException
	 */
	protected boolean initialMove() throws GameActionException {
		sinceSpawn++;
		if (sinceSpawn < SINCE_SPAWN_RUN_AWAY) {
			if (initialTargetLocation == null) {
				initialTargetLocation = rc.getLocation().add(
						rand.nextInt(SINCE_SPAWN_RUN_AWAY * 4)
								- SINCE_SPAWN_RUN_AWAY * 2,
						rand.nextInt(SINCE_SPAWN_RUN_AWAY * 4)
								- SINCE_SPAWN_RUN_AWAY * 2);
			}
			return moveTowards(initialTargetLocation);
		}
		return false;
	}

	protected MapLocation target = null;

	protected abstract boolean isMiner();

	protected void markDeadEnd() {
		MapLocation loc = rc.getLocation();
		boolean[] freeDirs = new boolean[8];

		int free = 0;
		int miningFree = 0;
		for (Direction d : Direction.values()) {
			MapLocation ml = loc.add(d);
			if (ml.equals(loc)) {
				continue;
			}

			if (rc.senseTerrainTile(ml) == TerrainTile.NORMAL
					&& ml.isAdjacentTo(loc)) {
				if (!Registry.MAP.isAttackerDeadEnd(ml)) {
					free++;
					freeDirs[d.ordinal()] = true;
				}
				if (!Registry.MAP.isMiningDeadEnd(ml)) {
					miningFree++;
				}
			}
		}

		if (miningFree <= 1) {
			Registry.MAP.setMiningDeadEnd(loc);
		}

		if (free == 1) {
			Registry.MAP.setAttackerDeadEnd(loc);
		} else {
			for (int i = 0; i < 8; i += 2) {
				if (!freeDirs[i] && !freeDirs[(i + 1) % 8]
						&& !freeDirs[(i + 2) % 8] && freeDirs[(i + 4) % 8]
						&& freeDirs[(i + 6) % 8]) {
					if (!Registry.MAP.isAttackerDeadEnd(loc)) {
						Registry.MAP.setAttackerDeadEnd(loc);
						Registry.COUNTER.increase();
						System.out.println(Registry.COUNTER.get()
								+ ": Marking " + loc + "as dead end");
					}
					break;
				}
			}
		}
	}

	protected Direction[] getFreeDirections(boolean mining) {
		MapLocation loc = rc.getLocation();
		Direction[] dirs = new Direction[8];
		int index = 0;

		for (Direction d : Direction.values()) {
			MapLocation ml = loc.add(d);
			if (ml.equals(loc)) {
				continue;
			}
			if (rc.senseTerrainTile(loc) == TerrainTile.NORMAL
					&& !Registry.MAP.isDeadEnd(ml, isMiner())) {
				dirs[index++] = d;
			}
		}

		Direction[] out = new Direction[index];
		System.arraycopy(dirs, 0, out, 0, index);
		return out;
	}

	/**
	 * 
	 * @return location of our target if there is one...
	 * @throws GameActionException
	 */
	protected MapLocation attackLeastHealtyValuedEnemyInRange()
			throws GameActionException {

		RobotInfo[] enemies = getEnemiesInAttackingRange();
		if (enemies.length > 0) {
			// attack!
			if (rc.isWeaponReady()) {
				MapLocation target = getLeastHealthValuedTarget(enemies);

				rc.attackLocation(target);

				return target;
			}

		}
		return null;
	}

	protected MapLocation attackHQOrTowerOrLeastHealtyEnemyInRange()// todo
			throws GameActionException {

		RobotInfo[] enemies = getEnemiesInAttackingRange();
		if (enemies.length > 0) {
			// attack!
			MapLocation target = getLeastHealthValuedTarget(enemies);

			if (rc.isWeaponReady()) {
				rc.attackLocation(target);
			}
			return target;
		}
		return null;
	}

	/**
	 * @param ml
	 * @param miner
	 * @return true if moved
	 * @throws GameActionException
	 */
	protected boolean moveTowards(MapLocation ml) throws GameActionException {
		if (!rc.isCoreReady()) {
			return false;
		}

		Direction[] freeDirections = getFreeDirections(isMiner());
		Direction directionTo = rc.getLocation().directionTo(ml);
		int d = directionTo.ordinal();

		Direction opt = null;
		int delta = 100;
		for (Direction dir : freeDirections) {
			if (rc.canMove(dir)
					&& dir.opposite() != Registry.MAP.getDirection(ml,
							isMiner())) { // do not go back
				int del = Math.min(
						Math.min(Math.abs(dir.ordinal() - d),
								Math.abs(dir.ordinal() - d - 8)),
						Math.abs(dir.ordinal() - d + 8));
				if (del < delta) {
					delta = del;
					opt = dir;
				}
			}
		}
		if (opt == null) {
			for (Direction dir : Direction.values()) {
				if (rc.canMove(dir)) {
					int del = Math.min(
							Math.min(Math.abs(dir.ordinal() - d),
									Math.abs(dir.ordinal() - d - 8)),
							Math.abs(dir.ordinal() - d + 8));
					if (del < delta) {
						delta = del;
						opt = dir;
					}
				}
			}
		}
		if (opt != null) {
			rc.move(opt);
			MapLocation dest = rc.getLocation().add(opt);
			Registry.MAP.setDirection(dest, isMiner(), opt);
			return true;
		}
		return false;
	}

	protected MapLocation getNearestTower(RobotController rc, Team t) {
		MapLocation[] towers = (t == myTeam) ? rc.senseTowerLocations() : rc
				.senseEnemyTowerLocations();
		int minSize = Integer.MAX_VALUE;
		MapLocation minLoc = null;
		MapLocation myLoc = rc.getLocation();
		for (MapLocation tower : towers) {
			int dist = myLoc.distanceSquaredTo(tower);
			if (dist < minSize) {
				minSize = dist;
				minLoc = tower;
			}
		}
		return minLoc;
	}
	
	protected MapLocation getNearestTowerOrHQ(RobotController rc, Team t) {
		MapLocation myLoc = rc.getLocation();
		MapLocation Tower = getNearestTower(rc, t);
		MapLocation HQ = (t == myTeam) ? rc.senseHQLocation(): rc.
				senseEnemyHQLocation();
		if (Tower == null)
			return HQ;
					
		int distToHQ = myLoc.distanceSquaredTo(HQ);
		int distToTower = myLoc.distanceSquaredTo(Tower);
		return distToHQ < distToTower ? HQ :Tower;
	}

	protected MapLocation getRandomTowerOrHQ(RobotController rc, Team t) {
		MapLocation[] towers = (t == myTeam) ? rc.senseTowerLocations() : rc
				.senseEnemyTowerLocations();
		
		int index = rand.nextInt(towers.length + 1);
		if (towers.length == index){
			return (t == myTeam) ? rc.senseHQLocation(): rc.senseEnemyHQLocation();
		}
		return towers[index];
	}

	protected MapLocation getNearestEnemy(RobotController rc) {
		RobotInfo[] enemies = rc.senseNearbyRobots(
				rc.getType().attackRadiusSquared, theirTeam);
		int minSize = Integer.MAX_VALUE;
		MapLocation minLoc = null;
		MapLocation I = rc.getLocation();
		for (RobotInfo enemy : enemies) {
			MapLocation loc = enemy.location;
			int dist = I.distanceSquaredTo(loc);
			if (dist < minSize) {
				minSize = dist;
				minLoc = loc;
			}
		}
		return minLoc;
	}
}

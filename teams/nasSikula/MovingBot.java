package nasSikula;

import java.util.Random;

import nasSikula.context.Registry;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import battlecode.common.TerrainTile;

public abstract class MovingBot extends BaseBot {
	// ORE s 4 ma uz se tezi stejne pomalu jako s 0.0001 (beaver) a 0.8 pro minera
	
	protected static final double DEAD_ORE_EPSILON = 0.4;
	protected static final double ORE_EPSILON = 0.8;

	private static final int SINCE_SPAWN_RUN_AWAY = 8;
	private int sinceSpawn = 0;
	private Direction sinceSpawnDirection = null;

	public MovingBot(RobotController rc) {
		super(rc);
	}

	/**
	 * must be called exactly once a round
	 * 
	 * @return direction to go or NONE
	 * @throws GameActionException
	 */
	protected Direction getInitialMoveDirection() throws GameActionException {
		if (sinceSpawn < SINCE_SPAWN_RUN_AWAY) {
			if (sinceSpawnDirection == null) {
				sinceSpawnDirection = getRandomDirection();
			}
			Direction moveDir = getMoveDir(sinceSpawnDirection);
			if (moveDir == null) {
				moveDir = getMoveDir(sinceSpawnDirection.opposite());
			}
			if (moveDir != null) {
				MapLocation location = rc.getLocation().add(moveDir, 10)
						.add(getRandomDirection(), rand.nextInt(13));
				Direction directionTo = rc.getLocation().directionTo(location);
				Direction moveDir2 = getMoveDir(directionTo);
				if (moveDir2 == Direction.OMNI) {
					moveDir2 = getRandomDirection();
				}
				if (moveDir2 != null) {
					sinceSpawnDirection = moveDir2;
					return sinceSpawnDirection;
				}
			}
		}
		sinceSpawn++;
		return Direction.NONE;
	}

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

		if (miningFree <= 1 && rc.senseOre(loc) < DEAD_ORE_EPSILON) {
			Registry.MAP.setMiningDeadEnd(loc);
		}

		if (free == 1) {
			Registry.MAP.setAttackerDeadEnd(loc);
		} else {
			for (int i = 0; i < 8; i += 2) {
				if (!freeDirs[i] && !freeDirs[(i + 1) % 8]
						&& !freeDirs[(i + 2) % 8] && freeDirs[(i + 4) % 8]
						&& freeDirs[(i + 6) % 8]) {
					Registry.MAP.setAttackerDeadEnd(loc);
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
					&& !(mining ? Registry.MAP.isMiningDeadEnd(ml)
							: Registry.MAP.isAttackerDeadEnd(ml))) {
				dirs[index] = d;
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
	
	protected MapLocation attackHQOrTowerOrLeastHealtyEnemyInRange()//todo
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
	protected boolean moveTowards(MapLocation ml, boolean miner)
			throws GameActionException {
		//System.out.println("in moveTowards");
		if (!rc.isCoreReady()) {
			//System.out.println("core not ready");
			return false;
		}

		//System.out.println("core ready");
		Direction[] freeDirections = getFreeDirections(miner);
		Direction directionTo = rc.getLocation().directionTo(ml);

		int d = directionTo.ordinal();
		Direction opt = null;
		int delta = 100;
		for (Direction dir : freeDirections) {
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
		if (opt != null && rc.isCoreReady()) {
			//System.out.println("move!!!");
			rc.move(opt);
			return true;
		}
		return false;
	}

	protected double get_ore_epsilon() {
		return 0;
	}
	
	protected MapLocation getNearestTower(RobotController rc, Team t) {
		MapLocation[] towers = (t == myTeam) ? rc.senseTowerLocations():rc.senseEnemyTowerLocations();
		int minSize = Integer.MAX_VALUE;
		MapLocation minLoc = null;
		MapLocation I = rc.getLocation();
		for (MapLocation tower : towers) {
			int dist = I.distanceSquaredTo(tower);
			if(dist < minSize)
			{
				minSize = dist;
				minLoc = tower;
			}
		}
		return minLoc;
	}
	
	protected MapLocation getRandomTower(RobotController rc, Team t) {
		MapLocation[] towers = (t == myTeam) ? rc.senseTowerLocations():rc.senseEnemyTowerLocations();
		int index = new Random().nextInt(towers.length);
		return towers[index];
	}
	
	protected MapLocation getNearestEnemy(RobotController rc) {
		RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, theirTeam);
		int minSize = Integer.MAX_VALUE;
		MapLocation minLoc = null;
		MapLocation I = rc.getLocation();
		for (RobotInfo enemy : enemies) {
			MapLocation loc = enemy.location;
			int dist = I.distanceSquaredTo(loc);
			if(dist < minSize)
			{
				minSize = dist;
				minLoc = loc;
			}
		}
		return minLoc;
	}
}

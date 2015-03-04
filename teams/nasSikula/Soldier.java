package nasSikula;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;


public class Soldier extends BaseBot {
	Random rand; 
    public Soldier(RobotController rc) {
        
    	super(rc);
    	rand = new Random(rc.getID());
    }
    private int xMoveFromStart = 5;

    public void execute() throws GameActionException {
    	if(xMoveFromStart < 0){//ze zacatku popobehni
    		xMoveFromStart--;
    		MapLocation theirHQ = rc.senseEnemyHQLocation();
    		Direction toDest = rc.getLocation().directionTo(theirHQ);
    		if (toDest == null){
    			toDest = Direction.OMNI;
    		}
    		if (toDest == Direction.OMNI){
    			rand.nextInt(8);//TODO random smer
    			toDest = Direction.SOUTH_EAST;
    		}
    		
    		rc.move(toDest);
    		}
    	
        transferSupplies();
        rc.yield();
    }
}
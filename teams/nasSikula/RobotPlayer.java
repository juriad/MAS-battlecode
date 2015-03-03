package nasSikula;

import battlecode.common.RobotController;

public class RobotPlayer {

    public static BaseBot convert(RobotController rc) {
        switch (rc.getType()) {
        case HQ:
            return new HQ(rc);
        case TOWER:
            return new Tower(rc);
        case BEAVER:
            return new Beaver(rc);

        case MINERFACTORY:
            return new MineFactory(rc);
        case MINER:
            return new Miner(rc);

        case BARRACKS:
            return new Barracks(rc);
        case SOLDIER:
            return new Soldier(rc);
        case BASHER:
            return new Basher(rc);

        case AEROSPACELAB:
            break;
        case COMMANDER:
            break;
        case COMPUTER:
            break;
        case DRONE:
            break;
        case HANDWASHSTATION:
            break;
        case HELIPAD:
            break;
        case LAUNCHER:
            break;
        case MISSILE:
            break;
        case SUPPLYDEPOT:
            break;
        case TANK:
            break;
        case TANKFACTORY:
            break;
        case TECHNOLOGYINSTITUTE:
            break;
        case TRAININGFIELD:
            break;
        default:
            // won't happen
            break;
        }
        return null;
    }

    public static void run(RobotController rc) throws Exception {
        Registry.init(rc);
        BaseBot myself = convert(rc);
        if (myself == null) {
            return;
        }

        while (true) {
            try {
                myself.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

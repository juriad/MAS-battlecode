package nasSikula;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Registry {
    public static RobotCount ROBOT_COUNT = null;

    public static void init(RobotController rc) {
        ROBOT_COUNT = new RobotCount(rc, 0);
    }

    public static abstract class RegistryClass {
        protected RobotController rc;
        private int offset;

        public RegistryClass(RobotController rc, int offset) {
            this.rc = rc;
            this.offset = offset;
        }

        abstract int getSize();

        public void write(int where, int what) {
            try {
                rc.broadcast(where + offset, what);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public int read(int where) {
            try {
                return rc.readBroadcast(where + offset);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public static class RobotCount extends RegistryClass {
        public RobotCount(RobotController rc, int offset) {
            super(rc, offset);
        }

        public void setCount(RobotType type, int count) {
            write(type.ordinal(), count);
        }

        public int getCount(RobotType type) {
            return read(type.ordinal());
        }

        @Override
        public int getSize() {
            return RobotType.values().length;
        }
    }
}

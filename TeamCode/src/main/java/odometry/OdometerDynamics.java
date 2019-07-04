package odometry;

import math.Vector3;

public class OdometerDynamics {
    private final Vector3 dynamicRobotIncrements;
    public OdometerDynamics(Vector3 dynamicRobotIncrements){
        this.dynamicRobotIncrements = dynamicRobotIncrements;
    }

    public Vector3 getDynamicRobotIncrements() {
        return dynamicRobotIncrements;
    }
}
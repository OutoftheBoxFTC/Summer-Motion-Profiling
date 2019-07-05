package odometry;

import math.Vector3;

public class SimpleOdometerDynamics {
    private final Vector3 dynamicRobotIncrements;
    public SimpleOdometerDynamics(Vector3 dynamicRobotIncrements){
        this.dynamicRobotIncrements = dynamicRobotIncrements;
    }

    public Vector3 getDynamicRobotIncrements() {
        return dynamicRobotIncrements;
    }
}
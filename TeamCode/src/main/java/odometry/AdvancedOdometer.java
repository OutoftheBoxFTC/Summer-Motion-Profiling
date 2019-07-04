package odometry;

import hardware.BulkReadData;
import math.Vector2;
import math.Vector3;

public class AdvancedOdometer extends Odometer{

    private Vector3 globalRobotDynamics, globalRobotVelocityDynamics;

    public AdvancedOdometer(double rotationFactor, double translationFactor, double auxRotationFactor) {
        super(rotationFactor, translationFactor, auxRotationFactor);
    }

    @Override
    public AdvancedOdometerDynamics updateRobotDynamics(BulkReadData data) {
        double left = data.getLeft(), right = data.getRight(), aux = data.getAux();
        double rotation = right-left, fwd = (left+right)/2, strafe = aux-rotation*auxRotationFactor;
        Vector3 globalRobotDynamics = new Vector3(strafe*translationFactor, fwd*translationFactor, rotation*rotationFactor);

        double vLeft = data.getvLeft(), vRight = data.getvRight(), vAux = data.getvAux();
        double vRotation = vRight-vLeft, vFwd = (vLeft+vRight)/2, vStrafe = (vAux-vRight)*auxRotationFactor;
        Vector3 globalRobotVelocityDynamics = new Vector3(vStrafe*translationFactor, vFwd*translationFactor, vRotation*rotation);

        Vector3 positionIncrements = globalRobotDynamics.subtract(this.globalRobotDynamics),
                velocityIncrements = globalRobotVelocityDynamics.subtract(this.globalRobotVelocityDynamics);

        AdvancedOdometerDynamics dynamics = new AdvancedOdometerDynamics(positionIncrements, velocityIncrements, this.globalRobotVelocityDynamics);

        this.globalRobotDynamics = globalRobotDynamics;
        this.globalRobotVelocityDynamics = globalRobotVelocityDynamics;
        return dynamics;
    }

    @Override
    public Vector2 findStaticIncrements(OdometerDynamics unCastedData) {
        AdvancedOdometerDynamics data = (AdvancedOdometerDynamics)unCastedData;
        Vector3 robotIncrements = data.getDynamicRobotIncrements(),
                robotVelocityIncrements = data.getDynamicRobotIncrements(),
                initialRobotVelocity = data.getPreviousVelocity();

    }

    @Override
    public Vector3 getGlobalDynamics() {
        return globalRobotDynamics.clone();
    }
}

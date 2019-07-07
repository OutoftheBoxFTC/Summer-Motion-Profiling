package odometry;

import hardware.BulkReadData;
import math.Vector2;
import math.Vector3;

public class SimpleOdometer extends Odometer {
    private Vector3 globalRobotDynamics;

    public SimpleOdometer(double rotationFactor, double translationFactor, double auxRotationFactor){
        super(rotationFactor, translationFactor, auxRotationFactor);
        globalRobotDynamics = new Vector3(0, 0, 0);
    }

    public SimpleOdometerDynamics updateRobotDynamics(BulkReadData data){
        int left = data.getLeft(), right = data.getRight(), aux = data.getAux();

        double newRotation = (right-left);
        double newFwd = (left+right)/2;
        double newStrafe = aux-newRotation*auxRotationFactor;

        double rotationIncrement = (newRotation-globalRobotDynamics.getC())*rotationFactor;
        double fwdIncrement = (newFwd-globalRobotDynamics.getB())*translationFactor;
        double strafeIncrement = (newStrafe-globalRobotDynamics.getA())*translationFactor;

        globalRobotDynamics.set(newStrafe, newFwd, newRotation);
        return new SimpleOdometerDynamics(new Vector3(strafeIncrement, fwdIncrement, rotationIncrement));
    }

    public Vector2 findStaticIncrements(SimpleOdometerDynamics dynamics){
        Vector3 robotIncrements = dynamics.getDynamicRobotIncrements();

        double strafe = robotIncrements.getA(),
                fwd = robotIncrements.getB(),
                rotation = robotIncrements.getC();
        double cos = Math.cos(rotation), sine = Math.sin(rotation);

        double y = fwd*sine-strafe*(cos+1);
        double x = strafe*sine-fwd*(cos+1);
        return new Vector2(x, y);
    }

    @Override
    public Vector3 getGlobalDynamics() {
        return globalRobotDynamics.dot(new Vector3(translationFactor, translationFactor, rotationFactor));
    }
}
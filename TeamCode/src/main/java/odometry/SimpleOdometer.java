package odometry;

import hardware.BulkReadData;
import math.Vector2;
import math.Vector3;

public class SimpleOdometer extends Odometer {
    private double globalFwd, globalStrafe, globalRotation;

    public SimpleOdometer(double rotationFactor, double translationFactor, double auxRotationFactor){
        super(rotationFactor, translationFactor, auxRotationFactor);
    }

    public OdometerDynamics updateRobotDynamics(BulkReadData data){
        double left = data.getLeft(), right = data.getRight(), aux = data.getAux();

        double newRotation = (right-left);
        double newFwd = (left+right)/2;
        double newStrafe = aux-newRotation*auxRotationFactor;

        double rotationIncrement = (newRotation-globalRotation)*rotationFactor;
        double fwdIncrement = (newFwd-globalFwd)*translationFactor;
        double strafeIncrement = (newStrafe-globalStrafe)*translationFactor;

        globalFwd = newFwd;
        globalRotation = newRotation;
        globalStrafe = newStrafe;
        return new OdometerDynamics(new Vector3(strafeIncrement, fwdIncrement, rotationIncrement));
    }

    public Vector2 findStaticIncrements(OdometerDynamics dynamics){
        Vector3 robotIncrements = dynamics.getDynamicRobotIncrements();

        double strafe = robotIncrements.getA(),
                fwd = robotIncrements.getB(),
                rotation = robotIncrements.getC();
        double cos = Math.cos(rotation), sine = Math.sin(rotation);

        double y = fwd*sine-strafe*(cos+1);
        double x = strafe*sine-fwd*(cos+1);
        return new Vector2(x, y);
    }

    public Vector3 getVelocity(BulkReadData data){
        double left = data.getvLeft(), right = data.getvRight(), aux = data.getvAux();
        double rotation = right-left,
                fwd = (right+left)/2,
                strafe = aux-rotation*auxRotationFactor;
        return new Vector3(strafe*translationFactor, fwd*translationFactor, rotation*rotationFactor);
    }

    @Override
    public Vector3 getGlobalDynamics() {
        return new Vector3(globalStrafe*translationFactor, globalFwd*translationFactor, globalRotation*rotationFactor);
    }
}
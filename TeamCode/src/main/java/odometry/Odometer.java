package odometry;

import hardware.BulkReadData;
import math.Vector2;
import math.Vector3;

public abstract class Odometer {
    protected double rotationFactor, translationFactor, auxRotationFactor;
    public Odometer(double rotationFactor, double translationFactor, double auxRotationFactor){
        this.rotationFactor = rotationFactor;
        this.translationFactor = translationFactor;
        this.auxRotationFactor = auxRotationFactor;
    }

    public abstract OdometerDynamics updateRobotDynamics(BulkReadData data);

    public abstract Vector2 findStaticIncrements(OdometerDynamics data);
    public abstract Vector3 getVelocity(BulkReadData data);
    public abstract Vector3 getGlobalDynamics();

    public void setFactors(double rotation, double translation, double auxRotation){
        rotationFactor = rotation;
        translationFactor = translation;
        auxRotationFactor = auxRotation;
    }
}

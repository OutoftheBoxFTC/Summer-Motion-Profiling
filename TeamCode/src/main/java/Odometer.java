
public class Odometer {
    private double rotationFactor, translationFactor, auxRotationFactor;

    private double globalFwd, globalStrafe, globalRotation;

    public Odometer(double rotationFactor, double translationFactor, double auxRotationFactor){
        this.rotationFactor = rotationFactor;
        this.translationFactor = translationFactor;
        this.auxRotationFactor = auxRotationFactor;
    }

    public Vector3 updatePosition(double left, double right, double aux){
        double newRotation = (right-left);
        double newFwd = (left+right)/2;
        double newStrafe = aux-newRotation*auxRotationFactor;

        double rotationIncrement = (newRotation-globalRotation)*rotationFactor;
        double fwdIncrement = (newFwd-globalFwd)*translationFactor;
        double strafeIncrement = (newStrafe-globalStrafe)*translationFactor;

        globalFwd = newFwd;
        globalRotation = newRotation;
        globalStrafe = newStrafe;
        return new Vector3(strafeIncrement, fwdIncrement, rotationIncrement);
    }

    public Vector3 getVelocity(double left, double right, double aux){
        double rotation = right-left,
                fwd = (right+left)/2,
                strafe = aux-rotation*auxRotationFactor;
        return new Vector3(strafe*translationFactor, fwd*translationFactor, rotation*rotationFactor);
    }

    public void setFactors(double rotation, double translation, double auxRotation){
        rotationFactor = rotation;
        translationFactor = translation;
        auxRotationFactor = auxRotation;
    }

    public double getGlobalFwd() {
        return globalFwd*translationFactor;
    }

    public double getGlobalStrafe() {
        return globalStrafe*translationFactor;
    }

    public double getGlobalRotation() {
        return globalRotation*rotationFactor;
    }
}

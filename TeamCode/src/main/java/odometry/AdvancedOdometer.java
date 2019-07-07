package odometry;

import hardware.BulkReadData;
import math.MathUtil;
import math.Vector2;
import math.Vector3;

@Deprecated
public class AdvancedOdometer extends Odometer{

    private Vector3 globalRobotDynamics, globalRobotVelocityDynamics;
    private int leftOffset, auxOffset, rightOffset;

    public AdvancedOdometer(double rotationFactor, double translationFactor, double auxRotationFactor) {
        super(rotationFactor, translationFactor, auxRotationFactor);
    }

    @Override
    public AdvancedOdometerDynamics updateRobotDynamics(BulkReadData data) {
        int left = data.getLeft()-leftOffset, right = data.getRight()-rightOffset, aux = data.getAux()-auxOffset;
        double rotation = right-left, fwd = (left+right)/2.0, strafe = aux-rotation*auxRotationFactor;
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


    //TODO finish meeee
    @Override
    public Vector2 findStaticIncrements(SimpleOdometerDynamics unCastedData) {
        AdvancedOdometerDynamics data = (AdvancedOdometerDynamics)unCastedData;
        Vector3 robotIncrements = data.getDynamicRobotIncrements(),
                robotVelocityIncrements = data.getDynamicVelocityIncrements(),
                initialRobotVelocity = data.getPreviousVelocity();
        double rInc = robotIncrements.getC(),
                vrI = initialRobotVelocity.getC(),
                vrInc = robotVelocityIncrements.getC();
        double xInc = robotIncrements.getA(),
                vxI = initialRobotVelocity.getA(),
                vxInc = robotVelocityIncrements.getA();
        double yInc = robotIncrements.getB(),
                vyI = initialRobotVelocity.getB(),
                vyInc = robotVelocityIncrements.getB();
        double t = 2*rInc/(vrInc+2*vrI);
        double ar = vrInc/t,
                ax = vxInc/t,
                ay = vyInc/t;
        boolean imaginaryIntegrals = ar<0;
        double bound = (vrI+vrInc)/Math.sqrt(Math.PI*Math.abs(ar));
        if(imaginaryIntegrals){
            bound = -bound;
        }
        double sIntegral = MathUtil.S((vrI+vrInc)/Math.sqrt(Math.PI*ar)),
                cIntegral = MathUtil.C((vrI+vrInc)/Math.sqrt(Math.PI*ar));

        double a = 1/4*Math.pow(ar/2, -3/2);
        return null;
    }

    @Override
    public Vector3 getGlobalDynamics() {
        return globalRobotDynamics.clone();
    }

    @Override
    public void calibrate(BulkReadData data) {
        leftOffset = data.getLeft();
        rightOffset = data.getRight();
        auxOffset = data.getAux();
    }


}
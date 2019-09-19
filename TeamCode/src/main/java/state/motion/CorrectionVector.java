package state.motion;

import drivetrain.RobotDrive;
import hardware.BulkReadData;
import math.Vector2;
import math.Vector3;
import motion.VelocityDriveState;
import state.StateMachine;

public class CorrectionVector extends VelocityDriveState {
    private double radius, angle;
    MOVEMENT_TYPE movement_type;
    Vector2 start = Vector2.ZERO(), target = Vector2.ZERO();
    public CorrectionVector(StateMachine stateMachine, RobotDrive robotDrive, Vector2 target, double radius, MOVEMENT_TYPE movement_type, BulkReadData data) {
        super(stateMachine, robotDrive);
        this.target = target;
        this.radius = radius;
        double x = (data.getLeft() + data.getRight()) - 2;
        double y = data.getAux() - (Math.abs(x - data.getLeft()));
        start = new Vector2(x, y);
        this.movement_type = movement_type;
    }

    @Override
    protected Vector3 getRobotVelocity() {
        return null;
    }

    public void update(BulkReadData data) {
        double x = (data.getLeft() + data.getRight()) / 2;
        double y = data.getAux() - (Math.abs(x - data.getLeft()));
        Vector2 point1 = Vector2.ZERO(), point2 = Vector2.ZERO();
        int numSolutions = getCircleIntersections(x, y, radius, start, target, point1, point2);
        if(numSolutions == 2){
            Vector2 point = (point1.distanceTo(target) < point2.distanceTo(target)) ? point1 : point2;
            Vector2 coordinates = new Vector2(x, y);
            angle = (movement_type == MOVEMENT_TYPE.FORWARD) ? (coordinates.angleTo(point)) : (coordinates.angleTo(point) * (movement_type.getP()));
        }
    }

    public double getCorrectionAngle(){
        return angle;
    }


    private int getCircleIntersections(double cx, double cy, double radius, Vector2 point1, Vector2 point2, Vector2 intersection1, Vector2 intersection2) {
        double dx, dy, A, B, C, det, t;

        dx = point2.getA() - point1.getA();
        dy = point2.getB() - point1.getB();

        A = dx * dx + dy * dy;
        B = 2 * (dx * (point1.getA() - cx) + dy * (point1.getB() - cy));
        C = (point1.getA() - cx) * (point1.getA() - cx) +
                (point1.getB() - cy) * (point1.getB() - cy) -
                radius * radius;

        det = B * B - 4 * A * C;
        if ((A <= 0.0000001) || (det < 0)) {
            // No real solutions.
            intersection1 = new Vector2(0, 0);
            intersection2 = new Vector2(0, 0);
            return 0;
        }
        else if (det == 0) {
            // One solution.
            t = -B / (2 * A);
            intersection1 =
                    new Vector2(point1.getA() + t * dx, point1.getB() + t * dy);
            intersection2 = new Vector2(0, 0);
            return 1;
        }
        else {
            // Two solutions.
            t = (float)((-B + Math.sqrt(det)) / (2 * A));
            intersection1 =
                    new Vector2(point1.getA() + t * dx, point1.getB() + t * dy);
            t = (float)((-B - Math.sqrt(det)) / (2 * A));
            intersection2 =
                    new Vector2(point1.getA() + t * dx, point1.getB() + t * dy);
            return 2;
        }
    }

    public enum MOVEMENT_TYPE{
        FORWARD(0),
        STRAFE(1),
        INVERSE_STRAFE(-1);
        private final int p;
        MOVEMENT_TYPE(int p) {
            this.p = p;
        }

        public int getP() {
            return p;
        }
    }
}

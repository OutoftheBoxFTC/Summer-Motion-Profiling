package state.motion;

import drivetrain.MecanumDrive;
import drivetrain.RobotDrive;
import hardware.BulkReadData;
import math.Vector3;
import motion.VelocityDriveState;
import state.StateMachine;

public class TurnCorrectionVector extends VelocityDriveState {
    double targetAngle, offset, kp, correction;
    boolean finished = false;
    public TurnCorrectionVector(double kp, StateMachine stateMachine, RobotDrive drive) {
        super(stateMachine, drive);
        this.kp = kp;
    }
    @Override
    public void update(BulkReadData data) {
        correction = kp * (targetAngle - (data.getGyro() - offset));
        finished = Math.abs(targetAngle - (data.getGyro() - offset)) < 3;
    }

    @Override
    protected Vector3 getRobotVelocity() {
        return new Vector3(0, 0, correction);
    }

    public boolean finished(){
        return finished;
    }

    public void setTurn(double angle, BulkReadData data){
        targetAngle = angle;
        offset = data.getGyro();
        finished = false;
    }
}

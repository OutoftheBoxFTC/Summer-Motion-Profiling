package states.logicStates.movement;

import hardware.BulkReadData;
import states.LogicState;
import states.StateMachine;

public class TurnCorrectionVector {
    double targetAngle, offset, kp, correction;
    boolean finished = false;
    public TurnCorrectionVector(double kp) {
        this.kp = kp;
    }

    public double update(BulkReadData data) {
        correction = kp * (targetAngle - (data.getGyro() - offset));
        finished = Math.abs(targetAngle - (data.getGyro() - offset)) < 3;
        return correction;
    }

    public boolean finished(BulkReadData data){
        return finished;
    }

    public void setTurn(double angle, BulkReadData data){
        targetAngle = angle;
        offset = data.getGyro();
        finished = false;
    }
}

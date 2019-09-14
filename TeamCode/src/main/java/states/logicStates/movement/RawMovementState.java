package states.logicStates.movement;

import hardware.BulkReadData;
import hardware.Hardware;
import math.Vector4;
import states.DriveState;
import states.LogicState;
import states.StateMachine;

public class RawMovementState extends DriveState {
    Hardware hardware;
    Vector4 wheelVelocities = new Vector4(0, 0, 0, 0);
    public RawMovementState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void update(BulkReadData data) {

    }

    @Override
    public Vector4 getWheelVelocities() {
        return wheelVelocities;
    }

    public void drive(double forward, double strafe, double rot){
        wheelVelocities = new Vector4(-forward + strafe + rot, forward + strafe + rot, -forward - strafe + rot, forward - strafe + rot);
    }
}

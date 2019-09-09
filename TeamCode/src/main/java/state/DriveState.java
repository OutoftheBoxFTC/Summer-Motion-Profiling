package state;

import hardware.BulkReadData;
import math.Vector3;
import math.Vector4;
import state.LogicState;
import state.StateMachine;

public abstract class DriveState extends LogicState {

    public DriveState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void update(BulkReadData data) {

    }

    public abstract Vector4 getWheelVelocities();
}
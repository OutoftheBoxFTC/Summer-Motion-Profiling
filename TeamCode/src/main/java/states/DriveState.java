package states;

import hardware.BulkReadData;
import math.Vector4;

public abstract class DriveState extends LogicState {

    public DriveState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void update(BulkReadData data) {

    }

    public abstract Vector4 getWheelVelocities();
}
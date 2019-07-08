package motion;

import hardware.BulkReadData;
import math.Vector3;
import state.LogicState;
import state.StateMachine;

public abstract class DriveState extends LogicState {

    public DriveState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void update(BulkReadData data) {

    }

    public abstract Vector3 getRobotVelocity();
}
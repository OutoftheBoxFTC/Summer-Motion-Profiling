package state.motion;

import hardware.BulkReadData;
import state.LogicState;
import state.StateMachine;

public class MotionManager extends LogicState {

    public MotionManager(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void update(BulkReadData data) {

    }
}

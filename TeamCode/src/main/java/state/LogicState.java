package state;

import hardware.BulkReadData;

public abstract class LogicState {
    protected StateMachine stateMachine;
    public LogicState(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    public abstract void update(BulkReadData data);
}
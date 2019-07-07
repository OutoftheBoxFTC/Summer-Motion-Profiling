package state;

import hardware.BulkReadData;

public abstract class LogicState {
    protected StateMachine stateMachine;
    private String thisState;
    public LogicState(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    public abstract void update(BulkReadData data);

    protected void deactivateThis(){
        stateMachine.deactivateLogic(thisState);
    }

    public void setThisState(String thisState) {
        this.thisState = thisState;
    }

    public void init(BulkReadData data){

    }
}
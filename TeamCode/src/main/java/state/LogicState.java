package state;

import hardware.BulkReadData;

public abstract class LogicState {
    protected StateMachine stateMachine;
    protected String stateName;
    public LogicState(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    public abstract void update(BulkReadData data);

    protected void deactivateThis(){
        stateMachine.deactivateLogic(stateName);
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void init(BulkReadData data){

    }

    public String getStateName() {
        return stateName;
    }
}
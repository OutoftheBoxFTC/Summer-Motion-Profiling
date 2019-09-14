package states;

import hardware.BulkReadData;

public abstract class ManagerState {
    protected StateMachine stateMachine;
    protected String stateName;
    protected LogicState[] logicStates;

    public ManagerState(LogicState... states)
    {
        logicStates = states;
    }

    public void update(BulkReadData data){
        for(LogicState logicState : logicStates){
            logicState.update(data);
        }
    }

    protected void deactivateThis(){
        for(LogicState logicState: logicStates){
            logicState.deactivateThis();
        }
        stateMachine.deactivateLogic(stateName);
    }

    public void setStateName(String name){
        this.stateName = name;
    }

    public void init(BulkReadData data){}

    public LogicState[] getLogicStates(){
        return logicStates;
    }
}

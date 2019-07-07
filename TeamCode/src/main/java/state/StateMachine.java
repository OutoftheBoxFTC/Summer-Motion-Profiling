package state;

import java.util.ArrayList;
import java.util.HashMap;

import hardware.BulkReadData;
import math.Vector3;

public class StateMachine {
    private HashMap<String, DriveState> driveStates;
    private HashMap<String, LogicState> logicStates;

    private DriveState activeDriveState, activatedDriveState;
    private ArrayList<LogicState> activeLogicStates;

    private ArrayList<LogicState> deactivatedLogicStates, activatedLogicStates;

    public StateMachine(){
        driveStates = new HashMap<>();
        logicStates = new HashMap<>();

        activeLogicStates = new ArrayList<>();
        activatedLogicStates = new ArrayList<>();
        activeDriveState = null;
        activatedDriveState = null;
        activatedLogicStates = new ArrayList<>();
        deactivatedLogicStates = new ArrayList<>();
    }

    public void update(BulkReadData data){
        for(LogicState state : activeLogicStates){
            state.update(data);
        }
        activeLogicStates.addAll(activatedLogicStates);
        activeLogicStates.removeAll(deactivatedLogicStates);
        activatedLogicStates.clear();
        deactivatedLogicStates.clear();
    }

    public Vector3 getMotorVelocity(){
        Vector3 velocity = new Vector3(0, 0, 0);
        if(activeDriveState!=null){
            velocity = activeDriveState.getMotorPowers();
        }

        if(activatedDriveState != null){
            activeDriveState = activatedDriveState;
            activatedDriveState = null;
        }
        return velocity;
    }

    public void appendLogicStates(HashMap<String, LogicState> states){
        logicStates.putAll(states);
    }

    public void deactivateLogic(String state){
        deactivatedLogicStates.add(logicStates.get(state));
    }

    public void activateLogic(String state){
        activatedLogicStates.add(logicStates.get(state));
    }

    public void setActiveDriveState(String state){
        activatedDriveState = driveStates.get(state);
    }

    public LogicState getState(String logicState){
        return logicStates.get(logicState);
    }

    public boolean isActive(LogicState state){
        return activeLogicStates.contains(state);
    }
}
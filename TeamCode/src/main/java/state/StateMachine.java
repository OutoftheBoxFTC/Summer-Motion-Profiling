package state;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import debug.SmartTelemetry;
import hardware.BulkReadData;
import math.Vector3;

public class StateMachine {
    private HashMap<String, DriveState> driveStates;
    private HashMap<String, LogicState> logicStates;

    private DriveState activeDriveState, activatedDriveState;
    private ArrayList<LogicState> activeLogicStates;

    private ArrayList<LogicState> deactivatedLogicStates, activatedLogicStates;

    private HashMap<String, Long> queriedActivations;
    private SmartTelemetry telemetry;

    public StateMachine(){
        this.telemetry = telemetry;
        driveStates = new HashMap<>();
        logicStates = new HashMap<>();

        activeLogicStates = new ArrayList<>();
        activatedLogicStates = new ArrayList<>();
        activeDriveState = null;
        activatedDriveState = null;
        activatedLogicStates = new ArrayList<>();
        deactivatedLogicStates = new ArrayList<>();
        queriedActivations = new HashMap<>();
    }

    public void update(BulkReadData data){
        for(LogicState state : activatedLogicStates){
            state.init(data);
        }
        activeLogicStates.addAll(activatedLogicStates);
        activatedLogicStates.clear();
        for(LogicState state : activeLogicStates){
            Log.d("Checkpoint 2", data==null?"No":"Yes");
            state.update(data);
        }

        if(!queriedActivations.isEmpty()) {
            ArrayList<String> removedQueries = new ArrayList<>();
            for (String key : queriedActivations.keySet()) {
                if (queriedActivations.get(key) < System.currentTimeMillis()) {
                    activatedLogicStates.add(logicStates.get(key));
                    removedQueries.add(key);
                }
            }
            for(String key : removedQueries){
                queriedActivations.remove(key);
            }
        }
        activeLogicStates.removeAll(deactivatedLogicStates);
        deactivatedLogicStates.clear();
    }

    public Vector3 getDriveVelocities(){
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
        for (String key : states.keySet()){
            states.get(key).setStateName(key);
        }
        logicStates.putAll(states);
    }

    public void appendDriveStates(HashMap<String, DriveState> states){
        driveStates.putAll(states);
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

    public boolean logicStateActive(String logicState){
        return activeLogicStates.contains(logicStates.get(logicState));
    }

    public boolean driveStateActive(String driveState){
        return activeDriveState.equals(driveStates.get(driveState));
    }

    public void activateLogic(String state, long time) {

    }

    public String[] getActiveStates() {
        ArrayList<String> activeStates = new ArrayList<>();
        for(LogicState state : activeLogicStates){
            activeStates.add(state.getStateName());
        }
        return activeStates.toArray(new String[activatedLogicStates.size()]);
    }
}
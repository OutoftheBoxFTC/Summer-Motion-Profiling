package states.managerStates;

import hardware.BulkReadData;
import math.Vector2;
import states.LogicState;
import states.StateMachine;
import states.logicStates.movement.MovementCorrectionVector;
import states.logicStates.movement.RawMovementState;
import states.logicStates.movement.TurnCorrectionVector;

public class MovementManager extends LogicState {
    RawMovementState rawMovementState;
    MovementCorrectionVector movementCorrectionVector;
    TurnCorrectionVector turnCorrectionVector;
    double forward, strafe, rawRot, rot, radius;
    Vector2 coordinates;
    boolean isStrafe = false;
    int strafeDir = 0;
    public MovementManager(RawMovementState rawMovementState, MovementCorrectionVector movementCorrectionVector, TurnCorrectionVector turnCorrectionVector, StateMachine stateMachine){
        super(stateMachine);
        this.rawMovementState = rawMovementState;
        this.movementCorrectionVector = movementCorrectionVector;
        this.turnCorrectionVector = turnCorrectionVector;
    }
    public MovementManager(RawMovementState rawMovementState, StateMachine stateMachine){
        super(stateMachine);
        this.rawMovementState = rawMovementState;
    }
    @Override
    public void update(BulkReadData data){
        rawMovementState.update(data);
        if(movementCorrectionVector != null){
            movementCorrectionVector.update(data);
            movementCorrectionVector.update(data);
        }
        if(!turnCorrectionVector.finished(data)){
            double correction = turnCorrectionVector.update(data);
            rawMovementState.drive(0, 0, correction);
        }else{
            double correction = movementCorrectionVector.getCorrectionAngle();
            turnCorrectionVector.setTurn(correction + (isStrafe ? (90*strafeDir) : 0), data);
            double turnCorrection = turnCorrectionVector.update(data);
            rawMovementState.drive(isStrafe ? 0 : (1 * strafeDir), isStrafe ? (1 * strafeDir) : 0, turnCorrection);
        }

    }

    public void driveRelative(Vector2 coordinates, double rot){

    }

    public void driveToPoint(Vector2 coordinates, double rot, double radius, boolean strafe, int strafeDir, BulkReadData data){
        this.coordinates = coordinates;
        this.rot = rot;
        this.radius = radius;
        this.isStrafe = strafe;
        this.strafeDir = strafeDir;
        turnCorrectionVector.setTurn(rot + (strafe ? (90*strafeDir) : 0), data);
        movementCorrectionVector.setTarget(coordinates, radius, data);
    }

    public void driveRaw(Vector2 coordinates, double rot){
        forward = coordinates.getA();
        strafe = coordinates.getB();
        this.rawRot = rot;
        rawMovementState.drive(forward, strafe, rot);
    }
}

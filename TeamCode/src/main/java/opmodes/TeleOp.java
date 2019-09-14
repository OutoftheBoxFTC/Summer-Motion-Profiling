package opmodes;

import java.util.HashMap;

import hardware.BulkReadData;
import math.Vector2;
import states.DriveState;
import states.LogicState;
import states.logicStates.movement.MovementCorrectionVector;
import states.logicStates.movement.RawMovementState;
import states.logicStates.movement.TurnCorrectionVector;
import states.managerStates.MovementManager;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="FunctionTest")
public class TeleOp extends BasicOpmode {
    MovementManager manager;
    RawMovementState rawMovementState;
    MovementCorrectionVector movementCorrectionVector;
    TurnCorrectionVector turnCorrectionVector;
    double forw = 1, stra = 2;
    public TeleOp() {
        super(1, false);
    }

    @Override
    protected void setup() {
        final HashMap<String, LogicState> states = new HashMap<>();
        rawMovementState = new RawMovementState(stateMachine);
        movementCorrectionVector = new MovementCorrectionVector();
        turnCorrectionVector = new TurnCorrectionVector(1);
        manager = new MovementManager(rawMovementState, stateMachine);
        states.put("init", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if(isStarted()){
                    deactivateThis();
                    stateMachine.activateLogic("Gamepad Drive");
                    stateMachine.setActiveDriveState("Raw Movement State");
                    stateMachine.activateLogic("Debug System");
                    stateMachine.activateLogic("Manager");
                    telemetry.clearAllHeadersExcept("Main Loop FPS", "Hardware FPS", "Activated Logic States", "vel");
                }
            }
        });
        states.put("Gamepad Drive", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                double forward = gamepad1.left_stick_y;
                double strafe = gamepad1.left_stick_x;
                forw = forward;
                stra = strafe;
                double angle = (data.getGyro()) + (Math.atan2(forward, strafe));
                double newFor = Math.sqrt((strafe * strafe) + (forward * forward)) * Math.sin(angle);
                double newStr = Math.sqrt((strafe * strafe) + (forward * forward)) * Math.cos(angle);
                manager.driveRaw(new Vector2(newFor, newStr), gamepad1.right_stick_x);
            }
        });
        states.put("Debug System", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                //telemetry.addOrientation(new Vector3(stra, forw, data.getGyro()));
                //telemetry.addOrientation(new Vector3(stra, forw, 33));
            }
        });
        HashMap<String, DriveState> drives = new HashMap<>();
        drives.put("Raw Movement State", rawMovementState);
        states.put("Manager", manager);
        stateMachine.appendLogicStates(states);
        stateMachine.appendDriveStates(drives);
        stateMachine.activateLogic("init");
    }
}

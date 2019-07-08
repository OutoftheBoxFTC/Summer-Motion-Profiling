package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import hardware.BulkReadData;
import math.Vector3;
import math.Vector4;
import state.DriveState;
import state.LogicState;

@TeleOp(name = "Functionality Test")
public class SensorReadout extends BasicOpmode {

    public SensorReadout() {
        super(new HolonomicDrive(1), 0, false);
    }

    @Override
    protected void setup() {
        robot.enableGyro();
        HashMap<String, LogicState> logicStates = new HashMap<>();
        logicStates.put("init", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if(isStarted()){
                    deactivateThis();
                    stateMachine.activateLogic("Sensor Readout");
                    telemetry.clearAllHeadersExcept("Main Loop FPS", "Hardware FPS", "Activated Logic States");
                }
            }
        });
        logicStates.put("Sensor Readout", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                telemetry.setHeader("left", String.valueOf(data.getLeft()));
                telemetry.setHeader("Right", String.valueOf(data.getRight()));
                telemetry.setHeader("Aux", String.valueOf(data.getAux()));
                telemetry.setHeader("Gyro", String.valueOf(data.getGyro()));
                if(isStopRequested()){
                    stateMachine.deactivateLogic("Sensor Readout");
                }
            }
        });
        HashMap<String, DriveState> driveStates = new HashMap<>();
        driveStates.put("Controller Drive", new DriveState() {
            @Override
            public Vector3 getMotorPowers() {
                //a, b, c, d
                return robotDrive.getRobotVelocity(new Vector4(gamepad1.y?0.5:0, gamepad1.x?0.5:0, gamepad1.a?0.5:0, gamepad1.b?0.5:0));
            }
        });
        stateMachine.appendLogicStates(logicStates);
        stateMachine.appendDriveStates(driveStates);
        stateMachine.activateLogic("init");
        stateMachine.setActiveDriveState("Controller Drive");
    }
}
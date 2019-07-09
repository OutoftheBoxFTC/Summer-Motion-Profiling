package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import hardware.BulkReadData;
import math.Vector3;
import motion.DriveState;
import motion.DriverControl;
import motion.FieldCentricDriverControl;
import odometry.SimpleOdometer;
import state.LogicState;
import state.Orientation;

@TeleOp(name = "Driver Controller Test.")
public class DriverControllerTest extends BasicOpmode {
    private Vector3 position;

    public DriverControllerTest() {
        super(new HolonomicDrive(1), 0.3, false);
    }

    @Override
    protected void setup() {
        HashMap<String, LogicState> logicStates = new HashMap<>();
        HashMap<String, DriveState> driveStates = new HashMap<>();
        position = new Vector3(0, 0, 0);
        logicStates.put("init", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if (isStarted()){
                    stateMachine.setActiveDriveState("field centric");
                    stateMachine.activateLogic("run");
                    stateMachine.activateLogic("orientation");
                    deactivateThis();
                }
            }
        });

        logicStates.put("run", new LogicState(stateMachine) {
            @Override
            public void init(BulkReadData data) {

            }

            @Override
            public void update(BulkReadData data) {

            }
        });

        logicStates.put("orientation", new Orientation(stateMachine, new SimpleOdometer(), position, new Vector3(0, 0, 0)){
            @Override
            public void update(BulkReadData data) {
                telemetry.setHeader("x", position.getA());
                telemetry.setHeader("y", position.getB());
                telemetry.setHeader("r", Math.toDegrees(position.getC()));
            }
        });

        driveStates.put("none", new DriveState(stateMachine) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0);
            }
        });
        driveStates.put("robot centric", new DriverControl(gamepad1, stateMachine));
        driveStates.put("field centric", new FieldCentricDriverControl(position, gamepad1, stateMachine));

        stateMachine.appendLogicStates(logicStates);
        stateMachine.appendDriveStates(driveStates);

        stateMachine.setActiveDriveState("none");
        stateMachine.activateLogic("init");
    }
}
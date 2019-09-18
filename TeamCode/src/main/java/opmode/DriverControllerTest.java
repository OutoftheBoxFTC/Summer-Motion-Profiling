package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import drivetrain.MecanumDrive;
import hardware.BulkReadData;
import math.Vector3;
import motion.DriverControl;
import motion.VelocityDriveState;
import state.DriveState;
import state.LogicState;

@TeleOp(name = "Driver Controller Test")
public class DriverControllerTest extends BasicOpmode {
    private MecanumDrive drive;

    public DriverControllerTest() {
        super(0.3, false);
    }

    @Override
    protected void setup() {
        HashMap<String, LogicState> logicStates = new HashMap<>();
        HashMap<String, DriveState> driveStates = new HashMap<>();
        drive = new MecanumDrive(MecanumDrive.Polarity.IN, Math.PI/4, 1);
        logicStates.put("init", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if (isStarted()){
                    stateMachine.setActiveDriveState("robot centric");
                    stateMachine.activateLogic("run");
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
                int servoPos = 0;
                servoPos += gamepad1.right_trigger;
                servoPos -= gamepad1.left_trigger;
                servoPos = Math.max(0, servoPos);
                servoPos = Math.min(1, servoPos);
                robot.getServo().setPosition(servoPos);
            }
        });

        driveStates.put("none", new VelocityDriveState(stateMachine, drive) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0);
            }
        });
        driveStates.put("robot centric", new DriverControl(gamepad1, stateMachine, drive));

        stateMachine.appendLogicStates(logicStates);
        stateMachine.appendDriveStates(driveStates);

        stateMachine.setActiveDriveState("none");
        stateMachine.activateLogic("init");
    }
}
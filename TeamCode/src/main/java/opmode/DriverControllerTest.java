package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import drivetrain.MecanumDrive;
import hardware.BulkReadData;
import hardware.Hardware;
import math.Vector3;
import motion.DriverControl;
import motion.FieldCentricDriverControl;
import motion.VelocityDriveState;
import state.DriveState;
import state.LogicState;

@TeleOp(name = "Driver Controller Test")
public class DriverControllerTest extends BasicOpmode {
    private MecanumDrive drive;
    private Vector3 position;

    public DriverControllerTest() {
        super(0.3, false);
        position = new Vector3(0, 0, 0);
    }

    @Override
    protected void setup() {
        robot.registerDevice(Hardware.HardwareDevice.DRIVE_MOTORS);
        robot.registerDevice(Hardware.HardwareDevice.GYRO);
        robot.disableDevice(Hardware.HardwareDevice.GYRO);
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
                if(gamepad1.a.isActive()&&gamepad1.a.isUpdated()){
                    stateMachine.setActiveDriveState("field centric");
                    robot.enableDevice(Hardware.HardwareDevice.GYRO);
                } else if(gamepad1.b.isActive()&&gamepad1.b.isUpdated()){
                    stateMachine.setActiveDriveState("robot centric");
                    robot.disableDevice(Hardware.HardwareDevice.GYRO);
                }
                position.setC(data.getGyro());
            }
        });

        driveStates.put("none", new VelocityDriveState(stateMachine, drive) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0);
            }
        });
        driveStates.put("robot centric", new DriverControl(gamepad1, stateMachine, drive));
        driveStates.put("field centric", new FieldCentricDriverControl(position, gamepad1, stateMachine, drive));

        stateMachine.appendLogicStates(logicStates);
        stateMachine.appendDriveStates(driveStates);

        stateMachine.setActiveDriveState("none");
        stateMachine.activateLogic("init");
    }
}
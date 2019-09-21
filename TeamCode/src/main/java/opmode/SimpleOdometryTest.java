package opmode;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import drivetrain.MecanumDrive;
import drivetrain.RobotDrive;
import hardware.BulkReadData;
import hardware.Hardware;
import math.Vector2;
import math.Vector3;
import motion.DriveToZero;
import motion.PIDControl;
import motion.PIDControl2;
import motion.VelocityDriveState;
import odometry.Odometer;
import odometry.SimpleOdometer;
import state.DriveState;
import state.LogicState;
import state.Orientation;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Simple Odometry Test")
public class SimpleOdometryTest extends BasicOpmode {
    private Odometer odometer;
    private Vector3 position, velocity;
    private static final double TRANSLATION_TOLERANCE = 0.1, ROTATION_TOLERANCE = Math.toRadians(0.5);
    private MecanumDrive drive;

    public SimpleOdometryTest() {
        super(0.1, true);
        drive = new MecanumDrive(MecanumDrive.Polarity.IN, Math.PI/4, 1);
    }

    @Override
    protected void setup() {
        robot.registerDevice(Hardware.HardwareDevice.GYRO);
        robot.registerDevice(Hardware.HardwareDevice.DRIVE_MOTORS);
        robot.registerDevice(Hardware.HardwareDevice.HUB_1_BULK);
        odometer = new SimpleOdometer();
        position = new Vector3(0, 0, 0);
        velocity = new Vector3(0, 0, 0);

        HashMap<String, LogicState> logicStates = new HashMap<>();
        final HashMap<String, DriveState> driveStates = new HashMap<>();
        logicStates.put("Orientation", new Orientation(stateMachine, odometer, position, velocity){
            @Override
            public void update(BulkReadData data) {
                super.update(data);
                telemetry.setHeader("X", position.getA());
                telemetry.setHeader("Y", position.getB());
                telemetry.setHeader("R", Math.toDegrees(position.getC()));
            }
        });
        logicStates.put("Init", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if(isStarted()){
                    stateMachine.activateLogic("Orientation");
                    stateMachine.activateLogic("Tracking");
                    deactivateThis();
                }
            }
        });

        logicStates.put("Tracking", new LogicState(stateMachine) {
            @Override
            public void init(BulkReadData data) {
                telemetry.pingMessage("Instructions", "Mess with it", 6000);
            }

            @Override
            public void update(BulkReadData data) {
                if(gamepad1.a.isActive()){
                    deactivateThis();
                    stateMachine.activateLogic("Terminate At Zero");
                    stateMachine.setActiveDriveState("Drive To Zero");
                }
            }
        });

        logicStates.put("Terminate At Zero", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if(new Vector2(position).length()<0.1&&position.getC()<Math.toRadians(0.1)){
                    stateMachine.setActiveDriveState("None");
                    deactivateThis();
                }
            }
        });

        driveStates.put("None", new VelocityDriveState(stateMachine, drive) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0);
            }
        });

        //TODO tune these and create some kind of global tuning reference system
        driveStates.put("Drive To Zero", new DriveToZero(position, new PIDControl2(1, 1, 1), new PIDControl(1, 1, 1), stateMachine, drive));
        stateMachine.appendDriveStates(driveStates);
        stateMachine.appendLogicStates(logicStates);
        stateMachine.setActiveDriveState("None");
        stateMachine.activateLogic("Init");
    }
}

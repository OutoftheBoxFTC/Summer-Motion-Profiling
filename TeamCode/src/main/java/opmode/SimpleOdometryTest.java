package opmode;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import hardware.BulkReadData;
import math.Vector2;
import math.Vector3;
import motion.DriveToZero;
import motion.PIDControl;
import motion.PIDControl2;
import odometry.Odometer;
import odometry.SimpleOdometer;
import motion.DriveState;
import state.LogicState;
import state.Orientation;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Simple Odometry Test")
public class SimpleOdometryTest extends BasicOpmode {
    private Odometer odometer;
    private Vector3 position, velocity;
    private static final double TRANSLATION_TOLERANCE = 0.1, ROTATION_TOLERANCE = Math.toRadians(0.5);


    public SimpleOdometryTest() {
        super(new HolonomicDrive(1), 0.1, true);
    }

    @Override
    protected void setup() {
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
                telemetry.pingMessage("Instructions", "Fugg wid it", 6000);
            }

            @Override
            public void update(BulkReadData data) {
                if(gamepad1.a){
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

        driveStates.put("None", new DriveState(stateMachine) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0);
            }
        });

        //TODO tune these and create some kind of global tuning reference system
        driveStates.put("Drive To Zero", new DriveToZero(position, new PIDControl2(1, 1, 1), new PIDControl(1, 1, 1), stateMachine));
        stateMachine.appendDriveStates(driveStates);
        stateMachine.appendLogicStates(logicStates);
        stateMachine.setActiveDriveState("None");
        stateMachine.activateLogic("Init");
    }
}

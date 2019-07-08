package opmode;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import hardware.BulkReadData;
import math.Vector3;
import motion.DriveToZero;
import motion.PIDControl;
import motion.PIDControl2;
import odometry.Odometer;
import odometry.SimpleOdometer;
import motion.DriveState;
import state.LogicState;
import state.Orientation;

public class OdometryTest extends BasicOpmode {
    private Odometer odometer;
    private Vector3 position, velocity;
    private static final double TRANSLATION_TOLERANCE = 0.1, ROTATION_TOLERANCE = Math.toRadians(0.5);


    public OdometryTest() {
        super(new HolonomicDrive(1), 0.1, true);
    }

    @Override
    protected void setup() {
        odometer = new SimpleOdometer();
        position = new Vector3(0, 0, 0);
        velocity = new Vector3(0, 0, 0);

        HashMap<String, LogicState> logicStates = new HashMap<>();
        HashMap<String, DriveState> driveStates = new HashMap<>();
        logicStates.put("Orientation", new Orientation(stateMachine, odometer, position, velocity));
        logicStates.put("Init", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if(isStarted()){
                    stateMachine.activateLogic("Orientation");
                    stateMachine.activateLogic("Tracking");
                    telemetry.setHeader("Instructions", "Fugg wid it");
                    deactivateThis();
                }
            }
        });

        logicStates.put("Tracking", new LogicState(stateMachine) {
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

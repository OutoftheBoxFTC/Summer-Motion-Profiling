package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import hardware.BulkReadData;
import math.Vector3;
import odometry.Odometer;
import odometry.SimpleOdometer;
import motion.DriveState;
import state.LogicState;

@TeleOp(name = "Simple Odometer Tuning")
public class SimpleOdometryTuning extends BasicOpmode {

    private Odometer odometer;
    private static final double TAU = Math.PI*2;

    public SimpleOdometryTuning() {
        super(new HolonomicDrive(1), 0.1, false);
    }

    @Override
    protected void setup() {
        odometer = new SimpleOdometer(1, 1, 1);
        final HashMap<String, LogicState> logicStates = new HashMap<>();
        robot.enableGyro();
        logicStates.put("init", new LogicState(stateMachine) {
            @Override
            public void init(BulkReadData data) {
                telemetry.pingMessage("Instructions", "Hit start, then wait for the robot to finish rotating. Note down the generated factors", 10000);
            }

            @Override
            public void update(BulkReadData data) {
                if(isStarted()){
                    deactivateThis();
                    stateMachine.activateLogic("rotation tracking");
                    stateMachine.setActiveDriveState("rotation");
                    telemetry.clearAllHeadersExcept("Main Loop FPS", "Hardware FPS", "Activated Logic States");
                }
            }
        });
        logicStates.put("rotation tracking", new LogicState(stateMachine) {
            private int rotations;
            private double previousGyro;

            @Override
            public void init(BulkReadData data) {
                rotations = 0;
                previousGyro = 0;
            }

            @Override
            public void update(BulkReadData data) {
                double gyro = data.getGyro();
                if(rotations >= 10){
                    stateMachine.setActiveDriveState("none");
                    deactivateThis();
                    stateMachine.activateLogic("Stop Moving");
                }
                odometer.updateRobotDynamics(data);
                telemetry.setHeader("rotations", String.valueOf(rotations));
                telemetry.setHeader("gyro", String.valueOf(gyro));
                if(gyro < previousGyro){
                    rotations++;
                }
                previousGyro = gyro;
            }
        });
        logicStates.put("Stop Moving", new LogicState(stateMachine){
            private long lastTime;
            @Override
            public void init(BulkReadData data) {
                lastTime = System.currentTimeMillis();
            }

            @Override
            public void update(BulkReadData data) {
                odometer.updateRobotDynamics(data);
                if(System.currentTimeMillis()-lastTime>250){
                    telemetry.pingMessage("Instructions", "Note down these factors and"+
                            "Move the robot to a place where you can push it in one direction for a long distance. Hit (A) when you are ready to proceed.", 12000);
                    double radiansTurned = 10*TAU+data.getGyro();
                    double rotation = odometer.getGlobalDynamics().getC();
                    double rotationFactor = radiansTurned/rotation;
                    double auxRotationFactor = data.getAux()/rotation;
                    telemetry.setHeader("Rotation Factor", String.valueOf(rotationFactor));
                    telemetry.setHeader("Aux Rotation Factor", String.valueOf(auxRotationFactor));
                    deactivateThis();
                    stateMachine.activateLogic("Wait for A");
                }
            }
        });
        logicStates.put("Wait for A", new LogicState(stateMachine) {
            @Override
            public void update(BulkReadData data) {
                if(gamepad1.a){
                    deactivateThis();
                    stateMachine.activateLogic("Odometer Readout");
                }
            }
        });
        logicStates.put("Odometer Readout", new LogicState(stateMachine) {
            @Override
            public void init(BulkReadData data) {
                telemetry.pingMessage("Instructions", "Push the robot as far as you can in one cardinal direction. Be extremely careful not to rotate it while doing this.", 12000);
            }

            @Override
            public void update(BulkReadData data) {
                telemetry.setHeader("LR Avg", String.valueOf((data.getLeft()+data.getRight())/2.0));
                telemetry.setHeader("Aux", String.valueOf(data.getAux()));
            }
        });

        HashMap<String, DriveState> driveStates = new HashMap<>();
        driveStates.put("none", new DriveState(stateMachine) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0);
            }
        });
        driveStates.put("rotation", new DriveState(stateMachine) {
            @Override
            public Vector3 getRobotVelocity() {
                return new Vector3(0, 0, 0.2);
            }
        });
        stateMachine.appendLogicStates(logicStates);
        stateMachine.appendDriveStates(driveStates);
        stateMachine.activateLogic("init");
        stateMachine.setActiveDriveState("none");
    }
}

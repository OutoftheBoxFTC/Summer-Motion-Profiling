package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import drivetrain.MecanumDrive;
import hardware.BulkReadData;
import math.Vector3;
import math.Vector4;
import motion.DriveState;
import state.LogicState;
/**
 * This class is a raw debug class of all sensors/functionality to test against expected behaviour.
 * Y: Front left wheel fwd (a)
 * X: Front right wheel fwd (b)
 * A: Back left wheel fwd (c)
 * B: Back right wheel fwd (d)
 *
 * When moving robot fwd, left and right increases
 * When rotating cc, aux and right increases, left decreases
 *
 * Gyro starts at 0, increases to 2pi, and returns to 0 after 1 full cc rotation
 */
@TeleOp(name = "Functionality Test")
public class FunctionalityTest extends BasicOpmode {

    public FunctionalityTest() {
        super(new MecanumDrive(MecanumDrive.Polarity.IN, Math.PI/4, 1), 1, false);
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
                    stateMachine.setActiveDriveState("Controller Drive");
                    telemetry.clearAllHeadersExcept("Main Loop FPS", "Hardware FPS", "Activated Logic States", "vel");
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
        driveStates.put("Controller Drive", new DriveState(stateMachine) {
            @Override
            public Vector3 getRobotVelocity() {
                //a, b, c, d
                Vector3 vel = robotDrive.getRobotVelocity(new Vector4(gamepad1.y?0.5:0, gamepad1.x?0.5:0, gamepad1.a?0.5:0, gamepad1.b?0.5:0));
                telemetry.setHeader("vel", vel.toString());
                return vel;
            }
        });
        stateMachine.appendLogicStates(logicStates);
        stateMachine.appendDriveStates(driveStates);
        stateMachine.activateLogic("init");
    }
}
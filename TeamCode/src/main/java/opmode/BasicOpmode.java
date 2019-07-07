package opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import debug.FPSDebug;
import debug.SmartTelemetry;
import hardware.BulkReadData;
import hardware.Hardware;
import math.Matrix22;
import math.Vector3;
import math.Vector4;
import state.StateMachine;
import drivetrain.RobotDrive;
/**
 * Notes:
 *  "Dynamics" refers to any robot centric metric
 *  Velocity is obtained from odometry wheel readings rather than position differentiation (for now)
 */
public abstract class BasicOpmode extends LinearOpMode{
    protected Hardware robot;
    protected FPSDebug fpsDebug;
    protected SmartTelemetry telemetry;
    protected RobotDrive robotDrive;
    protected StateMachine stateMachine;
    private ExecutorService threadManager;

    private double driveLoopPriority;

    public BasicOpmode(RobotDrive robotDrive, double driveLoopPriority){
        this.robotDrive = robotDrive;
        this.driveLoopPriority = driveLoopPriority;
    }

    @Override
    public void runOpMode() {
        this.telemetry = new SmartTelemetry(super.telemetry);
        fpsDebug = new FPSDebug(telemetry, "Main Loop");
        stateMachine = new StateMachine();

        robot = new Hardware(this, telemetry);
        robot.init();
        threadManager = Executors.newFixedThreadPool(1);
        setupStates(stateMachine);
        threadManager.execute(robot);
        double driveIterations = 0;
        while (!isStopRequested()){
            BulkReadData data = robot.newData();//stalls here until hardware loop obtains new data
            fpsDebug.startIncrement();
            stateMachine.update(data);
            while (driveIterations >= 1) {
                Vector3 robotVelocity = stateMachine.getMotorVelocity();
                Vector4 wheels = robotDrive.getWheelVelocities(robotVelocity);
                robot.drive(wheels.getA(), wheels.getB(), wheels.getC(), wheels.getD());
                driveIterations--;
            }
            driveIterations += driveLoopPriority;
            fpsDebug.endIncrement();
            fpsDebug.update();
            fpsDebug.queryFPS();
            telemetry.update();
        }

    }

    protected abstract void setupStates(StateMachine states);
}
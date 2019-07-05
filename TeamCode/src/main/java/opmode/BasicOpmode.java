package opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import debug.FPSDebug;
import debug.SmartTelemetry;
import hardware.BulkReadData;
import hardware.Hardware;
import math.Matrix22;
import math.Vector2;
import math.Vector3;
import odometry.Odometer;
import odometry.SimpleOdometerDynamics;
/**
 * Notes:
 *  "Dynamics" refers to any robot centric metric
 *  Velocity is obtained from odometry wheel readings rather than position differentiation (for now)
 */
public abstract class BasicOpmode extends LinearOpMode{
    protected Vector2 position;
    protected double initialRotation;
    private Vector3 velocity;
    protected Odometer odometer;
    protected Hardware robot;
    protected FPSDebug fpsDebug;
    protected SmartTelemetry telemetry;

    public BasicOpmode(Odometer odometer){
        this.odometer = odometer;
        this.telemetry = new SmartTelemetry(super.telemetry);
        fpsDebug = new FPSDebug(telemetry, "Main Loop");
    }

    public void update(){
        BulkReadData data = robot.newData();//stalls here until hardware loop obtains new data
        fpsDebug.startIncrement();

        updateOrientation(data);

        fpsDebug.endIncrement();
        fpsDebug.update();
        fpsDebug.queryFPS();
        telemetry.update();
    }

    private Matrix22 rotationMatrix(double rotation){
        double cos = Math.cos(rotation),
                sine = Math.sin(rotation);
        return new Matrix22(cos, sine, sine, cos);
    }

    protected void setStartingOrientation(Vector3 initialOrientation){
        this.position = new Vector2(initialOrientation.getA(), initialOrientation.getB());
        this.initialRotation = initialOrientation.getC();
        this.velocity = new Vector3(0, 0, 0);//hopefully
    }

    private void updateOrientation(BulkReadData data){
        SimpleOdometerDynamics dynamicRobotIncrements = odometer.updateRobotDynamics(data);

        Vector3 globalDynamics = odometer.getGlobalDynamics();
        double newRotation = initialRotation+globalDynamics.getC();
        Matrix22 rotation = rotationMatrix(newRotation);
        velocity = odometer.getVelocity(data);
        Vector2 staticRobotIncrements = odometer.findStaticIncrements(dynamicRobotIncrements);
        Vector2 fieldIncrements = rotation.transform(staticRobotIncrements);

        position = position.add(fieldIncrements);
    }
}

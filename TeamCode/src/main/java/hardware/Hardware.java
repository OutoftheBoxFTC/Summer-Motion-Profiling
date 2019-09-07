package hardware;

import android.support.annotation.NonNull;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;
import org.openftc.revextensions2.RevExtensions2;

import java.util.ArrayList;
import java.util.Map;

import debug.FPSDebug;
import debug.SmartTelemetry;

public class Hardware implements Runnable {
    private static final long MIN_WAIT_TIME=1000000;

    private LinearOpMode opMode;

    private ArrayList<BulkReadData> dataBuffer;
    private ArrayList<double[]> drivePowerBuffer;

    private SmartMotor a, b, c, d;
    private ExpansionHubEx hub;

    private ArrayList<SmartMotor> driveMotors;

    private FPSDebug fpsDebug;

    private BNO055IMU imu;

    private boolean useGyro, dataLogged;

    private SmartTelemetry telemetry;

    private CalibrationData calibration;

    public Hardware(LinearOpMode opmode, SmartTelemetry telemetry){
        this.opMode = opmode;
        driveMotors = new ArrayList<>();
        dataBuffer = new ArrayList<>();
        drivePowerBuffer = new ArrayList<>();
        fpsDebug = new FPSDebug(telemetry, "Hardware");
        this.telemetry = telemetry;
        useGyro = false;
        dataLogged = false;
    }

    public void init(){
        RevExtensions2.init();
        HardwareMap map = opMode.hardwareMap;
        hub = getOrNull(map, ExpansionHubEx.class, "hub");
        a = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "a"));
        b = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "b"));
        c = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "c"));
        d = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "d"));
        imu = getOrNull(map, BNO055IMU.class, "imu");
        if(imu != null) {
            initIMU();
        }
        b.getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
        c.getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
        driveMotors.add(a);
        driveMotors.add(b);
        driveMotors.add(c);
        driveMotors.add(d);
    }

    private void initIMU(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
    }

    public void drive(double a, double b, double c, double d){
        drivePowerBuffer.add(new double[]{a, b, c, d});
    }

    @Override
    public void run() {
        while (!opMode.isStopRequested()){
            long startTime = System.nanoTime();
            fpsDebug.startIncrement();

            boolean drivePowersBuffered = !drivePowerBuffer.isEmpty();
            if(drivePowersBuffered){
                double[] drivePowers = drivePowerBuffer.get(0);
                if(drivePowers != null) {
                    for (int i = 0; i < 4; i++) {
                        driveMotors.get(i).setPower(drivePowers[i]);
                    }
                }
            }
            RevBulkData rawData = hub.getBulkInputData();
            BulkReadData data = new BulkReadData(rawData, calibration);
            if(useGyro || calibration==null){
                data.addGyro(imu);
            }
            if(calibration==null){
                calibration = new CalibrationData(data);
                data = new BulkReadData(rawData, calibration);
            }
            if(drivePowersBuffered){
                //HOPEFULLY by now main loop is waiting for data and not about to send drive powers lol
                drivePowerBuffer.remove(0);
            }
            fpsDebug.endIncrement();
            fpsDebug.update();
            dataBuffer.add(data);
            while (System.nanoTime()-startTime<MIN_WAIT_TIME);
            dataLogged = true;
        }
    }

    public BulkReadData newData(){
        while (!dataLogged);
        dataLogged = false;
        fpsDebug.queryFPS();
        BulkReadData data = dataBuffer.get(dataBuffer.size()-1);
        dataBuffer.remove(dataBuffer.size()-1);
        return data;
    }

    /**
     * Optional due to addition of 3ms delay every hw loop
     */
    public void enableGyro(){
        useGyro = true;
    }

    public void disableGyro(){
        useGyro = false;
    }

    /**
     * Get the value associated with an id and instead of raising an error return null and log it
     *
     * @param map  the hardware map from the HardwareMap
     * @param name The ID in the hardware map
     * @param <T>  the type of hardware map
     * @return the hardware device associated with the name
     */
    public  <T extends HardwareDevice> T getOrNull(@NonNull HardwareMap.DeviceMapping<T> map, String name) {
        for (Map.Entry<String, T> item : map.entrySet()) {
            if (!item.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            telemetry.setHeader(name, "found");
            return item.getValue();
        }
        telemetry.setHeader(name, "not found");
        RobotLog.e("ERROR: " + name + " not found!");
        return null;
    }

    public <T> T getOrNull(@NonNull HardwareMap map, Class<T> type, String name) {
        try {
            T device = map.get(type, name);
            telemetry.setHeader(name, "found");
            return device;
        }
        catch (IllegalArgumentException e){
            telemetry.setHeader(name, "not found");
            RobotLog.e("ERROR: " + name + " not found!");
        }
        return null;
    }

    public BNO055IMU getIMU() {
        return imu;
    }
}
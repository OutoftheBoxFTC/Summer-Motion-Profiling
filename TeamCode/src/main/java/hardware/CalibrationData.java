package hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.revextensions2.RevBulkData;

public class CalibrationData {
    public static final int LEFT = 0, AUX = 1, RIGHT = 2;

    private int leftOffset, auxOffset, rightOffset;
    private double gyroOffset;

    public void addHub1BulkData(RevBulkData data){
        leftOffset = data.getMotorCurrentPosition(LEFT);
        rightOffset = data.getMotorCurrentPosition(RIGHT);
        auxOffset = data.getMotorCurrentPosition(AUX);
    }

    public void addGyroData(BNO055IMU gyro){
        Orientation orientation = gyro.getAngularOrientation();
        double yaw = orientation.firstAngle;
        double tau = Math.PI*2;
        this.gyroOffset = ((yaw%tau)+tau)%tau;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public int getAuxOffset() {
        return auxOffset;
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public double getGyroOffset() {
        return gyroOffset;
    }
}

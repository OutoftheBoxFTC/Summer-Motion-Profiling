package hardware;

public class CalibrationData {
    private int leftOffset, auxOffset, rightOffset;
    private double gyroOffset;
    public CalibrationData(BulkReadData data){
        leftOffset = data.getLeft();
        auxOffset = data.getAux();
        rightOffset = data.getRight();
        gyroOffset = data.getGyro();
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
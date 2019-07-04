package hardware;

import org.openftc.revextensions2.RevBulkData;

public class BulkReadData {
    private static final int LEFT = 0, AUX = 1, RIGHT = 2;
    private double left, right, aux, vLeft, vRight, vAux;

    public BulkReadData(RevBulkData data){
        left = data.getMotorCurrentPosition(LEFT);
        right = data.getMotorCurrentPosition(RIGHT);
        aux = data.getMotorCurrentPosition(AUX);
        vLeft = data.getMotorVelocity(LEFT);
        vRight = data.getMotorVelocity(RIGHT);
        vAux = data.getMotorVelocity(AUX);
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public double getAux() {
        return aux;
    }

    public double getvLeft() {
        return vLeft;
    }

    public double getvRight() {
        return vRight;
    }

    public double getvAux() {
        return vAux;
    }
}

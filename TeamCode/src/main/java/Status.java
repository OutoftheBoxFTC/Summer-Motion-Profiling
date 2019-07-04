import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Status {
    private static LinearOpMode opmode;

    public static LinearOpMode getOpmode() {
        return opmode;
    }

    public void clear(){
        opmode = null;
    }

    public static void setOpmode(LinearOpMode opmode) {
        Status.opmode = opmode;
    }
}
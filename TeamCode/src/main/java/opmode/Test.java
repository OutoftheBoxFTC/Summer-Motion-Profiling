package opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Encoders")
public class Test extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor a = hardwareMap.dcMotor.get("a");
        DcMotor b = hardwareMap.dcMotor.get("b");
        DcMotor c = hardwareMap.dcMotor.get("c");
        DcMotor d = hardwareMap.dcMotor.get("d");
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("A", a.getCurrentPosition());
            telemetry.addData("B", b.getCurrentPosition());
            telemetry.addData("C", c.getCurrentPosition());
            telemetry.addData("D", d.getCurrentPosition());
            telemetry.update();
        }
    }
}
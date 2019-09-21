package opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import debug.SmartTelemetry;

@TeleOp(name = "Test")
public class TestOpmode extends LinearOpMode {
    private SmartTelemetry test;
    @Override
    public void runOpMode() {
        test = new SmartTelemetry(this.telemetry);
        waitForStart();
        while (!isStopRequested()){
            test.setHeader("Controller", gamepad1.a);
            test.update();
        }
    }
}
package motion;

import com.qualcomm.robotcore.hardware.Gamepad;

import drivetrain.MecanumDrive;
import math.Vector3;
import state.StateMachine;

public class DriverControl extends VelocityDriveState {
    private Gamepad driverController;

    public DriverControl(Gamepad driverController, StateMachine stateMachine, MecanumDrive drive) {
        super(stateMachine, drive);
        this.driverController = driverController;
    }

    @Override
    public Vector3 getRobotVelocity() {
        return new Vector3(driverController.right_stick_x, -driverController.right_stick_y, driverController.left_stick_x);
    }
}
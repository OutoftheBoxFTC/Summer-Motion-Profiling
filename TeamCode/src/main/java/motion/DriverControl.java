package motion;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.sun.tools.javac.util.Position;

import math.Matrix22;
import math.Vector2;
import math.Vector3;
import state.StateMachine;

public class DriverControl extends DriveState {
    private Gamepad driverController;

    public DriverControl(Gamepad driverController, StateMachine stateMachine) {
        super(stateMachine);
        this.driverController = driverController;
    }

    @Override
    public Vector3 getRobotVelocity() {
        Log.d("test", "here");
        return new Vector3(driverController.right_stick_x, driverController.right_stick_y, driverController.left_stick_x);
    }
}

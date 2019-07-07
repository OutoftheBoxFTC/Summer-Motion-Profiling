package opmode;

import java.util.HashMap;

import drivetrain.HolonomicDrive;
import hardware.BulkReadData;
import state.LogicState;
import state.StateMachine;
import com.qualcomm.robotcore.eventloop.opmode.*;

@TeleOp(name = "Sensor Test")
public class SensorTest extends BasicOpmode {

    public SensorTest() {
        super(new HolonomicDrive(1), 0, false);
    }

    @Override
    protected void setupStates(StateMachine states) {
        robot.enableGyro();
        HashMap<String, LogicState> stateList = new HashMap<>();
        stateList.put("Sensor Readout", new LogicState(states) {
            @Override
            public void update(BulkReadData data) {
                telemetry.setHeader("left", String.valueOf(data.getLeft()));
                telemetry.setHeader("Right", String.valueOf(data.getRight()));
                telemetry.setHeader("Aux", String.valueOf(data.getAux()));
                telemetry.setHeader("Gyro", String.valueOf(data.getGyro()));
            }
        });
        states.appendLogicStates(stateList);
    }
}
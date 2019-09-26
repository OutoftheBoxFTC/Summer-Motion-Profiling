package opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.HashMap;

import hardware.ReadData;
import math.Vector3;
import state.LogicState;

@TeleOp(name = "Test")
public class TestOpmode extends BasicOpmode {

    public TestOpmode() {
        super(0, true);
    }

    @Override
    protected void setup() {
        telemetry.enableLogger();
        HashMap<String, LogicState> stateList = new HashMap<>();
        stateList.put("gamepad", new LogicState(stateMachine) {
            @Override
            public void update(ReadData data) {
                telemetry.setHeader("Position", new Vector3(gamepad1.leftStickX, gamepad1.leftStickY, gamepad1.rightStickY));
            }
        });
        stateList.put("-1", new LogicState(stateMachine) {

            @Override
            public void update(ReadData data) {
                if(isStarted()){
                    stateMachine.activateLogic("gamepad");
                    deactivateThis();
                }
            }
        });
        stateMachine.appendLogicStates(stateList);
        stateMachine.activateLogic("-1");
    }
}
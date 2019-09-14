package opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.IOException;

import logger.Connector;
import math.Vector3;

public class ConnectionTest extends OpMode {
    Connector connector;
    @Override
    public void init() {
        connector = Connector.getInstance();
        try {
            connector.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connector.addOrientation(new Vector3(1, 2, 3));
        connector.addSensorIO("Lmao");
        connector.addSensorIO("yeet");
        connector.addTelemetry("According");
        connector.addTelemetry("To");
        connector.addTelemetry("All");
        connector.addTelemetry("Known");
        connector.addTelemetry("Laws");
        connector.addTelemetry("Of");
        connector.addTelemetry("Avaiation");
        connector.addTelemetry("There");
    }

    @Override
    public void loop() {
        try {
            connector.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        try {
            connector.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package debug;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import logger.Connector;
import math.Vector3;

public class SmartTelemetry {
    private Telemetry telemetry;
    private ArrayList<String[]> pingMessages;
    private ArrayList<Long> endTimes;
    Connector connector;
    private Map<String, Object> headerMessages;

    public SmartTelemetry(Telemetry telemetry){
        this.telemetry = telemetry;
        headerMessages = new LinkedHashMap<>();
        pingMessages = new ArrayList<>();
        endTimes = new ArrayList<>();
        connector = Connector.getInstance();
        try {
            connector.start();
        } catch (IOException e) {
            RobotLog.e(e.getMessage());
        }
    }

    public void pingMessage(String header, String message, long timeMs){
        pingMessages.add(new String[]{header, message});
        endTimes.add(System.currentTimeMillis()+timeMs);
    }

    public void update(){
        for (int i = 0; i < pingMessages.size();) {
            if(System.currentTimeMillis() > endTimes.get(i)){
                endTimes.remove(i);
                pingMessages.remove(i);
            } else {
                if (pingMessages.get(i).length == 2) {
                    String[] message = pingMessages.get(i);
                    telemetry.addData(message[0], message[1]);
                }
                i++;
            }
        }

        for(String header : headerMessages.keySet()){
            telemetry.addData(header, headerMessages.get(header));
            connector.addTelemetry(header + ": " + headerMessages.get(header));
        }
        telemetry.update();
        try {
            connector.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHeader(String header, Object message){
        headerMessages.put(header, message);
    }

    public void clearHeaders(String... headers){
        for(String header : headers) {
            if(headerMessages.keySet().contains(header)) {
                headerMessages.remove(header);
            }
        }
    }

    public void addOrientation(Vector3 orientation){
        connector.addOrientation(orientation);
    }

    public void clearAllHeadersExcept(String... keep){
        Set<String> headers = headerMessages.keySet();
        ArrayList<String> keepList = new ArrayList<>(Arrays.asList(keep));
        ArrayList<String> removed = new ArrayList<>();
        for (String header : headers) {
            if(!keepList.contains(header)){
               removed.add(header);
            }
        }

        for(String header : removed){
            headerMessages.remove(header);
        }
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }
}

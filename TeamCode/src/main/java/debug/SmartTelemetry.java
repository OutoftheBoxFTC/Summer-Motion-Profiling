package debug;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmartTelemetry {
    private Telemetry telemetry;
    private ArrayList<String[]> pingMessages;
    private ArrayList<Long> endTimes;

    private Map<String, String> headerMessages;

    public SmartTelemetry(Telemetry telemetry){
        this.telemetry = telemetry;
        headerMessages = new HashMap<>();
        pingMessages = new ArrayList<>();
        endTimes = new ArrayList<>();
    }

    public void pingMessage(String header, String message, long timeMs){
        pingMessages.add(new String[]{header, message});
        endTimes.add(System.currentTimeMillis()+timeMs);
    }

    public void update(){
        for (int i = 0; i < pingMessages.size(); i++) {
            if(System.currentTimeMillis()<endTimes.get(i)){
                endTimes.remove(i);
                pingMessages.remove(i);
            }
            else if(pingMessages.get(i).length==2){
                String[] message = pingMessages.get(i);
                telemetry.addData(message[0], message[1]);
            }
        }

        for(String header : headerMessages.keySet()){
            telemetry.addData(header, headerMessages.get(header));
        }
        telemetry.update();
    }

    public void setHeader(String header, String message){
        headerMessages.put(header, message);
    }

    public void clearHeader(String header){
        headerMessages.remove(header);
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }
}

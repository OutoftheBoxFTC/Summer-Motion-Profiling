package debug;

import java.util.ArrayList;

public class FPSDebug {
    private ArrayList<Long> timeIncrements, timeStamps;
    private SmartTelemetry telemetry;
    private long lastTime = 0;
    private String loopName;

    public FPSDebug(SmartTelemetry telemetry, String loopName){
        timeIncrements = new ArrayList<>();
        timeStamps = new ArrayList<>();
        this.telemetry = telemetry;
        this.loopName = loopName;
    }

    public void update() {
        long now = timeStamps.get(0);
        while (now - timeStamps.get(0)>1000){
            timeIncrements.remove(0);
            timeStamps.remove(0);
        }
    }

    public void queryFPS(){
        double fps = 0,
        factor = 1000.0/timeIncrements.size();
        for (long timeIncrement : timeIncrements) {
            fps += factor/timeIncrement;
        }
        telemetry.setHeader(loopName + " FPS", String.valueOf(fps));
    }

    public void startIncrement() {
        if(lastTime==0) {
            lastTime = System.currentTimeMillis();
            timeStamps.add(lastTime);
        }
    }

    public void endIncrement(){
        if(lastTime==0)
            return;
        timeIncrements.add(System.currentTimeMillis()-lastTime);
        lastTime = 0;
    }
}
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
        while (now - timeStamps.get(0)>1e9){
            timeIncrements.remove(0);
            timeStamps.remove(0);
        }
    }

    public void queryFPS(){
        double fps = 0, fpsFactor = 1.0e9/timeIncrements.size(), time=0, timeFactor = 1/1.0e9/timeIncrements.size();
        for (long timeIncrement : timeIncrements) {
            fps += fpsFactor/timeIncrement;
            time += timeIncrement*timeFactor;
        }
        telemetry.setHeader(loopName + " FPS", String.valueOf((int)fps));
        telemetry.setHeader(loopName + "Duration", String.valueOf(time));
    }

    public void startIncrement() {
        if(lastTime==0) {
            lastTime = System.nanoTime();
        }
    }

    public void endIncrement(){
        if(lastTime==0)
            return;
        timeIncrements.add(System.nanoTime()-lastTime);
        timeStamps.add(lastTime);
        lastTime = 0;
    }
}
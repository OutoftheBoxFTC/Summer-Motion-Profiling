import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;
import org.openftc.revextensions2.RevExtensions2;

import java.util.ArrayList;
import java.util.Map;

public class Hardware implements Runnable {
    private LinearOpMode opMode;

    private ArrayList<RevBulkData> buffer;

    private SmartMotor a, b, c, d;
    private ExpansionHubEx hub1, hub2;

    private ArrayList<SmartMotor> driveMotors;


    public Hardware(LinearOpMode opmode){
        this.opMode = opmode;
        buffer = new ArrayList<>();
    }

    public void init(){
        RevExtensions2.init();
        HardwareMap map = opMode.hardwareMap;
        hub1 = getOrNull(map, ExpansionHubEx.class, "hub1");
        hub2 = getOrNull(map, ExpansionHubEx.class, "hub2");
        a = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "a"));
        b = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "b"));
        c = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "c"));
        d = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "d"));

        driveMotors.add(a);
        driveMotors.add(b);
        driveMotors.add(c);
        driveMotors.add(d);
    }

    @Override
    public void run() {
        while (opMode.opModeIsActive()){
            for(SmartMotor motor : driveMotors){
                motor.flushPower();
            }
            for(SmartMotor motor : driveMotors){
                motor.flushStates();
            }
            buffer.add(hub1.getBulkInputData());
        }
    }

    public RevBulkData newData(){
        while (buffer.isEmpty());
        RevBulkData data = buffer.get(buffer.size()-1);
        buffer.remove(buffer.size()-1);
        return data;
    }

    /**
     * Get the value associated with an id and instead of raising an error return null and log it
     *
     * @param map  the hardware map from the HardwareMap
     * @param name The ID in the hardware map
     * @param <T>  the type of hardware map
     * @return the hardware device associated with the name
     */
    protected <T extends HardwareDevice> T getOrNull(@NonNull HardwareMap.DeviceMapping<T> map, String name) {
        for (Map.Entry<String, T> item : map.entrySet()) {
            if (!item.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return item.getValue();
        }
        RobotLog.e("ERROR: " + name + " not found!");
        return null;
    }

    protected <T> T getOrNull(@NonNull HardwareMap map, Class<T> type, String name) {
        try {
            T device = map.get(type, name);
            opMode.telemetry.addData("Device", device);
            return map.get(type, name);
        }
        catch (IllegalArgumentException e){
            RobotLog.e("ERROR: " + name + " not found!");
        }
        return null;
    }
}
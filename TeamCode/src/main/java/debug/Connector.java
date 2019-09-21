package debug;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import debug.JsonFormat.Coordinates;
import debug.JsonFormat.RootObject;
import debug.JsonFormat.SensorIO;
import debug.JsonFormat.TelemetryData;
import math.Vector2;
import math.Vector3;

public class Connector {
    private static Connector instance = new Connector();
    DatagramSocket socket;
    Vector3 orientation = Vector3.ZERO();
    List<String> telemetry;
    List<String> sensorIO;
    RootObject rootObject = new RootObject(new Coordinates(), new SensorIO(), new TelemetryData());
    OutputStream stream;
    DatagramPacket packet;
    private Connector(){ }

    public static Connector getInstance(){
        return instance;
    }

    public void start() throws IOException {
        telemetry = new ArrayList<String>();
        sensorIO = new ArrayList<String>();
        socket = new DatagramSocket(1119);
        socket.setReuseAddress(true);
        socket.setBroadcast(true);
    }

    public void addTelemetry(String data){
        telemetry.add(data);
    }

    public void addSensorIO(String data){
        sensorIO.add(data);
    }

    public void addOrientation(Vector2 position, double rotation){
        orientation = new Vector3(position.getA(), position.getB(), rotation);
    }

    public void addOrientation(Vector3 position){
        orientation = position;
    }

    public void update() throws IOException {
        rootObject.TelemetryData.Data = telemetry;
        rootObject.SensorIO.data = sensorIO;
        rootObject.Coordinates.x = orientation.getA();
        rootObject.Coordinates.y = orientation.getB();
        rootObject.Coordinates.rot = orientation.getC();
        String json = SimpleGson.getInstance().toJson(rootObject);
        String test = telemetry.get(0);
        for(int i = 1; i < telemetry.size(); i ++){
            test += ", ";
            test += telemetry.get(i);
        }
        byte[] toSend = json.getBytes();
        int sendLen = toSend.length;
        for(int i = 100; i <= 149; i ++) {
            packet = new DatagramPacket(toSend, sendLen, InetAddress.getByName("192.168.1." + i), 1119);
            socket.send(packet);
            RobotLog.i(packet.getAddress().toString());
        }
    }

    public void end() throws IOException {
        stream.flush();
        stream.close();
        socket.close();
    }
}

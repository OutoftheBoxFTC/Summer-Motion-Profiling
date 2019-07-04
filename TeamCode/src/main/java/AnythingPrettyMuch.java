import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class AnythingPrettyMuch extends LinearOpMode{
    protected Vector2 position;
    protected double initialRotation;
    private Vector3 velocity;
    protected OdometryCalculator odometryCalculator;
    protected Odometer odometer;
    protected Hardware robot;
    protected FPSDebug fpsDebug;
    protected SmartTelemetry telemetry;

    public AnythingPrettyMuch(OdometryCalculator calculator){
        //TODO set
        odometer = new Odometer(0, 0, 0);
        this.odometryCalculator = calculator;
        this.telemetry = new SmartTelemetry(super.telemetry);
        fpsDebug = new FPSDebug(telemetry, "Main Loop");
    }

    public void update(){
        Data data = robot.newData();//stalls here until hardware loop obtains new data
        fpsDebug.startIncrement();
        updateOrientation(data);
        fpsDebug.endIncrement();
        fpsDebug.update();
        fpsDebug.queryFPS();
    }

    private Matrix22 rotationMatrix(double rotation){
        double cos = Math.cos(rotation),
                sine = Math.sin(rotation);
        return new Matrix22(cos, sine, sine, cos);
    }

    protected void setStartingOrientation(Vector3 initialOrientation){
        this.position = new Vector2(initialOrientation.getA(), initialOrientation.getB());
        this.initialRotation = initialOrientation.getC();
        this.velocity = new Vector3(0, 0, 0);//hopefully
    }

    private void updateOrientation(Data data){
        Vector3 dynamicRobotIncrements = odometer.updatePosition(data.getLeft(), data.getRight(), data.getAux());
        double newRotation = odometer.getGlobalRotation()+initialRotation;

        velocity = odometer.getVelocity(data.getvLeft(), data.getvRight(), data.getvAux());

        Matrix22 rotation = rotationMatrix(newRotation);
        Vector2 staticRobotIncrements = odometryCalculator.findStaticIncrement(dynamicRobotIncrements);
        Vector2 fieldIncrements = rotation.transform(staticRobotIncrements);

        position = position.add(fieldIncrements);
    }
}

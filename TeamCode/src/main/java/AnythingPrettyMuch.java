public class AnythingPrettyMuch {
    private Vector2 position;
    private double initialRotation;
    private OdometryCalculator odometryCalculator;
    private Odometer odometer;
    private Hardware robot;

    public void loop(){
        Data data = robot.newData();//stalls here until hardware loop obtains new data

        Vector3 dynamicRobotIncrements = odometer.updatePosition(data.getLeft(), data.getRight(), data.getAux());
        double newRotation = odometer.getGlobalRotation()+initialRotation;

        Vector3 velocity = odometer.getVelocity(data.getvLeft(), data.getvRight(), data.getvAux());

        Matrix22 rotation = rotationMatrix(newRotation);
        Vector2 staticRobotIncrements = odometryCalculator.findStaticIncrement(dynamicRobotIncrements);
        Vector2 fieldIncrements = rotation.transform(staticRobotIncrements);

        position = position.add(fieldIncrements);


    }

    private Matrix22 rotationMatrix(double rotation){
        double cos = Math.cos(rotation),
                sine = Math.sin(rotation);
        return new Matrix22(cos, sine, sine, cos);
    }

    protected void setStartingOrientation(Vector3 initialOrientation){
        this.position = new Vector2(initialOrientation.getA(), initialOrientation.getB());
        this.initialRotation = initialOrientation.getC();
    }
}

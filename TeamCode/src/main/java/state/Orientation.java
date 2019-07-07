package state;

import hardware.BulkReadData;
import math.Matrix22;
import math.Vector2;
import math.Vector3;
import odometry.Odometer;
import odometry.SimpleOdometerDynamics;

public class Orientation extends LogicState{
    private Vector3 position, velocity;
    private Odometer odometer;

    private double initialRotation;

    public Orientation(StateMachine stateMachine, Odometer odometer, Vector3 position, Vector3 velocity) {
        this.odometer = odometer;
        this.position = position;
        this.velocity = velocity;
        initialRotation = position.getC();
    }

    @Override
    public void update(BulkReadData data) {
        velocity.set(odometer.getVelocity(data));

        SimpleOdometerDynamics dynamicRobotIncrements = odometer.updateRobotDynamics(data);
        Vector3 globalDynamics = odometer.getGlobalDynamics();

        double newRotation = initialRotation+globalDynamics.getC();
        Matrix22 rotation = rotationMatrix(newRotation);

        Vector2 staticRobotIncrements = odometer.findStaticIncrements(dynamicRobotIncrements);
        Vector2 fieldIncrements = rotation.transform(staticRobotIncrements);
        position.set(position.add(new Vector3(fieldIncrements.getA(), fieldIncrements.getB(), 0)));
        position.setC(newRotation);
    }

    private Matrix22 rotationMatrix(double rotation){
        double cos = Math.cos(rotation),
                sine = Math.sin(rotation);
        return new Matrix22(cos, sine, sine, cos);
    }
}

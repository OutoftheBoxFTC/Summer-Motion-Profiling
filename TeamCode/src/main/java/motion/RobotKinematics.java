package motion;

import math.Vector3;
import math.Vector4;

public abstract class RobotKinematics {


    /**
     *
     * @param velocity in strafe, fwd, and rotation
     * @return velocities of motors a, b, c, d
     */
    public abstract Vector4 getWheelVelocities(Vector3 velocity);

    public abstract  Vector3 getRobotVelocity(Vector4 wheels);


}

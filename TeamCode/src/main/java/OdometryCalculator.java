public class OdometryCalculator {
    public Vector2 findStaticIncrement(Vector3 robotIncrements){
        double strafe = robotIncrements.getA(),
                fwd = robotIncrements.getB(),
                rotation = robotIncrements.getC();
        double cos = Math.cos(rotation), sine = Math.sin(rotation);

        double y = fwd*sine-strafe*(cos+1);
        double x = strafe*sine-fwd*(cos+1);
        return new Vector2(x, y);
    }
}
public class OdometryCalculator {
    public Point findIncrement(double fwd, double strafe, double rotation){
        double cos = Math.cos(rotation), sine = Math.sin(rotation);
        double y = fwd*sine-strafe*(cos+1);
        double x = strafe*sine-fwd*(cos+1);
        return new Point(x, y);
    }
}

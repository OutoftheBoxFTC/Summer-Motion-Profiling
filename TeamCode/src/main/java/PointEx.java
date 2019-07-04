public class PointEx extends Point {
    private double r;
    public PointEx(double x, double y, double r){
        super(x, y);
        this.r =r;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}

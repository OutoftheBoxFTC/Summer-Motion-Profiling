public class Vector2 {
    private double x, y;
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2 scale(double scalar){
        return new Vector2(x*scalar, y*scalar);
    }

    public Vector2 add(Vector2 v){
        return new Vector2(v.x+x, v.y+y);
    }
}
package math;

public class Vector2 {
    private double a, b;
    public Vector2(double a, double b){
        this.a = a;
        this.b = b;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public Vector2 scale(double scalar){
        return new Vector2(a *scalar, b *scalar);
    }

    public Vector2 add(Vector2 v){
        return new Vector2(v.a + a, v.b + b);
    }

    public Vector2 clone(){
        return new Vector2(a, b);
    }
}
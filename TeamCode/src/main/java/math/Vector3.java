package math;

public class Vector3 {
    private double a, b, c;
    public Vector3(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void set(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Vector3 clone(){
        return new Vector3(a, b, c);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(a-v.a, b-v.b, c-v.c);
    }

    public Vector3 add(Vector3 v){
        return new Vector3(v.a+a, v.b+b, v.c+c);
    }

    public Vector3 scale(double scalar){
        return new Vector3(a*scalar, b*scalar, c*scalar);
    }

    public Vector3 dot(Vector3 v) {
        return new Vector3(v.a*a, v.b*b, v.c*c);
    }

    public void set(Vector3 v) {
        a = v.a;
        b = v.b;
        c = v.c;
    }
}
public class Matrix22 {
    private Vector2 i, j;
    public Matrix22(double a, double b, double c, double d){
        i = new Vector2(a, c);
        j = new Vector2(b, d);
    }

    public Matrix22(Vector2 i, Vector2 j){
        this.i = i;
        this.j = j;
    }

    public Vector2 transform(Vector2 v){
        return i.scale(v.getA()).add(j.scale(v.getB()));
    }
}

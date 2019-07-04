public class Matrix44 {
    private Vector4 i, j, k, l;
    public Matrix44(Vector4 i, Vector4 j, Vector4 k, Vector4 l){
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
    }

    public Matrix44(double... params){
        if(params.length==16) {
            i = new Vector4(params[0], params[4], params[8], params[12]);
            j = new Vector4(params[1], params[5], params[9], params[13]);
            k = new Vector4(params[2], params[6], params[10], params[14]);
            l = new Vector4(params[3], params[7], params[11], params[15]);
        }
    }

    public Vector4 transform(Vector4 v){
        return i.scale(v.getA()).add(j.scale(v.getB())).add(k.scale(v.getC())).add(l.scale(v.getD()));
    }
}

package sensors;

import drivers.Pixycam;

public class PixycamProvider {
    Pixycam pixy;
    int[][] data;
    public PixycamProvider(Pixycam pixy, int registers){
        this.pixy = pixy;
        data = new int[registers][4];
    }

    public void update(){
        int current = pixy.getSigNum();
        data[current][0] = pixy.getX();
        data[current][1] = pixy.getY();
        data[current][2] = pixy.getWidth();
        data[current][3] = pixy.getHeight();
    }

    public int getX(int register){
        return data[register][0];
    }

    public int getY(int register){
        return data[register][1];
    }

    public int getWidth(int register){
        return data[register][2];
    }

    public int getHeight(int register){
        return data[register][3];
    }
}

package main;

public class Vector2 {
    public int x = 0, y = 0;


    public Vector2(){
        this(0, 0);
    }

    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Vector2(Vector2 v){
        this.x = v.x;
        this.y = v.y;
    }

    public boolean isSamePos(Vector2 v){
        return this.x == v.x && this.y == v.y;
    }

    public Vector2 normalized(){
        int m = (int)Math.sqrt(x*x + y*y);
        return new Vector2(x/m, y/m);
    }

    public int distanceTo(Vector2 v){
        return (int)Math.sqrt(Math.pow(v.x - this.x, 2) + Math.pow(v.y - this.y, 2));
    }

    public Vector2 dirTo(Vector2 v){
        return new Vector2(v.x - this.x, v.y - this.y).normalized();
    }

    @Override
    public String toString(){
        return "" + x + ", " + y;
    }

}

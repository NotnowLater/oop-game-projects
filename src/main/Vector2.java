package main;

public class Vector2 {
    private int x = 0;
    private int y = 0;

    public Vector2(){
        this(0, 0);
    }

    public Vector2(int x, int y){
        this.setX(x);
        this.setY(y);
    }
    public Vector2(Vector2 v){
        this.setX(v.getX());
        this.setY(v.getY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isSamePos(Vector2 v){
        return this.getX() == v.getX() && this.getY() == v.getY();
    }

    public Vector2 normalized(){
        int m = (int)Math.sqrt(getX() * getX() + getY() * getY());
        return new Vector2(getX() /m, getY() /m);
    }

    public int distanceTo(Vector2 v){
        return (int)Math.sqrt(Math.pow(v.getX() - this.getX(), 2) + Math.pow(v.getY() - this.getY(), 2));
    }

    public Vector2 dirTo(Vector2 v){
        return new Vector2(v.getX() - this.getX(), v.getY() - this.getY()).normalized();
    }

    @Override
    public String toString(){
        return "" + getX() + ", " + getY();
    }

}

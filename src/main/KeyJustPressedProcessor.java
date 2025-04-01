package main;

public class KeyJustPressedProcessor implements Processable{

    private int state;
    private boolean pressed;
    private boolean justPressed;

    @Override
    public void process() {
        if (isPressed()){
            if (getState() == 0){
                setJustPressed(true);
                setState(1);
            } else if (getState() == 1){
                setJustPressed(false);
            }
        } else {
            setState(0);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isJustPressed() {
        return justPressed;
    }

    public void setJustPressed(boolean justPressed) {
        this.justPressed = justPressed;
    }
}

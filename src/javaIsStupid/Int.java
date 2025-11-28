package javaIsStupid;

public class Int {
    private int value;

    public Int(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addToValue(int value) {
        this.value += value;
    }
}

package example;

public class Cell {
    private String value;

    public static final String X_VALUE = "X";
    public static final String O_VALUE = "O";
    public static final String EMPTY_VALUE = "";

    public Cell() {
        this.value = EMPTY_VALUE;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

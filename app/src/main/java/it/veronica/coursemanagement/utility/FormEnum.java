package it.veronica.coursemanagement.utility;

public enum FormEnum {
    CREATION(1),
    DETAIL(2),
    MODIFY(3),
    DELETE(4);

    private final int value;
    private FormEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
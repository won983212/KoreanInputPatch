package com.won983212.kpatch;

public interface IInputWrapper {
    void setValue(String text);

    String getValue();

    int getAnchorCursor();

    void setAnchorCursor(int cursor);

    int getMovingCursor();

    void setMovingCursor(int cursor);

    void write(String text);

    void delete(int delta);

    void cancelAllInputContext();
}
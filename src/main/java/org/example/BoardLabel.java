package org.example;

import javafx.scene.control.Label;

public class BoardLabel extends Label {

    private final int i;
    private final int j;

    public BoardLabel(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

}
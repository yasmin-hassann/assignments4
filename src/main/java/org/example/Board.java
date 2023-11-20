package org.example;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Board extends Group {
    private final Game game;
    private final int size;
    private final BoardLabel[][] board;

    private int checked_count = 0;

    Board(int size, Game game) {
        board = new BoardLabel[size][size];
        this.size = size;
        this.game = game;


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new BoardLabel(i, j);
                board[i][j].setStyle("-fx-border-color: #000000; -fx-border-width: 1px; -fx-background-color: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center; -fx-padding: 10px;");
                board[i][j].setPrefSize(50, 50);
                board[i][j].setTranslateX(50 * j);
                board[i][j].setTranslateY(50 * i);
                board[i][j].setOnMouseClicked(e -> clickLable((BoardLabel) e.getSource()));
                getChildren().add(board[i][j]);
            }
        }
    }

    public void clickLable(BoardLabel label) {

        if (label.getText().equals("S") || label.getText().equals("O")) {
            return;
        }

        game.updateMode();

        if (game.getIsSSelected()) {
            label.setText("S");
        } else {
            label.setText("O");
        }

        checkSOS(label);
        checked_count++;
        game.alterTurn();

    }

    private void checkSOS(BoardLabel label) {

        int i = label.getI();
        int j = label.getJ();


        if (label.getText().equals("S")) {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (isLabelO(i + k, j + l) && isLabelS(i + 2 * k, j + 2 * l)) {
                        addLine(i, j, i + 2 * k, j + 2 * l);
                        game.incrementScore();
                    }
                }
            }
        } else if (label.getText().equals("O")) {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (isLabelS(i + k, j + l) && isLabelS(i - k, j - l)) {
                        addLine(i + k, j + l, i - k, j - l);
                        game.incrementScore();
                    }
                }
            }

        }

    }

    public boolean isGameOver() {
        return checked_count == size * size;
    }

    private void addLine(int i, int j, int i1, int i2) {
        Line line = new Line();

        Label label1 = board[i][j];
        Label label2 = board[i1][i2];

        line.setStartX(label1.getTranslateX() + 25);
        line.setStartY(label1.getTranslateY() + 25);

        line.setEndX(label2.getTranslateX() + 25);
        line.setEndY(label2.getTranslateY() + 25);

        if (game.isBluePlayerTurn())
            line.setStroke(Color.BLUE);
        else
            line.setStroke(Color.RED);

        getChildren().add(line);
    }

    private boolean isLabelO(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            return false;
        }

        return board[i][j].getText().equals("O");
    }

    private boolean isLabelS(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            return false;
        }

        return board[i][j].getText().equals("S");
    }


    char[][] getBoardState() {
        char[][] boardStates = new char[getBoardSize()][getBoardSize()];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                String s = board[i][j].getText();

                if (s.length() > 0)
                    boardStates[i][j] = s.charAt(0);
                else
                    boardStates[i][j] = 'x';
            }
        return boardStates;
    }

    BoardLabel getBoardLabel(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return null;
        }

        return board[row][col];

    }


    public int getBoardSize() {
        return size;
    }

    /*
    private final Game game;
    private final int size;
    private final BoardLabel[][] board;

    private int checked_count = 0;


    public Board(int size, Game game) {
        board = new BoardLabel[size][size];
        this.size = size;
        this.game = game;


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new BoardLabel(i, j);
                board[i][j].setStyle("-fx-border-color: #000000; -fx-border-width: 1px; -fx-background-color: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center; -fx-padding: 10px;");
                board[i][j].setPrefSize(50, 50);
                board[i][j].setTranslateX(50 * j);
                board[i][j].setTranslateY(50 * i);
                board[i][j].setOnMouseClicked(e -> clickLable((BoardLabel) e.getSource()));
                getChildren().add(board[i][j]);
            }
        }
    }

    public void clickLable(BoardLabel label) {

        if (label.getText().equals("S") || label.getText().equals("O")) {
            return;
        }

        game.updateMode();

        if (game.getIsSSelected()) {
            label.setText("S");
        } else {
            label.setText("O");
        }

        checkSOS(label);
        checked_count++;
        game.alterTurn();

    }

    private void checkSOS(BoardLabel label) {

        int i = label.getI();
        int j = label.getJ();


        if (label.getText().equals("S")) {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (isLabelO(i + k, j + l) && isLabelS(i + 2 * k, j + 2 * l)) {
                        addLine(i, j, i + 2 * k, j + 2 * l);
                        game.incrementScore();

                    }
                }
            }
        } else if (label.getText().equals("O")) {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (isLabelS(i + k, j + l) && isLabelS(i - k, j - l)) {
                        addLine(i + k, j + l, i - k, j - l);
                        game.incrementScore();

                    }
                }
            }

        }

    }

    private void addLine(int i, int j, int i1, int j1) {
        Line line = new Line();

        Label label1 = board[i][j];
        Label label2 = board[i1][j1];

        line.setStartX(label1.getTranslateX() + 25);
        line.setStartY(label1.getTranslateY() + 25);

        line.setEndX(label2.getTranslateX() + 25);
        line.setEndY(label2.getTranslateY() + 25);

        if (game.isBluePlayerTurn())
            line.setStroke(Color.BLUE);
        else
            line.setStroke(Color.RED);

        getChildren().add(line);
    }

    private boolean isLabelO(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            return false;
        }

        return board[i][j].getText().equals("O");
    }

    private boolean isLabelS(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            return false;
        }

        return board[i][j].getText().equals("S");
    }


    char[][] getBoardState() {
        char[][] boardStates = new char[getBoardSize()][getBoardSize()];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                String s = board[i][j].getText();

                if (s.length() > 0)
                    boardStates[i][j] = s.charAt(0);
                else
                    boardStates[i][j] = 'x';
            }
        return boardStates;
    }

    BoardLabel getBoardLabel(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return null;
        }

        return board[row][col];

    }


    public int getBoardSize() {
        return size;
    }


    public boolean isBoardFull() {
        return checked_count == size * size;
    }

*/
}




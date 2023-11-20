package org.example;
/*
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserPanel extends VBox {
    private final Game game;
    private final CheckBox choiceBoxS;
    private final CheckBox choiceBoxO;
    private final RadioButton human;
    private final RadioButton computer;
    private int score = 0;
    private final Label scoreLabel;

    private boolean isModeS = true;
    private boolean isComputer = false;

    public UserPanel(String name, Game game) {
        super();
        this.game = game;
        score = 0;
        choiceBoxS = new CheckBox("S");
        choiceBoxO = new CheckBox("O");
        human = new RadioButton("Human");
        computer = new RadioButton("Computer");

        setStyle("-fx-border-color: #000000; -fx-padding: 10px;");

        Text nameText = new Text(name);
        nameText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        getChildren().add(nameText);

        //add vertical space
        getChildren().add(new Label(" "));

        getChildren().add(human);

        HBox tmpHbox = new HBox();
        tmpHbox.getChildren().add(new Label("     "));
        tmpHbox.getChildren().add(choiceBoxS);
        getChildren().add(tmpHbox);
        tmpHbox = new HBox();
        tmpHbox.getChildren().add(new Label("     "));
        tmpHbox.getChildren().add(choiceBoxO);
        getChildren().add(tmpHbox);

        //add vertical space
        getChildren().add(new Label(" "));
        getChildren().add(computer);
        //add vertical space
        getChildren().add(new Label(" "));
        getChildren().add(new Label(" "));

        scoreLabel = new Label("Score: " + score);
        getChildren().add(scoreLabel);

        choiceBoxO.setOnAction(this::onChoiceBoxO);
        choiceBoxS.setOnAction(this::onChoiceBoxS);

        human.setOnAction(this::onPlayerChange);
        computer.setOnAction(this::onPlayerChange);

        human.setSelected(true);

        choiceBoxS.setSelected(true);
        choiceBoxO.setSelected(false);
    }

    private void onPlayerChange(ActionEvent actionEvent) {

        if (actionEvent.getSource().equals(human)) {
            human.setSelected(true);
            computer.setSelected(false);
            isComputer = false;
        } else {
            human.setSelected(false);
            computer.setSelected(true);
            isComputer = true;
        }
    }


    public boolean isComputerSelected() {

        return isComputer;
    }

    private void onChoiceBoxS(ActionEvent actionEvent) {
        choiceBoxS.setSelected(true);
        choiceBoxO.setSelected(false);
        isModeS = true;
    }

    private void onChoiceBoxO(ActionEvent actionEvent) {
        choiceBoxS.setSelected(false);
        choiceBoxO.setSelected(true);
        isModeS = false;
    }

    public boolean isModeS() {
        return isModeS;
    }

    public void incrementScore() {
        score++;
        scoreLabel.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }

    public void resetBackground() {
        setStyle("-fx-border-color: #000000; -fx-padding: 10px;");
    }

    public void setActivatedBackground() {
        setStyle("-fx-border-color: #000000; -fx-padding: 10px; -fx-background-color: #00ff00;");
    }

    public void resetScore() {
        score = 0;
        scoreLabel.setText("Score: " + score);
        isModeS = true;
        choiceBoxS.setSelected(true);
        choiceBoxO.setSelected(false);
    }

    public void playNextMove(Board board) {

        char[][] boardState = board.getBoardState();
        Move mtoPlay = getHighestScoredMove(boardState);

        if (mtoPlay.score <= 0) {
            mtoPlay = getLowestScoredMove(boardState);
        }

        if (mtoPlay.moveToPlay == 'S') {
            this.isModeS = true;
            this.choiceBoxS.setSelected(true);
            this.choiceBoxO.setSelected(false);
        } else {
            this.isModeS = false;
            this.choiceBoxS.setSelected(false);
            this.choiceBoxO.setSelected(true);
        }
        BoardLabel lb = board.getBoardLabel(mtoPlay.row, mtoPlay.col);
        board.clickLable(lb);

    }

    private Move getLowestScoredMove(char[][] boardState) {
        Move m = new Move();
        m.score = 1000000000;
        int size = boardState.length;

        //put 'S' or 'O' in the cells that are empty (marked with 'x')
        //calculate new SOS count (score) after putting the 's' or 'o'
        //return the move that produced lowest score on the next move
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardState[i][j] == 'x') {

                    boardState[i][j] = 'S';
                    Move nextMove = getHighestScoredMove(boardState);
                    int score = nextMove.score;

                    if (score < m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'S';
                    }

                    boardState[i][j] = 'O';
                    nextMove = getHighestScoredMove(boardState);
                    score = nextMove.score;

                    if (score < m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'O';
                    }

                    boardState[i][j] = 'x';
                }
            }
        }

        return m;
    }


    private Move getHighestScoredMove(char[][] boardState) {
        Move m = new Move();
        int size = boardState.length;

        //put 'S' or 'O' in the cells that are empty (marked with 'x')
        //calculate new SOS count (score) after putting the 's' or 'o'
        //return the move that produced highest score
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardState[i][j] == 'x') {
                    boardState[i][j] = 'S';
                    int score = checkSOS(boardState, i, j);

                    if (score > m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'S';
                    }

                    boardState[i][j] = 'O';

                    score = checkSOS(boardState, i, j);

                    if (score > m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'O';
                    }

                    boardState[i][j] = 'x';
                }
            }
        }

        return m;
    }

    private boolean checkSO(char[][] board, int row, int col, char ch) {
        if (row < 0 || row >= board.length || col < 0 || col >= board.length) {
            return false;
        }
        return board[row][col] == ch;
    }

    private int checkSOS(char[][] board, int row, int col) {

        int score = 0;

        if (board[row][col] == 'S') {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (checkSO(board, row + k, col + l, 'O') && checkSO(board, row + 2 * k, col + 2 * l, 'S')) {
                        score++;
                    }
                }
            }
        } else {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (checkSO(board, row + k, col + l, 'S') && checkSO(board, row - k, col - l, 'S')) {
                        score++;
                    }
                }
            }

        }

        return score;
    }

    private class Move {
        int row, col;
        int score;
        char moveToPlay;

        Move() {
            row = col = score = -1;
            moveToPlay = 'x';
        }
    }
}
*/


import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserPanel extends VBox {
    private final Game game;
    private final CheckBox choiceBoxS;
    private final CheckBox choiceBoxO;
    private final RadioButton human;
    private final RadioButton computer;
    private int score;
    private final Label scoreLabel;

    private boolean isModeS = true;
    private boolean isComputer = false;

    public UserPanel(String name, Game game) {
        super();
        this.game = game;
        score = 0;
        choiceBoxS = new CheckBox("S");
        choiceBoxO = new CheckBox("O");
        human = new RadioButton("Human");
        computer = new RadioButton("Computer");

        setStyle("-fx-border-color: #000000; -fx-padding: 10px;");

        Text nameText = new Text(name);
        nameText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        getChildren().add(nameText);

        //add vertical space
        getChildren().add(new Label(" "));

        getChildren().add(human);

        HBox tmpHbox = new HBox();
        tmpHbox.getChildren().add(new Label("     "));
        tmpHbox.getChildren().add(choiceBoxS);
        getChildren().add(tmpHbox);
        tmpHbox = new HBox();
        tmpHbox.getChildren().add(new Label("     "));
        tmpHbox.getChildren().add(choiceBoxO);
        getChildren().add(tmpHbox);

        //add vertical space
        getChildren().add(new Label(" "));
        getChildren().add(computer);
        //add vertical space
        getChildren().add(new Label(" "));
        getChildren().add(new Label(" "));

        scoreLabel = new Label("Score: " + score);
        getChildren().add(scoreLabel);

        choiceBoxO.setOnAction(this::onChoiceBoxO);
        choiceBoxS.setOnAction(this::onChoiceBoxS);

        human.setOnAction(this::onPlayerChange);
        computer.setOnAction(this::onPlayerChange);

        human.setSelected(true);

        choiceBoxS.setSelected(true);
        choiceBoxO.setSelected(false);
    }

    private void onPlayerChange(ActionEvent actionEvent) {

        if (actionEvent.getSource().equals(human)) {
            human.setSelected(true);
            computer.setSelected(false);
            isComputer = false;
        } else {
            human.setSelected(false);
            computer.setSelected(true);
            isComputer = true;
        }
    }


    public boolean isComputerSelected() {

        return isComputer;
    }

    private void onChoiceBoxS(ActionEvent actionEvent) {
        choiceBoxS.setSelected(true);
        choiceBoxO.setSelected(false);
        isModeS = true;
    }

    private void onChoiceBoxO(ActionEvent actionEvent) {
        choiceBoxS.setSelected(false);
        choiceBoxO.setSelected(true);
        isModeS = false;
    }

    public boolean isModeS() {
        return isModeS;
    }

    public void incrementScore() {
        score++;
        scoreLabel.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }

    public void resetBackground() {
        setStyle("-fx-border-color: #000000; -fx-padding: 10px;");
    }

    public void setActivatedBackground() {
        setStyle("-fx-border-color: #000000; -fx-padding: 10px; -fx-background-color: #00ff00;");
    }

    public void resetScore() {
        score = 0;
        scoreLabel.setText("Score: " + score);
        isModeS = true;
        choiceBoxS.setSelected(true);
        choiceBoxO.setSelected(false);
    }

    public void playNextMove(Board board) {

        char[][] boardState = board.getBoardState();
        Move mtoPlay = getHighestScoredMove(boardState);

        if (mtoPlay.score <= 0) {
            mtoPlay = getLowestScoredMove(boardState);
        }

        if (mtoPlay.moveToPlay == 'S') {
            this.isModeS = true;
            this.choiceBoxS.setSelected(true);
            this.choiceBoxO.setSelected(false);
        } else {
            this.isModeS = false;
            this.choiceBoxS.setSelected(false);
            this.choiceBoxO.setSelected(true);
        }
        BoardLabel lb = board.getBoardLabel(mtoPlay.row, mtoPlay.col);
        board.clickLable(lb);

    }

    private Move getLowestScoredMove(char[][] boardState) {
        Move m = new Move();
        m.score = 1000000000;
        int size = boardState.length;

        //put 'S' or 'O' in the cells that are empty (marked with 'x')
        //calculate new SOS count (score) after putting the 's' or 'o'
        //return the move that produced lowest score on the next move
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardState[i][j] == 'x') {

                    boardState[i][j] = 'S';
                    Move nextMove = getHighestScoredMove(boardState);
                    int score = nextMove.score;

                    if (score < m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'S';
                    }

                    boardState[i][j] = 'O';
                    nextMove = getHighestScoredMove(boardState);
                    score = nextMove.score;

                    if (score < m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'O';
                    }

                    boardState[i][j] = 'x';
                }
            }
        }

        return m;
    }


    private Move getHighestScoredMove(char[][] boardState) {
        Move m = new Move();
        int size = boardState.length;

        //put 'S' or 'O' in the cells that are empty (marked with 'x')
        //calculate new SOS count (score) after putting the 's' or 'o'
        //return the move that produced highest score
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardState[i][j] == 'x') {
                    boardState[i][j] = 'S';
                    int score = checkSOS(boardState, i, j);

                    if (score > m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'S';
                    }

                    boardState[i][j] = 'O';

                    score = checkSOS(boardState, i, j);

                    if (score > m.score) {
                        m.score = score;
                        m.row = i;
                        m.col = j;
                        m.moveToPlay = 'O';
                    }

                    boardState[i][j] = 'x';
                }
            }
        }

        return m;
    }

    private boolean checkSO(char[][] board, int row, int col, char ch) {
        if (row < 0 || row >= board.length || col < 0 || col >= board.length) {
            return false;
        }
        return board[row][col] == ch;
    }

    private int checkSOS(char[][] board, int row, int col) {

        int score = 0;

        if (board[row][col] == 'S') {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (checkSO(board, row + k, col + l, 'O') && checkSO(board, row + 2 * k, col + 2 * l, 'S')) {
                        score++;
                    }
                }
            }
        } else {

            for (int k = -1; k <= 1; k++) {
                for (int l = -1; l <= 1; l++) {
                    if (k == 0 && l == 0) {
                        continue;
                    }

                    if (checkSO(board, row + k, col + l, 'S') && checkSO(board, row - k, col - l, 'S')) {
                        score++;
                    }
                }
            }

        }

        return score;
    }

    private class Move {
        int row, col;
        int score;
        char moveToPlay;

        Move() {
            row = col = score = -1;
            moveToPlay = 'x';
        }
    }
}

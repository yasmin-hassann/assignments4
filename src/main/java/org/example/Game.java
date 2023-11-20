package org.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

public class Game extends Scene {

    private final BorderPane pane;
    //keep track of the player's choice (S or O)
    private Boolean isSSelected = true; //true = S, false = O
    //Two players
    private final UserPanel blue;
    private final UserPanel red;

    private Boolean isBluePlayerTurn = true; //true = blue, false = red

    private boolean isSimpleMode = true; //true = simple, false = general

    private final Label statusLabel = new Label("Blue Player's Turn");
    private final Label SOS;

    private Board board;


    public Game() {
        super(new BorderPane());
        int boardSize = takeGameInfo();
        pane = (BorderPane) getRoot();
        blue = new UserPanel("Blue Player", this);
        red = new UserPanel("Red Player", this);

        //build the top panel
        HBox topPanel = new HBox();
        topPanel.setStyle("-fx-border-color: #000000; -fx-padding: 10px;");
        topPanel.prefHeight(60);

        SOS = new Label("SOS (" + (isSimpleMode ? "Simple" : "General") + " Mode)                            ");
        SOS.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        topPanel.getChildren().add(SOS);


        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            resetGame();
        });
        topPanel.getChildren().add(reset);
        pane.setTop(topPanel);

        //build the left panel
        pane.setLeft(blue);
        pane.setRight(red);

        FlowPane bottom = new FlowPane();
        bottom.setStyle("-fx-border-color: #000000; -fx-padding: 10px;");
        bottom.prefHeight(60);
        bottom.getChildren().add(statusLabel);
        pane.setBottom(bottom);

        board = new Board(boardSize, this);
        pane.setCenter(board);

        blue.setActivatedBackground();
    }

    private int takeGameInfo() {
        //ask the user for the game mode
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Game Mode");
        dialog.setHeaderText("Choose the game mode");

        //hide the alert confirmation button

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        DialogPane dialogPane = dialog.getDialogPane();

        Button simple = new Button("Simple");
        simple.setOnAction(e -> {
            isSimpleMode = true;
            System.out.println("simple");
            dialog.close();
            dialog.hide();
            window.hide();
        });

        Button general = new Button("General");
        general.setOnAction(e -> {
            isSimpleMode = false;
            System.out.println("general");
            dialog.close();
            dialog.hide();
            window.hide();
        });

        HBox hbox = new HBox();
        hbox.setSpacing(65);
        hbox.getChildren().addAll(simple, general);
        dialogPane.setContent(hbox);
        dialog.showAndWait();


        //ask for board size
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Board Size");
        alert.setHeaderText("Choose the board size");

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        choiceBox.setValue(8);
        //hide the alert cancel button
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setVisible(false);

        alert.getDialogPane().setContent(choiceBox);
        alert.showAndWait();


        return choiceBox.getValue();

    }


    //get type of game mode for current player
    public boolean getIsSSelected() {
        return isSSelected;
    }

    public void updateMode() {
        if (isBluePlayerTurn) {
            isSSelected = blue.isModeS();
        } else {
            isSSelected = red.isModeS();
        }
    }

    public void alterTurn() {

        if (board.isGameOver() || (isSimpleMode && (blue.getScore() > 0 || red.getScore() > 0))) {
            gameOver();
            return;
        }
        isBluePlayerTurn = !isBluePlayerTurn;
        if (isBluePlayerTurn) {
            statusLabel.setText("Blue Player's Turn");
            isSSelected = blue.isModeS();
            blue.setActivatedBackground();
            red.resetBackground();

            if (blue.isComputerSelected()) {
                blue.playNextMove(board);
            }
        } else {
            statusLabel.setText("Red Player's Turn");
            isSSelected = red.isModeS();
            red.setActivatedBackground();
            blue.resetBackground();

            if (red.isComputerSelected())
                red.playNextMove(board);
        }
    }

    private void gameOver() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        if (blue.getScore() > red.getScore()) {
            alert.setContentText("Blue Player Wins!");
        } else if (blue.getScore() < red.getScore()) {
            alert.setContentText("Red Player Wins!");
        } else {
            alert.setContentText("Draw!");
        }

        alert.showAndWait();
        resetGame();

    }

    private void resetGame() {
        int boardSize = takeGameInfo();

        board = new Board(boardSize, this);
        SOS.setText("SOS (" + (isSimpleMode ? "Simple" : "General") + " Mode)                            ");

        blue.resetScore();
        red.resetScore();
        pane.setCenter(board);

        isSSelected = true;
        statusLabel.setText("Blue Player's Turn");
        isSSelected = blue.isModeS();
        blue.setActivatedBackground();
        red.resetBackground();

        Window window = getRoot().getScene().getWindow();

        int width = boardSize * 60 + 300;
        int height = boardSize * 50 + 200;

        if (width < 600) width = 600;
        if (height < 300) height = 300;
        window.setWidth(width);
        window.setHeight(height);
    }


    public void incrementScore() {
        if (isBluePlayerTurn) {
            blue.incrementScore();
        } else {
            red.incrementScore();
        }
    }

    public boolean isBluePlayerTurn() {
        return isBluePlayerTurn;
    }
}

/*
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

public class Game extends Scene {

    private final BorderPane pane;
    //keep track of the player's choice (S or O)
    private Boolean isSSelected = true; //true = S, false = O
    //Two players
    private final UserPanel blue;
    private final UserPanel red;

    private Boolean isBluePlayerTurn = true; //true = blue, false = red

    private boolean isSimpleMode = true; //true = simple, false = general

    private final Label statusLabel = new Label("Blue Player's Turn");
    private final Label SOS;

    private Board board;


    public Game() {
        super(new BorderPane());
        int boardSize = takeGameInfo();
        pane = (BorderPane) getRoot();
        blue = new UserPanel("Blue Player", this);
        red = new UserPanel("Red Player", this);

        //build the top panel
        HBox topPanel = new HBox();
        topPanel.setStyle("-fx-border-color: #000000; -fx-padding: 10px;");
        topPanel.prefHeight(60);

        SOS = new Label("SOS (" + (isSimpleMode ? "Simple" : "General") + " Mode)                            ");
        SOS.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        topPanel.getChildren().add(SOS);


        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            resetGame();
        });
        topPanel.getChildren().add(reset);
        pane.setTop(topPanel);

        //build the left panel
        pane.setLeft(blue);
        pane.setRight(red);

        FlowPane bottom = new FlowPane();
        bottom.setStyle("-fx-border-color: #000000; -fx-padding: 10px;");
        bottom.prefHeight(60);
        bottom.getChildren().add(statusLabel);
        pane.setBottom(bottom);

        board = new Board(boardSize, this);
        pane.setCenter(board);

        blue.setActivatedBackground();
    }

    private int takeGameInfo() {
        //ask the user for the game mode
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Game Mode");
        dialog.setHeaderText("Choose the game mode");

        //hide the alert confirmation button

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        DialogPane dialogPane = dialog.getDialogPane();

        Button simple = new Button("Simple");
        simple.setOnAction(e -> {
            isSimpleMode = true;
            System.out.println("simple");
            dialog.close();
            dialog.hide();
            window.hide();
        });

        Button general = new Button("General");
        general.setOnAction(e -> {
            isSimpleMode = false;
            System.out.println("general");
            dialog.close();
            dialog.hide();
            window.hide();
        });

        HBox hbox = new HBox();
        hbox.setSpacing(65);
        hbox.getChildren().addAll(simple, general);
        dialogPane.setContent(hbox);
        dialog.showAndWait();


        //ask for board size
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Board Size");
        alert.setHeaderText("Choose the board size");

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        choiceBox.setValue(8);
        //hide the alert cancel button
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setVisible(false);

        alert.getDialogPane().setContent(choiceBox);
        alert.showAndWait();


        return choiceBox.getValue();

    }


    //get type of game mode for current player
    public boolean getIsSSelected() {
        return isSSelected;
    }

    public void updateMode() {
        if (isBluePlayerTurn) {
            isSSelected = blue.isModeS();
        } else {
            isSSelected = red.isModeS();
        }
    }



public void alterTurn() {
    if (board.isBoardFull()) {
        gameOver();
        return;
    }
    isBluePlayerTurn = !isBluePlayerTurn;
    if (isBluePlayerTurn) {
        statusLabel.setText("Blue Player's Turn");
        isSSelected = blue.isModeS();
        blue.setActivatedBackground();
        red.resetBackground();

        if (blue.isComputerSelected()) {
            blue.playNextMove(board);
        }
    } else {
        statusLabel.setText("Red Player's Turn");
        isSSelected = red.isModeS();
        red.setActivatedBackground();
        blue.resetBackground();

        if (red.isComputerSelected())
            red.playNextMove(board);
    }

}

    private void gameOver() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        if (blue.getScore() > red.getScore()) {
            alert.setContentText("Blue Player Wins!");
        } else if (blue.getScore() < red.getScore()) {
            alert.setContentText("Red Player Wins!");
        } else {
            alert.setContentText("Draw!");
        }

        alert.showAndWait();
        resetGame();

    }



    private void resetGame() {
        int boardSize = takeGameInfo();

        board = new Board(boardSize, this);
        SOS.setText("SOS (" + (isSimpleMode ? "Simple" : "General") + " Mode)                            ");

        blue.resetScore();
        red.resetScore();
        pane.setCenter(board);

        isSSelected = true;
        statusLabel.setText("Blue Player's Turn");
        isSSelected = blue.isModeS();
        blue.setActivatedBackground();
        red.resetBackground();

        Window window = getRoot().getScene().getWindow();

        int width = boardSize * 60 + 300;
        int height = boardSize * 50 + 200;

        if (width < 600) width = 600;
        if (height < 300) height = 300;
        window.setWidth(width);
        window.setHeight(height);
    }


    public void incrementScore() {
        if (isBluePlayerTurn) {
            blue.incrementScore();


        } else {
            red.incrementScore();


        }
    }




    public boolean isBluePlayerTurn() {
        return isBluePlayerTurn;
    }
}

 */
package com.example.gui;

import com.example.logic.Game;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Menu {
    private static int remainingTime1 = 60;
    private static int remainingTime2 = 60;
    private static volatile boolean isRunning = false;

    private static VBox menu;

    private static Label timeLabel1;
    private static Label timeLabel2;
    private static Label victoryLabel;

    public static void showMenu() {
        timeLabel2 = new Label();
        timeLabel1 = new Label();
        victoryLabel = new Label();
        updateLabels();

        Button newGameButton = new Button("New game");
        newGameButton.setOnAction(event -> startGame());

        menu = new VBox(10);
        menu.minWidth(150);
        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(timeLabel2, timeLabel1, victoryLabel, newGameButton);
    }

    private static void startGame(){
        Board.game = new Game();
        Board.showBoard();
        remainingTime1 = 60;
        remainingTime2 = 60;
        updateLabels();
        startCountdown();
        hideVictoryMessage();
    }

    private static void startCountdown() {
        if (!isRunning) {
            isRunning = true;
            Thread countdownThread = new Thread(() -> {
                while (remainingTime1 > 0 && remainingTime2 > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (!isRunning) {
                        Thread.onSpinWait();
                    }
                    if (Board.game.isWhiteMove()) {
                        remainingTime1--;
                    } else {
                        remainingTime2--;
                    }
                    Platform.runLater(Menu::updateLabels);
                }
                Board.game.setWhiteWinner(remainingTime1 > 0);
                Platform.runLater(Menu::displayVictoryMessage);
                isRunning = false;
            });
            countdownThread.start();
        }
    }

    public static void stopCountdown() {
        isRunning = false;
    }

    private static void updateLabels() {
        timeLabel2.setText("White player:\n" + remainingTime2 + " seconds");
        timeLabel1.setText("Black player:\n" + remainingTime1 + " seconds");
    }

    public static void displayVictoryMessage() {
        victoryLabel.setText((Board.game.isWhiteWinner() ? "White" : "Black") + " player won!");
    }

    public static void hideVictoryMessage() {
        victoryLabel.setText("");
    }

    public static VBox getMenu() {
        return menu;
    }
}

package com.example;

import com.example.gui.Board;
import com.example.gui.Menu;
import com.example.logic.Game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();
        Scene scene = new Scene(root, Game.BOARD_SIZE * Board.TILE_SIZE + 150, Game.BOARD_SIZE * Board.TILE_SIZE);

        Board.showBoard();
        root.getChildren().add(Board.getBoard());

        Menu.showMenu();
        root.getChildren().add(Menu.getMenu());

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
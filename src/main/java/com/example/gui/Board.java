package com.example.gui;

import com.example.logic.Game;
import com.example.logic.SquareState;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Board {

    public static final int TILE_SIZE = 60;

    private static final GridPane board = new GridPane();
    public static Game game;

    public static void showBoard() {
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            for (int col = 0; col < Game.BOARD_SIZE; col++) {
                clearCell(row, col);
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((row + col) % 2 == 0) {
                    tile.setFill(Color.WHITE);
                } else {
                    tile.setFill(Color.GRAY);
                    int x = row;
                    int y = col;
                    tile.setOnMouseClicked(event -> BoardController.onMouseClicked(getCell(x, y), tile, x, y));
                }
                board.add(tile, col, row);
                if(game != null){
                    SquareState piece = game.getPiece(row, col);
                    Circle circle;
                    switch(piece){
                        case WhitePawn:
                            circle = new Circle((double) TILE_SIZE / 2 - 5, Color.WHITE);
                            GridPane.setHalignment(circle, HPos.CENTER);
                            GridPane.setValignment(circle, VPos.CENTER);
                            circle.setMouseTransparent(true);
                            board.add(circle, col, row);
                        break;
                        case BlackPawn:
                            circle = new Circle((double) TILE_SIZE / 2 - 5, Color.BLACK);
                            GridPane.setHalignment(circle, HPos.CENTER);
                            GridPane.setValignment(circle, VPos.CENTER);
                            circle.setMouseTransparent(true);
                            board.add(circle, col, row);
                        break;
                        case WhitePromoted:
                            circle = new Circle((double) TILE_SIZE / 2 - 5, Color.WHITE);
                            GridPane.setHalignment(circle, HPos.CENTER);
                            GridPane.setValignment(circle, VPos.CENTER);
                            circle.setMouseTransparent(true);
                            board.add(circle, col, row);
                            circle = new Circle((double) TILE_SIZE / 2 - 15, Color.RED);
                            GridPane.setHalignment(circle, HPos.CENTER);
                            GridPane.setValignment(circle, VPos.CENTER);
                            circle.setMouseTransparent(true);
                            board.add(circle, col, row);
                            break;
                        case BlackPromoted:
                            circle = new Circle((double) TILE_SIZE / 2 - 5, Color.BLACK);
                            GridPane.setHalignment(circle, HPos.CENTER);
                            GridPane.setValignment(circle, VPos.CENTER);
                            circle.setMouseTransparent(true);
                            board.add(circle, col, row);
                            circle = new Circle((double) TILE_SIZE / 2 - 15, Color.RED);
                            GridPane.setHalignment(circle, HPos.CENTER);
                            GridPane.setValignment(circle, VPos.CENTER);
                            circle.setMouseTransparent(true);
                            board.add(circle, col, row);
                            break;
                        case Empty:
                            break;
                    }
                }
            }
        }
    }

    public static GridPane getBoard(){
        return board;
    }

    private static Node getCell(int row, int column) {
        for (Node child : board.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(child);
            Integer rowIndex = GridPane.getRowIndex(child);

            if (columnIndex != null && rowIndex != null && columnIndex == column && rowIndex == row) {
                return child;
            }
        }
        return null;
    }

    private static void clearCell(int column, int row) {
        Node node = getCell(column, row);
        if (node != null) {
            board.getChildren().remove(node);
        }
    }
}
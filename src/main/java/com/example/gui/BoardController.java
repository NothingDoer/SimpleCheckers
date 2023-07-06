package com.example.gui;

import com.example.logic.SquareState;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardController {

    private static Node activeCell;
    private static int activeCellRow;
    private static int activeCellCol;

    public static void onMouseClicked(Node cell, Rectangle tile, int row, int col) {
        if(Board.game == null || Board.game.isWhiteWinner() != null){
            return;
        }
        if(activeCell == null) {
            if(Board.game.isWhiteMove() && (Board.game.getPiece(row, col) == SquareState.WhitePawn || Board.game.getPiece(row, col) == SquareState.WhitePromoted)){
                activeCell = cell;
                activeCellRow = row;
                activeCellCol = col;
                tile.setFill(Color.GREEN);
            }
            else if(!Board.game.isWhiteMove() && (Board.game.getPiece(row, col) == SquareState.BlackPawn || Board.game.getPiece(row, col) == SquareState.BlackPromoted)){
                activeCell = cell;
                activeCellRow = row;
                activeCellCol = col;
                tile.setFill(Color.GREEN);
            }
        }
        else if(Board.game.checkPossibleMove(activeCellRow, activeCellCol, row, col)){
            Board.game.move(activeCellRow, activeCellCol, row, col);
            Board.game.conditionalNextRound();
            Board.game.checkForWinner();
            if(Board.game.isWhiteWinner() != null){
                Menu.stopCountdown();
                Menu.displayVictoryMessage();
            }
            Board.showBoard();
            activeCell = null;
        }
        else{
            Board.showBoard();
            activeCell = null;
        }
    }

}

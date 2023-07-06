package com.example.logic;

public class Game {

    public static final int BOARD_SIZE = 8;

    private final SquareState[][] pieces;
    private boolean whiteMove;
    private Boolean whiteWinner;
    private boolean killedThisRound;
    
    public Game(){
        pieces = new SquareState[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 != 0) {
                    if(row < 3){
                        pieces[row][col] = SquareState.BlackPawn;
                    }
                    else if(row >= BOARD_SIZE - 3){
                        pieces[row][col] = SquareState.WhitePawn;
                    }
                    else{
                        pieces[row][col] = SquareState.Empty;
                    }
                } else {
                    pieces[row][col] = SquareState.Empty;
                }
            }
        }
        whiteMove = true;
        whiteWinner = null;
    }

    public SquareState getPiece(int row, int col){
        return pieces[row][col];
    }

    public boolean isWhiteMove(){
        return whiteMove;
    }

    public boolean checkPossibleMove(int startY, int startX, int endY, int endX) {
        if(pieces[endY][endX] != SquareState.Empty){
            return false;
        }

        int moveX = endX - startX;
        int moveY = endY - startY;

        if(Math.abs(moveX) != Math.abs(moveY)){
            return false;
        }

        if(Math.abs(moveX) == 2) {
            int middleX = (startX + endX) / 2;
            int middleY = (startY + endY) / 2;
            return isDifferentColor(pieces[startY][startX], pieces[middleY][middleX]);
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquareState piece = pieces[row][col];
                if(whiteMove && (piece == SquareState.WhitePawn || piece == SquareState.WhitePromoted) && canKill(piece, row, col)){
                    return false;
                }
                if(!whiteMove && (piece == SquareState.BlackPawn || piece == SquareState.BlackPromoted) && canKill(piece, row, col)){
                    return false;
                }
            }
        }

        if (pieces[startY][startX] == SquareState.WhitePawn) {
            return moveY == -1;
        } else if (pieces[startY][startX] == SquareState.BlackPawn) {
            return moveY == 1;
        }
        if(pieces[startY][startX] == SquareState.WhitePromoted || pieces[startY][startX] == SquareState.BlackPromoted){
            return Math.abs(moveY) == 1;
        }
        return false;
    }

    public void move(int startY, int startX, int endY, int endX){
        int moveX = endX - startX;
        if(Math.abs(moveX) == 2) {
            int middleX = (startX + endX) / 2;
            int middleY = (startY + endY) / 2;
            pieces[middleY][middleX] = SquareState.Empty;
            killedThisRound = true;
        }
        pieces[endY][endX] = pieces[startY][startX];
        pieces[startY][startX] = SquareState.Empty;

        if(endY == 0 && pieces[endY][endX] == SquareState.WhitePawn){
            pieces[endY][endX] = SquareState.WhitePromoted;
        }
        if(endY == BOARD_SIZE - 1 && pieces[endY][endX] == SquareState.BlackPawn){
            pieces[endY][endX] = SquareState.BlackPromoted;
        }
    }

    public void conditionalNextRound(){
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquareState piece = pieces[row][col];
                if(whiteMove && (piece == SquareState.WhitePawn || piece == SquareState.WhitePromoted) && canKill(piece, row, col) && killedThisRound){
                    return;
                }
                if(!whiteMove && (piece == SquareState.BlackPawn || piece == SquareState.BlackPromoted) && canKill(piece, row, col) && killedThisRound){
                    return;
                }
            }
        }
        whiteMove = !whiteMove;
        killedThisRound = false;
    }

    public void checkForWinner(){
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                SquareState piece = pieces[row][col];
                if(whiteMove && (piece == SquareState.WhitePawn || piece == SquareState.WhitePromoted) && (canMove(piece, row, col) || canKill(piece, row, col))){
                    return;
                }
                if(!whiteMove && (piece == SquareState.BlackPawn || piece == SquareState.BlackPromoted) && (canMove(piece, row, col) || canKill(piece, row, col))){
                    return;
                }
            }
        }
        whiteWinner = !whiteMove;
    }

    private boolean isDifferentColor(SquareState a, SquareState b){
        if(a == SquareState.Empty || b == SquareState.Empty){
            return false;
        }
        boolean isAWhite = a == SquareState.WhitePawn || a == SquareState.WhitePromoted;
        boolean isBWhite = b == SquareState.WhitePawn || b == SquareState.WhitePromoted;
        return isAWhite != isBWhite;
    }

    private boolean canKill(SquareState piece, int row, int col) {
        if (row >= 2 && col >= 2) {
            SquareState leftPiece = pieces[row-1][col-1];
            SquareState capturedPiece = pieces[row-2][col-2];
            if (isDifferentColor(piece, leftPiece) && capturedPiece == SquareState.Empty) {
                return true;
            }
        }

        if (row >= 2 && col <= BOARD_SIZE - 3) {
            SquareState leftBackPiece = pieces[row-1][col+1];
            SquareState capturedPiece = pieces[row-2][col+2];
            if (isDifferentColor(piece, leftBackPiece) && capturedPiece == SquareState.Empty) {
                return true;
            }
        }

        if (row <= BOARD_SIZE - 3 && col >= 2) {
            SquareState rightPiece = pieces[row+1][col-1];
            SquareState capturedPiece = pieces[row+2][col-2];
            if (isDifferentColor(piece, rightPiece) && capturedPiece == SquareState.Empty) {
                return true;
            }
        }

        if (row <= BOARD_SIZE - 3 && col <= BOARD_SIZE - 3) {
            SquareState rightBackPiece = pieces[row+1][col+1];
            SquareState capturedPiece = pieces[row+2][col+2];
            return isDifferentColor(piece, rightBackPiece) && capturedPiece == SquareState.Empty;
        }

        return false;
    }

    private boolean canMove(SquareState piece, int row, int col) {
        if ((piece != SquareState.BlackPawn) && row >= 1 && col >= 1) {
            SquareState leftPiece = pieces[row-1][col-1];
            if (leftPiece == SquareState.Empty) {
                return true;
            }
        }

        if ((piece != SquareState.BlackPawn) && row >= 1 && col <= BOARD_SIZE - 2) {
            SquareState leftBackPiece = pieces[row-1][col+1];
            if (leftBackPiece == SquareState.Empty) {
                return true;
            }
        }

        if ((piece != SquareState.WhitePawn) && row <= BOARD_SIZE - 2 && col >= 1) {
            SquareState rightPiece = pieces[row+1][col-1];
            if (rightPiece == SquareState.Empty) {
                return true;
            }
        }

        if ((piece != SquareState.WhitePawn) && row <= BOARD_SIZE - 2 && col <= BOARD_SIZE - 2) {
            SquareState rightBackPiece = pieces[row+1][col+1];
            return rightBackPiece == SquareState.Empty;
        }

        return false;
    }

    public void setWhiteWinner(Boolean winner){
        whiteWinner = winner;
    }

    public Boolean isWhiteWinner(){
        return whiteWinner;
    }
}

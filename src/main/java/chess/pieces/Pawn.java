package chess.pieces;

import chess.Chessboard;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

import java.util.Arrays;

/**
 * Classe réprésentant le pion
 */
public class Pawn extends Piece {

    /**
     * Vrai si le pion n'a pas encore été déplacé.
     */
    private boolean notMovedYet = true;


    /**
     * Constructeur d'une pièece d'échec (Pion)
     *
     * @param chessboard Echiquier auquel la pièce appartient
     * @param position Position initiale de la pièce
     * @param color Couleur de la pièce
     */
    public Pawn(Chessboard chessboard, Position position, Color color) {
        super(chessboard, position, color, "Pion", color.name() == "BLACK" ? Symbol.BLACK_PANW : Symbol.WHITE_PAWN);
        // TODO Auto-generated constructor stub
    }

    /**
     * Deplace la pièce sur la case indiquée.
     *
     * @param destination Position de la case de destination du déplacement.
     * @throws ChessMoveException Si le mouvement n'est pas possible
     */
    public void moveTo(Position destination) throws ChessMoveException {
        String other_color = board.getPiece(destination) == null ? "No piece": board.getPiece(destination).getColor().toString();
        if(isValidMove(destination) &&
                !other_color.equals(this.getColor().toString())
        )
        {
            if(notMovedYet) notMovedYet = false;
            board.setPiece(destination, this);
        }
        else {
            throw new ChessMoveException("Mouvement non valide", this.getPosition(), destination);
        }
    }

    /**
     * Teste la validité d'un déplacement
     *
     * @param destination Le déplacement
     */
    @Override
    public boolean isValidMove(Position destination) {
        boolean isPossible = false;

        int current_x = this.getPosition().getX();
        int current_y = this.getPosition().getY();

        Piece lastPiece;
        boolean isPawnAdjacent = false;
        if(board.lastMove[1] != null) {
            lastPiece = board.getPiece(board.lastMove[1]);
            isPawnAdjacent = lastPiece.getName().equals("Pion") && (current_y - 1 == lastPiece.getPosition().getY() || current_y + 1 == lastPiece.getPosition().getY());
        }

        if(this.isBlack()) {
            if(notMovedYet) {
                if(current_x - destination.getX() == 1 && destination.getY() == current_y||
                        current_x - destination.getX() == 2 && destination.getY() == current_y) {
                    isPossible = true;
                }
            }
            else if(this.getPosition().getManhattanDistance(destination) == 1 && destination.getY() == current_y && current_x - destination.getX() == 1) {
                isPossible = true;
            }
            else if(this.getPosition().getManhattanDistance(destination) == 2 &&
                    isPawnAdjacent && board.lastMove[1].getX() - board.lastMove[0].getX() == 2) {
                isPossible = true;
                board.setPiece(board.lastMove[1], this);
            }
            else if(this.getPosition().getManhattanDistance(destination) == 2 && board.getPiece(destination) != null) {
                isPossible = true;
            }
        }

        else if(this.isWhite()) {
            if(notMovedYet) {
                if( destination.getX() - current_x == 1 && destination.getY() == current_y||
                        destination.getX() - current_x == 2 && destination.getY() == current_y) {
                    isPossible = true;
                }
            }
            else if(this.getPosition().getManhattanDistance(destination) == 1 && destination.getY() == current_y && destination.getX() - current_x == 1) {
                isPossible = true;
            }
            else if(this.getPosition().getManhattanDistance(destination) == 2 &&
                    isPawnAdjacent && board.lastMove[0].getX() - board.lastMove[1].getX() == 2) {
                isPossible = true;
                board.setPiece(board.lastMove[1], this);
            }
            else if(this.getPosition().getManhattanDistance(destination) == 2 && board.getPiece(destination) != null) {
                isPossible = true;
            }
        }
        return isPossible;
    }

}
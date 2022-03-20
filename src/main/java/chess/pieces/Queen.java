package chess.pieces;

import chess.Chessboard;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

/**
 * Classe réprésentant la reine
 */
public class Queen extends Piece {

    /**
     * Constructeur d'une pièece d'échec (Reine)
     *
     * @param chessboard Echiquier auquel la pièce appartient
     * @param position Position initiale de la pièce
     * @param color Couleur de la pièce
     */
    public Queen(Chessboard chessboard, Position position, Color color) {
        super(chessboard, position, color, "Reine", color.name() == "BLACK" ? Symbol.BLACK_QUEEN : Symbol.WHITE_QUEEN);
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
            board.setPiece(destination, this);
        } else {
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
        return board.isPiecePresentOnSameLineBetween(this.getPosition(), destination) || board.isPiecePresentOnSameColumnBetween(this.getPosition(), destination) || board.isPiecePresentOnSameDiagonalBetween(this.getPosition(), destination);
    }

    @Override
    public Object clone() {
        Piece piece = null;
        try {
            Queen clone = (Queen) super.superClone();
            /* some operations */
            return clone;
        } catch (CloneNotSupportedException e) {
            piece = new Queen(
                    this.getBoard(), this.getPosition(), this.getColor());
        }
        try {
            piece.board = (Chessboard) this.board.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        piece.setPosition((Position) this.getPosition().clone());
        return piece;
    }
}
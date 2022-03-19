package chess.pieces;

import chess.Chessboard;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

/**
 * Classe réprésentant la tour
 */
public class Rook extends Piece {

    /**
     * Constructeur d'une pièece d'échec (Tour)
     *
     * @param chessboard Echiquier auquel la pièce appartient
     * @param position Position initiale de la pièce
     * @param color Couleur de la pièce
     */
    public Rook(Chessboard chessboard, Position position, Color color) {
        super(chessboard, position, color, "Tour", color.name() == "BLACK" ? Symbol.BLACK_ROOK : Symbol.WHITE_ROOK);
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
        return board.isPiecePresentOnSameColumnBetween(this.getPosition(), destination) || board.isPiecePresentOnSameLineBetween(this.getPosition(), destination);
    }

}
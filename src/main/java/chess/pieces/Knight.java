package chess.pieces;

import chess.Chessboard;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

/**
 * Classe réprésentant le cavalier
 */
public class Knight extends Piece {

    /**
     * Constructeur d'une pièece d'échec (Cavalier)
     *
     * @param chessboard Echiquier auquel la pièce appartient
     * @param position Position initiale de la pièce
     * @param color Couleur de la pièce
     */
    public Knight(Chessboard chessboard, Position position, Color color) {
        super(chessboard, position, color, "Cavalier", color.name() == "BLACK" ? Symbol.BLACK_KNGIHT : Symbol.WHITE_KNIGHT);
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
        boolean isPossible = false;
        if (
                ((this.getPosition().getX() + 1 == destination.getX()) && (this.getPosition().getY() - 2 == destination.getY())) ||
                        ((this.getPosition().getX() - 1 == destination.getX()) && (this.getPosition().getY() - 2 == destination.getY())) ||
                        ((this.getPosition().getY() - 1 == destination.getY()) && (this.getPosition().getX() - 2 == destination.getX())) ||
                        ((this.getPosition().getY() - 1 == destination.getY()) && (this.getPosition().getX() + 2 == destination.getX())) ||
                        ((this.getPosition().getX() + 1 == destination.getX()) && (this.getPosition().getY() + 2 == destination.getY())) ||
                        ((this.getPosition().getX() - 1 == destination.getX()) && (this.getPosition().getY() + 2 == destination.getY())) ||
                        ((this.getPosition().getX() + 2 == destination.getX()) && (this.getPosition().getY() + 1 == destination.getY())) ||
                        ((this.getPosition().getX() - 2 == destination.getX()) && (this.getPosition().getY() + 1 == destination.getY()))
        ) {
            isPossible = true;
        }
        return isPossible;
    }

}
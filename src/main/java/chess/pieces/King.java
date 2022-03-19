package chess.pieces;

import chess.Chessboard;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

/**
 * Classe réprésentant le roi
 */
public class King extends Piece {

    /**
     * Constructeur d'une pièece d'échec (Roi)
     *
     * @param chessboard Echiquier auquel la pièce appartient
     * @param position Position initiale de la pièce
     * @param color Couleur de la pièce
     */
    public King(Chessboard chessboard, Position position, Color color) {
        super(chessboard, position, color, "Roi", color.name() == "BLACK" ? Symbol.BLACK_KING : Symbol.WHITE_KING);
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
            this.inCheck();
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
        int current_x = this.getPosition().getX();
        int current_y = this.getPosition().getY();

        if(current_x < destination.getX()) {
            if( destination.getX() - current_x == 1) {
                isPossible = true;
            }
        }
        else if (destination.getX() < current_x){
            if(current_x - destination.getX() == 1) {
                isPossible = true;
            }
        }
        //droite
        else if(current_y < destination.getY()) {
            Piece right_piece = board.getPiece(current_x, destination.getY()+1);
            if( destination.getY() - current_y == 1) {
                isPossible = true;
            }
            else if(destination.getY() - current_y == 2 &&
                    right_piece instanceof Rook
            )
            {
                isPossible = true;
                Position king_hop = new Position(current_x, right_piece.getPosition().getY()-2);
                board.setPiece(king_hop, right_piece);
            }
        }
        //gauche
        else if (destination.getY() < current_y){
            Piece right_piece = board.getPiece(current_x, destination.getY()-1);
            if(current_y - destination.getY() == 1) {
                isPossible = true;
            }
            else if(current_y - destination.getY() == 2 &&
                    right_piece instanceof Rook
            )
            {
                isPossible = true;
                Position king_hop = new Position(current_x, right_piece.getPosition().getY()+2);
                board.setPiece(king_hop, right_piece);
            }
        }
        else if(this.getPosition().getManhattanDistance(destination) == 2) {
            isPossible = !(board.getPiece(destination) instanceof King);
        }
        return isPossible;
    }

    /**
     * Vérifie si le roi en quesiton est en état d'échec
     *
     * @return true si le roi est en échec, false sinon
     */
    public boolean inCheck() {
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Position pos = new Position(x, y);
                if (!sameColorPiece(pos)) {
                    if (
                            (board.isPiecePresentOnSameLineBetween(this.getPosition(), pos) ||
                                    board.isPiecePresentOnSameColumnBetween(this.getPosition(), pos)) &&
                                    (board.getPiece(pos) instanceof Rook
                                            || board.getPiece(pos) instanceof Queen)
                    ) {
                        System.out.println("Le roi en position " + this.getPosition().toAlgebraicNotation() + " est en échec!!");
                        return true;
                    } else if (
                            board.isPiecePresentOnSameDiagonalBetween(this.getPosition(), pos) &&
                                    (board.getPiece(pos) instanceof Rook
                                    || board.getPiece(pos) instanceof Queen)) {
                        System.out.println("Le roi en position " + this.getPosition().toAlgebraicNotation() + " est en échec!!");
                        return true;
                    } else if (
                            (
                                    ((this.getPosition().getX() + 1 == pos.getX()) && (this.getPosition().getY() - 2 == pos.getY())) ||
                                            ((this.getPosition().getX() - 1 == pos.getX()) && (this.getPosition().getY() - 2 == pos.getY())) ||
                                            ((this.getPosition().getY() - 1 == pos.getY()) && (this.getPosition().getX() - 2 == pos.getX())) ||
                                            ((this.getPosition().getY() - 1 == pos.getY()) && (this.getPosition().getX() + 2 == pos.getX())) ||
                                            ((this.getPosition().getX() + 1 == pos.getX()) && (this.getPosition().getY() + 2 == pos.getY())) ||
                                            ((this.getPosition().getX() - 1 == pos.getX()) && (this.getPosition().getY() + 2 == pos.getY())) ||
                                            ((this.getPosition().getX() + 2 == pos.getX()) && (this.getPosition().getY() + 1 == pos.getY())) ||
                                            ((this.getPosition().getX() - 2 == pos.getX()) && (this.getPosition().getY() + 1 == pos.getY()))
                            ) && board.getPiece(pos) instanceof Knight
                    ) {
                        System.out.println("Le roi en position " + this.getPosition().toAlgebraicNotation() + " est en échec!!");
                        return true;
                    } else if (this.getPosition().getManhattanDistance(pos) == 2 && board.getPiece(pos) instanceof Pawn) {
                        System.out.println("Le roi en position " + this.getPosition().toAlgebraicNotation() + " est en échec!!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean sameColorPiece(Position destination) {
        String opposingColor = board.getPiece(destination) == null ? "" : board.getPiece(destination).getColor().name();
        return board.getPiece(this.getPosition()).getColor().name().equals(opposingColor);
    }
}

package chess.util;

import chess.Chessboard;
import chess.pieces.Piece;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    //@Test
    void makeRandomValiedMove() {
    }

    @Test
    void getBestPossibleMove() throws ChessMoveException, CloneNotSupportedException {
        AI ai = new AI();
        Position[] moves = ai.getBestPossibleMove(new Chessboard(), Color.WHITE);
        for(Position pos : moves) {
            System.out.println(pos.toAlgebraicNotation());
        }
    }

    public static void testAllPossibleMoves() {
        Chessboard board = new Chessboard();
        ArrayList<Position[]> all_moves = new ArrayList<>();
        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                if (board.getPiece(ligne, colonne) != null) {
                    Piece chess_piece = board.getPiece(ligne, colonne);
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            Position pos = new Position(x, y);
                            String other_color = board.getPiece(pos) == null ? "No piece": board.getPiece(pos).getColor().toString();
                            try {
                                if (chess_piece.isValidMove(pos) && !other_color.equals(chess_piece.getColor().toString())) {
                                    Position[] positions = new Position[2];
                                    positions[0] = chess_piece.getPosition();
                                    positions[1] = pos;
                                    all_moves.add(positions);
                                }
                            }
                            catch(ArrayIndexOutOfBoundsException e) {
                                System.out.println(x + " " + y);
                                System.out.println(pos);
                                System.out.println(e);
                            }
                        }
                    }
                }
            }
        }
        Position[][] all_possible_moves = new Position[all_moves.size()][];
        for(int i = 0; i < all_moves.size(); i++) {
            all_possible_moves[i] = all_moves.get(i);
            //System.out.println(Arrays.toString(all_possible_moves[i]));
            System.out.print(all_possible_moves[i][0].toAlgebraicNotation() + " ");
            System.out.print(all_possible_moves[i][1].toAlgebraicNotation());
        }
    }

    @Test
    public void test() throws CloneNotSupportedException {
        Chessboard board = new Chessboard();
        Chessboard copy = board.clone();
        System.out.println(Arrays.deepEquals(copy.getPieces(), board.getPieces()));
    }

    public static void main(String[] args) {

    }
}
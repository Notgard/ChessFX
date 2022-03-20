package chess.util;

import chess.Chessboard;
import chess.pieces.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class AI {

    /**
     * Retourne un coup d'échec au hasard avce le contexte de la partie actuelle
     *
     * @param board l'échiquier de la partie
     * @return une série de coup d'échec, le première élément contient la case de départ et la deuxième la case de destination
     */
    public Position[] makeRandomValidMove(Chessboard board, Color color) {
        ArrayList<Position[]> random_moves = new ArrayList<>();
        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                if (board.getPiece(ligne, colonne) != null) {
                    Piece chess_piece = board.getPiece(ligne, colonne);
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            Position pos = new Position(x, y);
                            String other_color = board.getPiece(pos) == null ? "No piece": board.getPiece(pos).getColor().toString();
                            if (chess_piece.isValidMove(pos) && !other_color.equals(chess_piece.getColor().toString()) && chess_piece.getColor().toString().equals(color.toString())) {
                                Position[] positions = new Position[2];
                                positions[0] = chess_piece.getPosition();
                                positions[1] = pos;
                                random_moves.add(positions);
                            }
                        }
                    }
                }
            }
        }
        int index = (int)(Math.random() * random_moves.size());
        return random_moves.get(index);
    }


    /**
     * Evalue la valeur d'une pièce sur un échiquier min-max
     *
     * @param board l'échiquier
     * @param color la couleur du joueur actuel
     * @return
     */
    private int evaluateBoardValue(Chessboard board, Color color) {
        // Sets the value for each piece using standard piece value
        HashMap<String, Integer> piece_value = new HashMap<String, Integer>();
        piece_value.put("Pion", 100);
        piece_value.put("Cavalier", 350);
        piece_value.put("Fou", 350);
        piece_value.put("Tour", 525);
        piece_value.put("Reine", 1000);
        piece_value.put("Roi", 10000);

        // Loop through all pieces on the board and sum up total
        int value = 0;

        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                if (board.getPiece(ligne, colonne) != null) {
                    Piece chess_piece = board.getPiece(ligne, colonne);
                    value += piece_value.get(chess_piece.getName()) * (chess_piece.getColor() == color ? 1 : -1);
                }
            }
        }

        return value;
    };

    private Position[][] getAllPossibleMoves(Chessboard board) {
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
                            catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                        }
                    }
                }
            }
        }
        Position[][] all_possible_moves = new Position[all_moves.size()][];
        for(int i = 0; i < all_moves.size(); i++) {
            all_possible_moves[i] = all_moves.get(i);
        }
        return all_possible_moves;
    }

    public Position[] getBestPossibleMove(Chessboard board, Color playerColor) throws CloneNotSupportedException, ChessMoveException {
        // List all possible moves
        Chessboard original = board.clone();
        Position[][] all_possible_moves = this.getAllPossibleMoves(original);
        int length = all_possible_moves.length;
        System.out.println(original);
        // Sort moves randomly, so the same move isn't always picked on ties
        Collections.shuffle(Arrays.asList(all_possible_moves));

        // Search for move with highest value
        Position bestMoveSoFarStart = null;
        Position bestMoveSoFarDest = null;
        var bestMoveValue = Double.NEGATIVE_INFINITY;

        /**
         * Solution autour du problème avec les deep copies de board

        Chessboard[] copies = new Chessboard[length];
        Chessboard original = board.clone();
        for(int i = 0; i < length; i++) {
            copies[i] = original.clone();
        }
        */
        int t = 0;
        for(Position[] pos : all_possible_moves) {
            Chessboard board_copy = board.clone();
            System.out.println(board.getPieces() == board_copy.getPieces());
            System.out.println(board_copy.getLastMove() == board_copy.getLastMove());

            Piece chess_piece = board_copy.getPiece(pos[0]);

            Position originalPosition = (Position) pos[0].clone();
            try {
                chess_piece.setBoard(board_copy);
                chess_piece.moveTo(pos[1]);
            }
            catch(Exception ignored) {
            }
            int moveValue = evaluateBoardValue(board_copy, playerColor);
            if(moveValue > bestMoveValue) {
                bestMoveSoFarStart = originalPosition;
                bestMoveSoFarDest = pos[1];
                bestMoveValue = moveValue;
            }
            t++;
            //board_copy.setPiece(originalPosition, chess_piece);
        }

        return new Position[]{bestMoveSoFarStart, bestMoveSoFarDest};
    }
}

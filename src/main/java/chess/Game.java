package chess;

import java.util.*;

import chess.pieces.Piece;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;

/**
 * Classe représentant une partie d'échecs.
 */
public class Game {


    /**
     * Nom du joueur ayant les pièces blanches
     */
    private String whitePlayerName;

    /**
     * Nom du joueur ayant les pièces noires
     */
    private String BlackPlayerName;

    /**
     * Echiquier
     */
    private Chessboard board;

    /**
     * Couleur des pièces du joueur courant
     */
    private Color currentColor = Color.WHITE;

    /**
     * Construit une patrie d'échec avec le nom des deux joueurs, l'échiquier et son affichage
     * @param whitePlayerName Nom du joueur ayant les pièces blanches
     * @param blackPlayerName Nom du joueur ayant les pièces noires
     */
    public Game(String whitePlayerName, String blackPlayerName) {
        this.whitePlayerName = whitePlayerName;
        this.BlackPlayerName = blackPlayerName;
        this.board = new Chessboard();
        System.out.println(this.board.toString());
    }

    /**
     * Tour du joueur courant
     *
     * @param start Position de la pièce à déplacer
     * @param end Destination du déplacement
     * @throws ChessMoveException si la case de départ est vide, si elle contient une pièce de l'adversaire, ou si le déplacement est invalide.
     */
    public void turn(Position start, Position end) throws ChessMoveException {
        this.board.getPiece(start).moveTo(end);
        this.currentColor = this.currentColor.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Retourne le nom du joueur ayant les pièces blanches
     *
     * @return le nom du joueur ayant les pièces blanches
     */
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    /**
     * Retourne le nom du joueur ayant les pièces noires
     *
     * @return le nom du joueur ayant les pièces noires
     */
    public String getBlackPlayerName() {
        return BlackPlayerName;
    }

    /**
     * Retourne la couleur des pièces du joueur dont c'est le tour
     *
     * @return la couleur des pièces du joueur dont c'est le tour
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Programme principal. Permet à deux joueurs de saisir leurs déplacements à tour de rôle, en affichant l'échiquier après chaque coup.
     *
     * @param args
     */
    public static void main(String args[]) {

        /**
         * Historique des mouvement d'un joueur
         * L'identificant correspond au tour du jeu
         * La valeur correspond au mouvement du joueur
         */
        Map<Integer, String> moves = new HashMap<Integer, String>();
        /**
         * Historique du mouvement des joueurs
         * L'identifiant correspond à la couleur des pièeces du joueur
         * La valeur correspond à l'historique de ses mouvement pour chaque tour
         */
        Map<String, Map<Integer, String>> player_moves = new HashMap<String, Map<Integer, String>>();

        Scanner scan = new Scanner( System.in );

        System.out.println("Enter player playing the white pieces : ");
        String whiteplayer = scan.nextLine();

        System.out.println("Enter player playing the black pieces : ");
        String blackplayer = scan.nextLine();

        Game game = new Game(whiteplayer, blackplayer);
        // Suprime l'historique de la partie précédente
        moves.clear();
        player_moves.clear();

        // Nombre de tours
        int turn = 0;
        System.out.println("Terminal based chess game!, '/q' to quit and '/h' to view move history");
        try ( Scanner scanner = new Scanner( System.in ) ) {
            while(true) {
                String currentPlayerName = game.getCurrentColor().equals(Color.WHITE) ? game.getWhitePlayerName() : game.getBlackPlayerName();
                System.out.print(currentPlayerName + ", what piece to move ? : ");
                String input = scanner.nextLine();

                String[] commandParts = input.split(" ");
                String baseCommand = commandParts.length > 0 ? commandParts[0] : null;
                String player = commandParts.length > 1 ? commandParts[1] : null;
                Integer n_move = commandParts.length > 2 ? Integer.parseInt(commandParts[2]) : null;

                /**
                 * Commande permettant de voir l'historique des mouvement d'un joueur
                 * Exemple: /h pour voir l'historique des 2 joueurs.
                 *          /h black pour voir l'historique du joueur avec les pièces noires.
                 *          /h white 10 pour voir le mouvement fait par le joueur avec les pièces blanches au 11ème tour.
                 */
                if(Objects.equals(input.substring(0, 2), "/h")) {
                    if (turn != 0) {
                        System.out.println(n_move);
                        if (n_move != null) {
                            assert false;
                            System.out.println(player.substring(0, 1).toUpperCase() + player.substring(1) + " moves: " + player_moves.get(player).get(n_move));
                        } else if (player != null) {
                            System.out.println(player.substring(0, 1).toUpperCase() + player.substring(1) + " move: " + player_moves.get(player).toString());
                        } else {
                            System.out.println("Black moves: " + player_moves.get("black").toString() + "\n" + "White moves: " + player_moves.get("white").toString());
                        }
                    }
                    continue;
                }
                /**
                 * Commande permettant de quitter la partie actuelle
                 */
                else if (Objects.equals(input.substring(0, 2), "/q")) {
                    break;
                }
                String position = input;
                Piece chess_piece = null;
                try {
                    chess_piece = game.board.getPiece(new Position(position));
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e);
                }
                assert chess_piece != null;
                if (chess_piece.getColor() != game.getCurrentColor()) {
                    System.out.println("You can't move this piece " + currentPlayerName + "!\nPlease play a move for the corresponding color piece.");
                    continue;
                }
                System.out.print("Where to move this piece ? : ");
                String next_move = scanner.nextLine();
                Position next_position = new Position(next_move);
                try {
                    game.turn(chess_piece.getPosition(), next_position);
                    moves.put(turn, "From " + new Position(position).toAlgebraicNotation() + " to " + new Position(next_move).toAlgebraicNotation());
                    player_moves.put(game.getCurrentColor().toString().toLowerCase(Locale.ROOT), moves);
                } catch (ChessMoveException e) {
                    e.printStackTrace();
                }
                System.out.println(game.board.toString());
                turn++;
            }
        }
    }
}

package chess;

import chess.pieces.*;
import chess.util.ChessMoveException;
import chess.util.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class Main extends Application {

    private GridPane echiquier;
    private int turn = 0;
    private Text[][] text_piece;
    private Piece[][] original_pieces;
    private chess.util.Color currentColor = chess.util.Color.WHITE;
    Text current_turn;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Damier");

            // Une petite piece pour la déplacer
            Text cavalier_noir = new Text(Character.toString('♞'));
            cavalier_noir.setFont(Font.font("Verdana", 40));
            // Comment en HTML avec la propiété 'class'
            // Les caractèristiques sont spécifiées dans la feuille de style associée
            cavalier_noir.getStyleClass().addAll("piece","black");

            // Une autre petite image pour vpoir le CSS
            Text cavalier_blanc = new Text(Character.toString('♘'));
            cavalier_blanc.setFont(Font.font("Verdana", 40));
            // Comment en HTML avec la propiété 'class'
            // Les caractèristiques sont spécifiées dans la feuille de style associée
            cavalier_blanc.getStyleClass().addAll("piece","white");



            // On prend un BorderPane : on a donc les zones top, left, center, right, bottom
            BorderPane root = new BorderPane();
            Chessboard board = new Chessboard();
            this.original_pieces = board.getPieces();
            Piece[][] piece = board.getPieces();
            // l'échiquier
            this.echiquier = new GridPane();
            echiquier.setAlignment(Pos.CENTER);

            // Le contour
            for (int nombre = 8; nombre > 0; nombre--) {
                Text t;
                t = new Text(Character.toString('A' + nombre-1));
                GridPane.setHalignment(t, HPos.CENTER);
                echiquier.add(t, nombre, 0);

                t = new Text(Character.toString('A' + nombre-1));
                GridPane.setHalignment(t, HPos.CENTER);
                echiquier.add(t, nombre, 9);

                t = new Text(Character.toString('0' + nombre));
                GridPane.setMargin(t, new Insets(0.0, 10.0, 0.0, 10.0));
                echiquier.add(t, 0, nombre);

                t = new Text(Character.toString('0' + nombre));
                GridPane.setMargin(t, new Insets(0.0, 10.0, 0.0, 10.0));
                echiquier.add(t, 9, nombre);
            }

            // Les cases
            for (int ligne = 0; ligne < 8; ligne++) {
                for (int colonne = 0; colonne < 8; colonne++) {
                    Rectangle r = new Rectangle(50, 50, (ligne + colonne) % 2 == 0 ? Color.WHITE : Color.GAINSBORO);
                    echiquier.add(r, colonne + 1, ligne + 1);
                }
            }

            setPieces(board, chess.util.Color.WHITE);

            /* Pour démo : je pose le cavalier noir
            echiquier.add(cavalier_noir, 3, 2);
            echiquier.add(cavalier_blanc, 6, 5);
            */

            // Les zones de saisie
            HBox saisie = new HBox();
            final Text texte_choix = new Text("Spécifiez votre coup :\nEx: A2A4 ");
            TextField coupTF = new TextField();

            // Bouton : jouer ce coup
            Button jouer_coup = new Button("Jouez ce coup");
            jouer_coup.setOnAction(e -> {
                String choix = coupTF.getText();
                if (choix.trim().length() < 4)
                    alert("Votre proposition '" + choix + "' doit contenir au moins 4 caractères");
                else if (this.currentColor != board.getPiece(new Position(choix.substring(0, 2))).getColor()) {
                    alert("Vous ne pouvez jouer une pièce qui n'est pas de votre couleur");
                }
                else {
                    String origine = choix.substring(0, 2);
                    String destination = choix.substring(2, 4);
                    alert("Vous avez demandé de déplacer la pièce\nde la case '" + origine + "' à la case '"
                            + destination + "'");

                    Position start = new Position(origine);
                    System.out.println(start.toAlgebraicNotation());
                    Position end = new Position(destination);
                    System.out.println(end.toAlgebraicNotation());
                    Piece chess_piece = board.getPiece(start);
                    System.out.println(chess_piece.getSymbol());
                    System.out.println(chess_piece.getPosition());
                    try {
                        chess_piece.moveTo(end);
                        deplace(text_piece[start.getX()][start.getY()], echiquier, end.getY()+1, end.getX()+1);
                        this.turn++;
                        current_turn.setText("Current Turn : " + String.valueOf(this.turn));
                    } catch (ChessMoveException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(start.getX() + " " + start.getY() + " | " + end.getX() + " " + end.getX());
                    System.out.println(board);
                }
            });

            // Bouton : montrer les déplacements possibles pour la pièce spécifiée dans la zone de saisie
            Button montrer_possible = new Button("Montrer les possibilités");
            montrer_possible.setOnAction(
                    e -> /*alert("La présentation des déplacements possibles\n n'est pas encore disponible")*/
                    {
                        String choix = coupTF.getText();

                        if (choix.trim().length() < 2)
                            alert("Votre proposition '" + choix + "' doit contenir au moins 2 caractères");
                        else if (board.getPiece(new Position(choix.substring(0, 2))) != null){
                            String origine = choix.substring(0, 2);

                            Position start = new Position(origine);

                            Piece chess_piece = board.getPiece(start);
                            System.out.println(chess_piece.getSymbol() + " " + chess_piece.getPosition());
                            alert("Voici les différentes possibilités pour la pièce actuelle");
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; y++) {
                                    Position destination = new Position(x, y);
                                    String other_color = board.getPiece(destination) == null ? "No piece": board.getPiece(destination).getColor().toString();
                                    if(chess_piece.isValidMove(new Position(x, y)) && !other_color.equals(chess_piece.getColor().toString())) {
                                        System.out.println(x + " and the " + y);
                                        if (this.getNodeFromGridPane(echiquier, x, y) instanceof Rectangle) {
                                            Rectangle r = (Rectangle) this.getNodeFromGridPane(echiquier, y+1, x+1);

                                            System.out.println(r);
                                            assert r != null;
                                            Paint original = r.getFill();
                                            r.setFill(Color.YELLOW);
                                            Timeline timeline = new Timeline(new KeyFrame(
                                                    Duration.millis(10000),
                                                    ae ->
                                                    {
                                                        r.setFill(original);
                                                    }));
                                            timeline.play();
                                        }
                                    }
                                }
                            }

                        }
                    }
            );


            saisie.getChildren().addAll(texte_choix, coupTF, jouer_coup, montrer_possible);

            // Les boutons de gestion de l'application
            GridPane boutons = new GridPane();
            boutons.setAlignment(Pos.CENTER);
            Button demarre_blanc = new Button("Je (re)démarre en blanc");

            demarre_blanc.setOnAction(e -> {
                for(Text[] chars : this.text_piece) {
                    for (Text pieces : chars) {
                        System.out.println(pieces);
                        echiquier.getChildren().remove(pieces);
                    }
                }
                setPieces(board, chess.util.Color.WHITE);
                this.turn = 0;
                current_turn.setText("Current Turn : " + String.valueOf(this.turn));
            });

            Button demarre_noir = new Button("Je (re)démarre en noir");

            demarre_noir.setOnAction(e ->
            {
                for(Text[] chars : this.text_piece) {
                    for (Text pieces : chars) {
                        System.out.println(pieces);
                        echiquier.getChildren().remove(pieces);
                    }
                }
                setPieces(board, chess.util.Color.BLACK);
                this.turn = 0;
                current_turn.setText("Current Turn : " + String.valueOf(this.turn));
            });

            Button quitter = new Button("Quitter");
            // Appel de la lambda function sur le clic du bouton 'quitter'
            quitter.setOnAction(e -> Platform.exit());

            current_turn = new Text("Current Turn : " + this.turn);
            GridPane.setHalignment(current_turn, HPos.LEFT);
            GridPane.setMargin(current_turn, new Insets(0.0, 10.0, 0.0, 10.0));
            boutons.add(current_turn, 0, 0);
            boutons.add(demarre_blanc, 1, 0);
            boutons.add(demarre_noir, 2, 0);
            boutons.add(quitter, 3, 0);

            // crée la scène (contenu de la fenêtre principale
            Scene scene = new Scene(root, 500, 500);

            // On remplit verticalement les 3 zones centrales de l'application
            root.setTop(echiquier);
            root.setCenter(saisie);
            root.setBottom(boutons);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);

            // On lance l'affichage
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Déplacement de la pièce
     *
     * @param piece            Pièce à déplacer
     * @param grille_echiquier Grille
     * @param colonne          Nouvelle colonne
     * @param ligne            Nouvelle ligne
     */
    private void deplace(Text piece, GridPane grille_echiquier, int colonne, int ligne) {
        grille_echiquier.getChildren().remove(piece);
        System.out.println("Ligne " + ligne + " Colonne " + colonne);
        System.out.println("Move this piece : " + piece.getText());
        grille_echiquier.add(piece, colonne, ligne);
        int x = -1;
        int y = -1;
        for(int i = 0, len = text_piece.length; i < len; i++) {
            for(int j = 0, length = text_piece[i].length; j < length; j++) {
                System.out.print(text_piece[i][j] != null ? text_piece[i][j].getText() + " " : "");
                if (text_piece[i][j] == piece) {
                    x = i;
                    y = j;
                }
            }
            System.out.println();
        }
        System.out.println(x + " & " + y);
        Text temp = text_piece[x][y];
        System.out.println(temp.getText());
        text_piece[x][y] = null;

        text_piece[ligne-1][colonne-1] = temp;
        System.out.println(text_piece[ligne-1][colonne-1].getText());
        this.currentColor = currentColor == chess.util.Color.BLACK ? chess.util.Color.WHITE : chess.util.Color.BLACK;
    }

    /**
     * Message bloquant
     *
     * @param message Le message
     */
    private void alert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Attention !");
        alert.setHeaderText("Message d'alerte");
        alert.setContentText(message);

        alert.showAndWait();
    }


    /** Point d'entrée dans le programme
     *
     * @param args Les arguments d'exécution
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Chargement d'un échiquier à partir d'un tableau de pièces décrites sur la
     * base de la notation algébrique
     * {@link <a href="https://fr.wikipedia.org/wiki/Notation_alg%C3%A9brique">...</a>} en 4 lettres
     * représentant : Type {Roi,Dame,Fou,Cavalier,Tour,Pion} Couleur {Blanc,Noir}
     * Colonne {A-H} Ligne {1-8} <br>
     * Ainsi, "RNA6" représente le Roi Noir en position A6.
     *
     * @param listePieces Le tableau des pièces
     */
    public void charge(String[] listePieces) {
        String[] defaut = new String[0];
        if (this.turn == 1) {
            defaut = new String[]{
                    // Pieces noires
                    "TNA8", "CNB8", "FNC8", "DND8", "RNE8", "FNF8", "CNG8", "TNH8",
                    // Pions noirs
                    "PNA7", "PNB7", "PNC7", "PND7", "PNE7", "PNF7", "PNG7", "PNH7",
                    // Pions blancs
                    "PBA2", "PBB2", "PBC2", "PBD2", "PBE2", "PBF2", "PBG2", "PBH2",
                    // Pièces blanches
                    "TBA1", "CBB1", "FBC1", "RBD1", "DBE1", "FBF1", "CBG1", "TBH1"};
            for (String pieces : defaut) {
            }
        }
    }

    private void setPieces(Chessboard board, chess.util.Color switchColor) {
        // Les Pieces
        this.text_piece = new Text[8][8];
        board.setBoardPieces(this.original_pieces);
        if(switchColor == chess.util.Color.WHITE) {
            for (int ligne = 0; ligne < 8; ligne++) {
                for (int colonne = 0; colonne < 8; colonne++) {
                    Text piece_char;
                    if (board.getPiece(ligne, colonne) != null) {
                        Piece chess_piece = board.getPiece(ligne, colonne);
                        if(this.currentColor != switchColor)
                            chess_piece.setColor(chess_piece.getColor() == chess.util.Color.WHITE ? chess.util.Color.BLACK : chess.util.Color.WHITE);
                        piece_char = new Text(Character.toString(chess_piece.getSymbol()));
                        piece_char.setTextAlignment(TextAlignment.CENTER);
                        System.out.print(piece_char.getText() + " ");
                        text_piece[ligne][colonne] = piece_char;
                        piece_char.setFont(Font.font("Verdana", 40));
                        piece_char.getStyleClass().addAll("piece", chess_piece.getColor().toString().toLowerCase(Locale.ROOT));
                        echiquier.add(piece_char, colonne + 1, ligne + 1);
                    }
                }
                System.out.println("");
            }
            this.currentColor = switchColor;
        }
        else if(switchColor == chess.util.Color.BLACK) {
            //Collections.reverse(Arrays.asList(board.getPieces()));
            for (int ligne = 0; ligne < 8; ligne++) {
                for (int colonne = 0; colonne < 8; colonne++) {
                    Text piece_char;
                    if (board.getPiece(ligne, colonne) != null) {
                        Piece chess_piece = board.getPiece(ligne, colonne);
                        if(this.currentColor != switchColor)
                            chess_piece.setColor(chess_piece.getColor() == chess.util.Color.WHITE ? chess.util.Color.BLACK : chess.util.Color.WHITE);
                        piece_char = new Text(Character.toString(chess_piece.getSymbol()));
                        piece_char.setTextAlignment(TextAlignment.CENTER);
                        System.out.print(piece_char.getText() + " ");
                        text_piece[ligne][colonne] = piece_char;
                        piece_char.setFont(Font.font("Verdana", 40));
                        piece_char.getStyleClass().addAll("piece", chess_piece.getColor().toString().toLowerCase(Locale.ROOT));
                        echiquier.add(piece_char, colonne + 1, ligne + 1);
                    }
                }
                System.out.println("");
            }
            this.currentColor = switchColor;
        }
        System.out.println(board);
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}

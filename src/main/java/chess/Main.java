package chess;

import chess.pieces.*;
import chess.util.AI;
import chess.util.ChessMoveException;
import chess.util.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class Main extends Application {

    private GridPane echiquier;
    private int turn = 0;
    private Text[][] text_piece;
    private Piece[][] original_pieces;
    private chess.util.Color currentColor = chess.util.Color.WHITE;
    private Text current_turn;
    private ArrayList<String> move_historic;

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

            System.out.println("Current color playing : " + currentColor.name());
            // l'échiquier
            this.echiquier = new GridPane();
            echiquier.setAlignment(Pos.BASELINE_LEFT);

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

            /**
             * Conteneur pour l'affichage de l'historique des coups
             */
            VBox historic = new VBox();
            Text desc = new Text("L'historique des coup d'échecs :");
            TextArea historic_field = new TextArea();
            //historic_field.setAlignment(Pos.TOP_LEFT);
            historic_field.setPrefSize(200, 400);
            historic_field.setEditable(false);
            setupPieces(board, chess.util.Color.WHITE);
            GridPane.setValignment(historic_field, VPos.TOP);
            historic.getChildren().addAll(desc, historic_field);

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
                Piece piece = board.getPiece(new Position(choix.substring(0, 2)));
                if (piece != null && this.currentColor != piece.getColor()) {
                    System.out.println(board.getPiece(new Position(choix.substring(0, 2))).getSymbol() + " " + board.getPiece(new Position(choix.substring(0, 2))).getPosition() + " " + board.getPiece(new Position(choix.substring(0, 2))).getColor().name());
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


                    try {
                        chess_piece.moveTo(end);
                        deplace(text_piece[start.getX()][start.getY()], echiquier, end.getY()+1, end.getX()+1);
                        this.turn++;
                        current_turn.setText("Current Turn : " + String.valueOf(this.turn));
                        move_historic.add(turn + ". " + chess_piece.getName().charAt(0) + chess_piece.getColor().name().charAt(0) + start.toAlgebraicNotation() + "-" + chess_piece.getPosition().toAlgebraicNotation());
                        historic_field.setText(move_historic.toString());
                    } catch (ChessMoveException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(start.getX() + " " + start.getY() + " | " + end.getX() + " " + end.getY());
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
                                    Position pos = new Position(x, y);
                                    if(chess_piece.isValidMove(pos) && !other_color.equals(chess_piece.getColor().toString())) {
                                        System.out.println(x + "; " + y);
                                        System.out.println("Can move to : " + pos.toAlgebraicNotation());
                                        if (this.getNodeFromGridPane(echiquier, x, y) instanceof Rectangle) {
                                            /**
                                             * Affichage graphique des différents mouvements possibles d'une pièce
                                             */
                                            Rectangle r = (Rectangle) this.getNodeFromGridPane(echiquier, y+1, x+1);

                                            System.out.println(r);
                                            assert r != null;
                                            Paint original = r.getFill();
                                            r.setFill(Color.YELLOW);
                                            Timeline timeline = new Timeline(new KeyFrame(
                                                    Duration.millis(5000),
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
            boutons.setAlignment(Pos.BASELINE_LEFT);
            Button demarre_blanc = new Button("Je (re)démarre en blanc");

            /**
             * Changer de côté (Noir)
             */
            demarre_blanc.setOnAction(e -> {
                for(Text[] chars : this.text_piece) {
                    for (Text pieces : chars) {
                        echiquier.getChildren().remove(pieces);
                    }
                }
                setupPieces(board, chess.util.Color.WHITE);
                this.turn = 0;
                current_turn.setText("Current Turn : " + String.valueOf(this.turn));
                System.out.println("Current color playing : " + currentColor.name());
            });

            Button demarre_noir = new Button("Je (re)démarre en noir");


            /**
             * Changer de côté (Noir)
             */
            demarre_noir.setOnAction(e ->
            {
                for(Text[] chars : this.text_piece) {
                    for (Text pieces : chars) {
                        echiquier.getChildren().remove(pieces);
                    }
                }
                setupPieces(board, chess.util.Color.BLACK);
                this.turn = 0;
                current_turn.setText("Current Turn : " + String.valueOf(this.turn));
                System.out.println("Current color playing : " + currentColor.name());
            });

            Button quitter = new Button("Quitter");
            // Appel de la lambda function sur le clic du bouton 'quitter'
            quitter.setOnAction(e -> Platform.exit());


            /**
             * Action de l'Intelligence Artificielle pendant l'appui sur le bouton
             */
            Button AIMove = new Button("Coup de l'IA");
            AIMove.setOnAction(e -> {
               AI ai = new AI();
                try {
                    Chessboard copy = board.clone();
                    //Position[] moves = ai.getBestPossibleMove(copy, currentColor);
                    Position[] randomAiMoves = ai.makeRandomValidMove(board, currentColor);
                    Position start = randomAiMoves[0];
                    Position end = randomAiMoves[1];
                    System.out.println(Arrays.toString(randomAiMoves));

                    System.out.println(start + "->" + start.toAlgebraicNotation() + " " + end + "->" + end.toAlgebraicNotation());

                    System.out.println(board);
                    Piece chess_piece = null;
                    try {
                        Position original_start = (Position) start.clone();
                        chess_piece= board.getPiece(start);
                        chess_piece.moveTo(end);
                        System.out.println(end);
                        System.out.println(original_start);
                        deplace(text_piece[original_start.getX()][original_start.getY()], echiquier, end.getY()+1, end.getX()+1);
                    }
                    catch (Exception ignored) {

                    }
                    this.turn++;

                    current_turn.setText("Current Turn : " + String.valueOf(this.turn));
                    move_historic.add(turn + ". " + chess_piece.getName().charAt(0) + chess_piece.getColor().name().charAt(0) + start.toAlgebraicNotation() + "-" + chess_piece.getPosition().toAlgebraicNotation());
                    historic_field.setText(move_historic.toString());
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                }
            });

            current_turn = new Text("Current Turn : " + this.turn);
            GridPane.setHalignment(current_turn, HPos.LEFT);
            GridPane.setMargin(current_turn, new Insets(0.0, 10.0, 0.0, 10.0));
            boutons.add(current_turn, 0, 0);
            boutons.add(demarre_blanc, 1, 0);
            boutons.add(demarre_noir, 2, 0);
            boutons.add(AIMove, 3, 0);
            boutons.add(quitter, 4, 0);

            // crée la scène (contenu de la fenêtre principale
            Scene scene = new Scene(root, 700, 520);

            // conteneur des zones de saisies et des boutons
            VBox container = new VBox();
            container.getChildren().addAll(saisie, boutons);

            // On remplit verticalement les 3 zones centrales de l'application
            root.setLeft(echiquier);
            root.setCenter(historic);
            root.setBottom(container);
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
        System.out.println("Move this piece : " + piece.getText());
        System.out.println(this.getNodeFromGridPane(grille_echiquier, colonne, ligne));
        if(this.getNodeFromGridPane(grille_echiquier, colonne, ligne) instanceof Text) {
            System.out.println(this.getNodeFromGridPane(grille_echiquier, colonne, ligne));
            grille_echiquier.getChildren().remove(this.getNodeFromGridPane(grille_echiquier, colonne+1, ligne+1));
        }
        grille_echiquier.add(piece, colonne, ligne);
        int x = -1;
        int y = -1;
        for(int i = 0, len = text_piece.length; i < len; i++) {
            for(int j = 0, length = text_piece[i].length; j < length; j++) {
                if (text_piece[i][j] == piece) {
                    x = i;
                    y = j;
                }
            }
        }
        Text temp = text_piece[x][y];

        text_piece[x][y] = null;
        text_piece[ligne-1][colonne-1] = temp;

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

    /**
     * Permet de mettre en place les pièces de l'échiquier
     *
     * @param board l'échiquier
     * @param switchColor la couleur de départ du joueur principal
     */
    private void setupPieces(Chessboard board, chess.util.Color switchColor) {
        // Les Pieces
        this.move_historic =  new ArrayList<>();
        this.text_piece = new Text[8][8];
        board.reset();
        if(switchColor == chess.util.Color.WHITE) {
            for (int ligne = 0; ligne < 8; ligne++) {
                for (int colonne = 0; colonne < 8; colonne++) {
                    Text piece_char;
                    if (board.getPiece(ligne, colonne) != null) {
                        Piece chess_piece = board.getPiece(ligne, colonne);
                        piece_char = new Text(Character.toString(chess_piece.getSymbol()));
                        piece_char.setTextAlignment(TextAlignment.CENTER);
                        text_piece[ligne][colonne] = piece_char;
                        piece_char.setFont(Font.font("Verdana", 40));
                        piece_char.getStyleClass().addAll("piece", chess_piece.getColor().toString().toLowerCase(Locale.ROOT));
                        echiquier.add(piece_char, colonne + 1, ligne + 1);
                    }
                }
            }
            this.currentColor = switchColor;
        }

        else if(switchColor == chess.util.Color.BLACK) {
            // Switch color of all the pieces
            for(int col = 0; col < 8; col++) {
                board.setColor(0, col, chess.util.Color.BLACK);
                board.setPosition(0, col, new Position(6, col));

                board.setColor(1, col, chess.util.Color.BLACK);
                board.setPosition(1, col, new Position(7, col));

                board.setColor(6, col, chess.util.Color.WHITE);
                board.setPosition(6, col, new Position(0, col));
                board.setColor(7, col, chess.util.Color.WHITE);
                board.setPosition(7, col, new Position(1, col));
            }
            //needs to swap the rows
            Collections.reverse(Arrays.asList(board.getPieces()));
            for (int ligne = 0; ligne < 8; ligne++) {
                for (int colonne = 0; colonne < 8; colonne++) {
                    Text piece_char;
                    if (board.getPiece(ligne, colonne) != null) {
                        Piece chess_piece = board.getPiece(ligne, colonne);
                        piece_char = new Text(Character.toString(chess_piece.getSymbol()));
                        piece_char.setTextAlignment(TextAlignment.CENTER);
                        text_piece[ligne][colonne] = piece_char;
                        piece_char.setFont(Font.font("Verdana", 40));
                        piece_char.getStyleClass().addAll("piece", chess_piece.getColor().toString().toLowerCase(Locale.ROOT));
                        echiquier.add(piece_char, colonne + 1, ligne + 1);
                    }
                }
            }
            this.currentColor = switchColor;
        }
        System.out.println(board);
    }

    /**
     * Permet d'obtenir l'élement à la position spécifiée dans le grillage
     *
     * @param gridPane le grillage
     * @param col la colonne
     * @param row la ligne
     * @return l'élement Node du grillage GridPane
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}

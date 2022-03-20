package chess.pieces;

import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.Chessboard;

public abstract class Piece implements Cloneable {
    private Position position;
    private final char symbol;
    private Color color;
    private String name;
    protected Chessboard board;

    /**
     * Constructeur instanciant une pièce d'échec
     *
     * @param chessboard Echiquier auquel la pièce appartient
     * @param position Position de la pièce sur l'échiquier
     * @param color Couleur de la pièce (Color.WHITE ou Color.BLACK)
     * @param name Nom de la pièce (Roi, Reine, ...)
     * @param symbol Symbole de la pièce
     */
    public Piece (Chessboard chessboard, Position position, Color color, String name, char symbol) {
        this.board = chessboard;
        this.position = position;
        this.color = color;
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * Retourne la position de la pièce sur l'échiquier.
     *
     * @return La position de la pièece sur l'échiquier
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Retourne le symbole de la pièce
     *
     * @return Le symbole de la pièece
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Retourne la couleur de la pièce
     *
     * @return La couleur de la pièece
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retourne le nom de la pièce.
     *
     * @return Le nom de la pièece
     */
    public String getName() {
        return name;
    }

    /**
     * Teste la couleur de la pièce.
     *
     * @return true si la pièce est noire, false sinon
     */
    public boolean isBlack() {
        return color.name() == "BLACK";
    }

    /**
     * Teste la couleur de la pièce.
     *
     * @return true si la pièce est blanche, false sinon
     */
    public boolean isWhite() {
        return color.name() == "WHITE";
    }

    /**
     * Modifie la position d'une pièece
     *
     * @param pos La nouvelle position de la pièece
     */
    public void setPosition(Position pos) {
        this.position.setPosition(pos);
    }

    /**
     * Deplace la pièce sur la case indiquée.
     *
     * @param destination Position de la case de destination du déplacement.
     * @throws ChessMoveException Si le mouvement n'est pas possible
     */
    public void moveTo(Position destination) throws ChessMoveException {

    }

    /**
     * Teste la validité d'un déplacement
     *
     * @param destination Le déplacement
     */
    public abstract boolean isValidMove(Position destination);

    public void setColor(chess.util.Color color) {
        this.color = color;
    }

    public Chessboard getBoard() {
        return this.board;
    }

    public void setBoard(Chessboard board) {
        this.board = board;
    }

    public Piece(Piece p) {
        this(p.getBoard(), p.getPosition(), p.getColor(), p.getName(), p.getSymbol());
    }

    public abstract Object clone();

    public Object superClone() throws CloneNotSupportedException {
        return super.clone();
    }
}
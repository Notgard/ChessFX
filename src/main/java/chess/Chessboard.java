package chess;

import chess.pieces.*;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;

import java.util.Arrays;

/**
 * Classe représentant un échiquier lors d'une partie d'échecs
 */
public class Chessboard {

    /**
     * Tableau de pièces représentant les cases de l'échiquier (une case vide vaut null).
     */
    private Piece[][] pieces;

    public Position[] lastMove;
    /**
     * Construit un échiquier
     */
    public Chessboard() {

        /**
         * Dans cette achitecture, les valeurs x, y correspondent au valeur ligne et colonne du
         * tableau 2D ci-dessous.
         */
        this.lastMove = new Position[2];

        this.pieces = new Piece[8][8];

        this.pieces[0] = new Piece[]{
                new Rook(this, new Position(0, 0), Color.WHITE),
                new Knight(this, new Position(0, 1), Color.WHITE),
                new Bishop(this, new Position(0, 2), Color.WHITE),
                new Queen(this, new Position(0, 3), Color.WHITE),
                new King(this, new Position(0, 4), Color.WHITE),
                new Bishop(this, new Position(0, 5), Color.WHITE),
                new Knight(this, new Position(0, 6), Color.WHITE),
                new Rook(this, new Position(0, 7), Color.WHITE)
        };
        this.pieces[1] = new Piece[]{
                new Pawn(this, new Position(1, 0), Color.WHITE),
                new Pawn(this, new Position(1, 1), Color.WHITE),
                new Pawn(this, new Position(1, 2), Color.WHITE),
                new Pawn(this, new Position(1, 3), Color.WHITE),
                new Pawn(this, new Position(1, 4), Color.WHITE),
                new Pawn(this, new Position(1, 5), Color.WHITE),
                new Pawn(this, new Position(1, 6), Color.WHITE),
                new Pawn(this, new Position(1, 7), Color.WHITE)
        };

        this.pieces[6] = new Piece[]{
                new Pawn(this, new Position(6, 0), Color.BLACK),
                new Pawn(this, new Position(6, 1), Color.BLACK),
                new Pawn(this, new Position(6, 2), Color.BLACK),
                new Pawn(this, new Position(6, 3), Color.BLACK),
                new Pawn(this, new Position(6, 4), Color.BLACK),
                new Pawn(this, new Position(6, 5), Color.BLACK),
                new Pawn(this, new Position(6, 6), Color.BLACK),
                new Pawn(this, new Position(6, 7), Color.BLACK)
        };

        this.pieces[7] = new Piece[]{
                new Rook(this, new Position(7, 0), Color.BLACK),
                new Knight(this, new Position(7, 1), Color.BLACK),
                new Bishop(this, new Position(7, 2), Color.BLACK),
                new Queen(this, new Position(7, 3), Color.BLACK),
                new King(this, new Position(7, 4), Color.BLACK),
                new Bishop(this, new Position(7, 5), Color.BLACK),
                new Knight(this, new Position(7, 6), Color.BLACK),
                new Rook(this, new Position(7, 7), Color.BLACK)
        };

    }

    /**
     * Retourne une chaîne de caractères représentant l'échiquier.
     *
     * @return L'échiquier avec les pièces positionnées correctement
     */
    @Override
    public String toString() {
        int lines = 7;
        StringBuilder top = new StringBuilder(" ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓\n");
        top.insert(0, "   A   B   C   D   E   F   G   H\n");
        for (int x = 14; x >= 0; x--) {
            StringBuilder line;
            if (x % 2 == 0) {
                int n = 0;
                line = new StringBuilder("┃   ┃   ┃   ┃   ┃   ┃   ┃   ┃   ┃");
                for (int i = 2; i < 34; i += 4) {
                    if (pieces[lines][n] != null)
                        line.setCharAt(i, pieces[lines][n].getSymbol());
                    n++;
                }
                lines--;
                line.insert(0, Character.forDigit(lines + 2, 10));
                line.append(lines + 2).append("\n");
                top.append(line);
            } else {
                line = new StringBuilder(" ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫\n");
                top.append(line);
            }
        }
        StringBuilder bottom = new StringBuilder(" ┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛\n");
        top.append(bottom);
        top.append("   A   B   C   D   E   F   G   H\n");
        return top.toString();
    }

    /**
     * Retourne la pièce de la case (x,y) de l'échiquier ou null si la case est vide
     *
     * @param x Abscisse de la case (0 à 7)
     * @param y Ordonnée de la case (0 à 7)
     * @return Pièce située sur la case ou null si la case est vide
     */
    public Piece getPiece(int x, int y) {
        return pieces[x][y] != null ? pieces[x][y] : null;
    }

    /**
     * Retourne la pièce de la case indiquée par pos ou null si la case est vide
     *
     * @param pos Position de la case
     * @return Pièce située sur la case ou null si la case est vide
     */
    public Piece getPiece(Position pos) {
        Piece piece = pieces[pos.getX()][pos.getY()] != null ? pieces[pos.getX()][pos.getY()] : null;
        return piece;
    }

    /**
     * Remplace la pièce située sur la case indiquée
     *
     * @param pos Position de la case de destination
     * @param newPiece Nouvelle pièce de la case
     */
    public void setPiece(Position pos, Piece newPiece) {
        Position oldPos = newPiece.getPosition();
        pieces[pos.getX()][pos.getY()] = newPiece;
        System.out.println("Moved from: " + oldPos.toAlgebraicNotation() + " To: " + pos.toAlgebraicNotation());
        this.lastMove[0] = new Position(oldPos.toAlgebraicNotation());
        this.lastMove[1] = pos;
        pieces[oldPos.getX()][oldPos.getY()] = null;
        newPiece.setPosition(pos);
    }

    /**
     * Teste la présence d'une pièce sur la diagonale comprise entre les positions start et end (exclues)
     *
     * @param start Première extrémité
     * @param end Seconde extrémité
     * @return true s'il y a une pièce sur la diagonale comprise entre les positions start et end (exclues), false sinon.
     */
    public boolean isPiecePresentOnSameDiagonalBetween(Position start, Position end) {
        boolean isPossible = false;
        //check and swap
        if (end.getX() > start.getX()) {
            Position swap = start;
            start = end;
            end = swap;
        }
        int this_x = start.getX();
        int this_y = start.getY();

        if (end.isOnSameDiagonalAs(start) && end.getY() < this_y) {
            //descend column and check if any obstacles in between
            for (int x = this_x, y = this_y; x > end.getX() && y > end.getY(); x--, y--) {
                isPossible = this.getPiece(x, y) == null;
            }
        } else if (end.isOnSameDiagonalAs(start) && end.getY() > this_y) {
            //descend column and check if any obstacles in between
            for (int x = this_x, y = this_y; x > end.getX() && y < end.getY(); x--, y++) {
                isPossible = this.getPiece(x, y) == null;
            }
        }
        return isPossible;
    }

    /**
     * Teste la présence d'une pièce sur la ligne comprise entre les positions start et end (exclues)
     *
     * @param start Première extrémité
     * @param end Seconde extrémité
     * @return true s'il y a une pièce sur la ligne comprise entre les positions start et end (exclues), false sinon.
     */
    public boolean isPiecePresentOnSameLineBetween(Position start, Position end) {
        boolean isPossible = false;
        if (end.isOnSameLineAs(start)) {
            //check and swap
            if (end.getY() < start.getY()) {
                Position swap = start;
                start = end;
                end = swap;
            }
            int this_x = start.getX();
            int this_y = start.getY();
            //descend column and check if any obstacles in between
            for (int y = this_y; y < end.getY(); y++) {
                isPossible = this.getPiece(this_x, y) == null;
            }
        }
        return isPossible;
    }

    /**
     * Teste la présence d'une pièce sur la colonne comprise entre les positions start et end (exclues)
     *
     * @param start Première extrémité
     * @param end Seconde extrémité
     * @return true s'il y a une pièce sur la colonne comprise entre les positions start et end (exclues), false sinon.
     */
    public boolean isPiecePresentOnSameColumnBetween(Position start, Position end) {
        boolean isPossible = false;
        if (end.isOnSameColumnAs(start)) {
            //check and swap
            if (end.getX() < start.getX()) {
                Position swap = start;
                start = end;
                end = swap;
            }
            int this_x = start.getX();
            int this_y = start.getY();
            //descend column and check if any obstacles in between

            for (int x = end.getX(); x > this_x; x--) {
                isPossible = this.getPiece(x, this_y) == null;
            }
        }
        return isPossible;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    void setBoardPieces(Piece[][] newPieces) {
        this.pieces = newPieces;
    }

    public void reset() {
        this.lastMove = new Position[2];

        this.pieces = new Piece[8][8];

        this.pieces[0] = new Piece[]{
                new Rook(this, new Position(0, 0), Color.WHITE),
                new Knight(this, new Position(0, 1), Color.WHITE),
                new Bishop(this, new Position(0, 2), Color.WHITE),
                new Queen(this, new Position(0, 3), Color.WHITE),
                new King(this, new Position(0, 4), Color.WHITE),
                new Bishop(this, new Position(0, 5), Color.WHITE),
                new Knight(this, new Position(0, 6), Color.WHITE),
                new Rook(this, new Position(0, 7), Color.WHITE)
        };
        this.pieces[1] = new Piece[]{
                new Pawn(this, new Position(1, 0), Color.WHITE),
                new Pawn(this, new Position(1, 1), Color.WHITE),
                new Pawn(this, new Position(1, 2), Color.WHITE),
                new Pawn(this, new Position(1, 3), Color.WHITE),
                new Pawn(this, new Position(1, 4), Color.WHITE),
                new Pawn(this, new Position(1, 5), Color.WHITE),
                new Pawn(this, new Position(1, 6), Color.WHITE),
                new Pawn(this, new Position(1, 7), Color.WHITE)
        };

        this.pieces[6] = new Piece[]{
                new Pawn(this, new Position(6, 0), Color.BLACK),
                new Pawn(this, new Position(6, 1), Color.BLACK),
                new Pawn(this, new Position(6, 2), Color.BLACK),
                new Pawn(this, new Position(6, 3), Color.BLACK),
                new Pawn(this, new Position(6, 4), Color.BLACK),
                new Pawn(this, new Position(6, 5), Color.BLACK),
                new Pawn(this, new Position(6, 6), Color.BLACK),
                new Pawn(this, new Position(6, 7), Color.BLACK)
        };

        this.pieces[7] = new Piece[]{
                new Rook(this, new Position(7, 0), Color.BLACK),
                new Knight(this, new Position(7, 1), Color.BLACK),
                new Bishop(this, new Position(7, 2), Color.BLACK),
                new Queen(this, new Position(7, 3), Color.BLACK),
                new King(this, new Position(7, 4), Color.BLACK),
                new Bishop(this, new Position(7, 5), Color.BLACK),
                new Knight(this, new Position(7, 6), Color.BLACK),
                new Rook(this, new Position(7, 7), Color.BLACK)
        };
    }

    public void setColor(int x, int y, Color color) {
        System.out.println("old color : " + this.pieces[x][y].getColor().name());
        System.out.println("switch to : " + color.name());
        this.pieces[x][y].setColor(color);
        System.out.println("new color : " + this.pieces[x][y].getColor().name());
    }

    public void setPosition(int x, int y, Position pos) {
        this.pieces[x][y].setPosition(pos);
    }
}
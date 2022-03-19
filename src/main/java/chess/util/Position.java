package chess.util;

/**
 * @author roub0002
 * Représente la position d'une pièce lors d'une partie d'échecs
 */
public class Position {
    private int x, y;

    /**
     * Constructeur par défaut
     */
    public Position() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Construit un position sur un échiquier à partir de valeur x et y
     *
     * @param x position sur l'axe des abscices
     * @param y position sur l'axe des ordonées
     */
    public Position(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            throw new IllegalArgumentException("Position Invalide : " + x + ", " + y);
        this.x = x;
        this.y = y;
    }

    /**
     * Construit un position sur un échiquier à partir de notation d'échec
     *
     * @param algebraicNotation Notation constituer d'une lettre et d'un chiffre
     */
    public Position(String algebraicNotation) {
        String ABC = "ABCDEFGH";
        char y = algebraicNotation.charAt(0);
        int x = Character.getNumericValue(algebraicNotation.charAt(1));
        if (ABC.indexOf(y) == -1 || ( x < 1 || x > 8))
            throw new IllegalArgumentException("Position Invalide : " + x + y);
        this.x = x - 1;
        this.y = ABC.indexOf(y);
    }

    /**
     * Retourne la valeur x de la position
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la valeur y de la position
     *
     * @return
     */
    public int getY() {
        return y;
    }

    @Override
    //Ne marche pas correctement avec les notations algebriques
    public String toString() {
        return "( " + x + ", " + y + " );";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public String toAlgebraicNotation() {
        String ABC = "ABCDEFGH";
        char firstLetter = ABC.charAt(this.y);
        String pos = ""+ firstLetter + (this.x + 1);
        return pos;
    }

    /**
     * Retourne true si la position est sur la même ligne, false sinon
     *
     * @param pos
     * @return
     */
    public boolean isOnSameLineAs(Position pos) {
        return pos.getX() == this.x;
    }

    /**
     * Retourne true si la position est sur la même colonne, false sinon
     *
     * @param pos
     * @return
     */
    public boolean isOnSameColumnAs(Position pos) {
        return this.y == pos.getY();
    }

    /**
     * Retourne true si la position est sur la même diagonale, false sinon
     *
     * @param pos
     * @return
     */
    public boolean isOnSameDiagonalAs(Position pos) {
        return Math.abs(this.x - pos.getX()) == Math.abs(this.y - pos.getY());
    }

    /**
     * La distance entre deux positions
     *
     * @param pos
     * @return
     */
    public int getManhattanDistance(Position pos) {
        return Math.abs(this.x - pos.getX()) + Math.abs(this.y - pos.getY());
    }

    /**
     * Sets a new position to a position
     * @param pos
     */
    public void setPosition(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    /**
     * Checks if an algebraic position is valid or not
     * @param pos
     * @return
     */
    public static boolean isValid(String pos) {
        String ABC = "ABCDEFGH";
        if(ABC.indexOf(pos.charAt(0)) == -1 && (int)pos.charAt(1) < 1 && (int)pos.charAt(1) > 8)
            return true;
        else
        {
            return false;
        }
    }

    public static void main(String[] args) {
        Position a = new Position("A1");
        System.out.println(a.isOnSameColumnAs(new Position("A4")));
    }
}
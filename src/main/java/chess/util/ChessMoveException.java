package chess.util;

import java.io.Serial;

/**
 * Lancée lorsqu'un déplacement est invalide.
 */
public class ChessMoveException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur.
     *
     * @param message Le message d'erreur
     * @param startinPosition Position de la case de départ du déplacement
     * @param destination Position de la case de destination du déplacement
     */
    public ChessMoveException(String message, Position startinPosition, Position destination) {
        super(message);
    }
}

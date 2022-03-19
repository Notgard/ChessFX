module chess.chessfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens chess to javafx.fxml;
    exports chess;
}
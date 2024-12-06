module ru.guu.lab20 {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp;


    opens ru.guu.lab20 to javafx.fxml;
    exports ru.guu.lab20;
}
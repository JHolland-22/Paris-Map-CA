module com.example.paris {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.paris to javafx.fxml;
    exports com.example.paris;
}
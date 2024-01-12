module com.ecohub {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ecohub to javafx.fxml;
    exports com.ecohub;
}

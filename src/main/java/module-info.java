module com.ecohub {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires com.jfoenix;
  requires de.jensd.fx.glyphs.commons;
  requires de.jensd.fx.glyphs.fontawesome;
  requires java.sql;

  opens com.ecohub.controller to javafx.fxml;
  opens com.ecohub.models to javafx.base;
  opens com.ecohub.dialog to javafx.fxml;

  exports com.ecohub;
}

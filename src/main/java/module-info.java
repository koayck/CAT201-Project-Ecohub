module com.ecohub {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.jfoenix;
  requires de.jensd.fx.glyphs.commons;
  requires de.jensd.fx.glyphs.fontawesome;
  requires java.sql;

  opens com.ecohub to javafx.fxml;
  exports com.ecohub ;
}
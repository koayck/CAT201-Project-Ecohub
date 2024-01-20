package com.ecohub.models;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.math.BigDecimal;
import java.sql.Date;

import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;

public class Record {

  String title;
  String category;
  String subcategory;
  Date date;
  BigDecimal value;
  BigDecimal footprint;
  int recordId;

  private JFXButton editButton;
  private JFXButton deleteButton;

  public Record(String title, String subcategory, BigDecimal value, Date date, BigDecimal footprint, int recordId) {
    this.subcategory = subcategory;
    this.title = title;
    this.value = value;
    this.date = date;
    this.footprint = footprint;
    this.recordId = recordId;
  }


  public BigDecimal getInput() {
    return value;
  }

  public void setInput(BigDecimal value) {
    this.value = value;
  }

  public String getActivity() {
    return title;
  }

  public void setActivity(String title) {
    this.title = title;
  }

  // getter and setter for category
  public String getCategory() {
    return category;
  }

  // setter for category
  public void setCategory(String category) {
    this.category = category;
  }

  // getter and setter for subcategory
  public String getSubcategory() {
    return subcategory;
  }

  // setter for subcategory
  public void setSubcategory(String subcategory) {
    this.subcategory = subcategory;
  }

  // getter and setter for date
  public Date getDate() {
    return date;
  }

  // setter for date
  public void setDate(Date date) {
    this.date = date;
  }

  // getter and setter for footprint
  public BigDecimal getFootprint() {
    return footprint;
  }

  // setter for footprint
  public void setFootprint(BigDecimal footprint) {
    this.footprint = footprint;
  }

  // getter and setter for recordId
  public int getRecord_id() {
    return recordId;
  }

  // setter for recordId
  public void setRecord_id(int recordId) {
    this.recordId = recordId;
  }

  // getter and setter for editButton
  public JFXButton getEditButton() {
    return editButton;
  }

  // setter for editButton
  public void setEditButton(JFXButton editButton) {
    this.editButton = editButton;
  }

  // getter and setter for deleteButton
  public JFXButton getDeleteButton() {
    return deleteButton;
  }

  // setter for deleteButton
  public void setDeleteButton(JFXButton deleteButton) {
    this.deleteButton = deleteButton;
  }

  // toString method
  @Override
  public String toString() {
    return title;
  }

  // method to decorate the buttons
  private void btnDecoration() {
    deleteButton.setStyle(
      "-fx-background-color: transparent;-fx-background-radius:0;"
    );
    deleteButton.setCursor(Cursor.HAND);
    FontAwesomeIconView TrashIcon = new FontAwesomeIconView(
      FontAwesomeIcon.TRASH
    );
    TrashIcon.setSize("17");
    TrashIcon.setStyle("-fx-fill:white; -fx-cursor: hand;");
    deleteButton.setGraphic(TrashIcon);
    deleteButton.setContentDisplay(ContentDisplay.TOP);

    editButton.setStyle(
      "-fx-background-color: transparent;-fx-background-radius:0;"
    );
    editButton.setCursor(Cursor.HAND);
    FontAwesomeIconView EditIcon = new FontAwesomeIconView(
      FontAwesomeIcon.EDIT
    );
    EditIcon.setSize("17");
    EditIcon.setStyle("-fx-fill:white; -fx-cursor: hand;");
    editButton.setGraphic(EditIcon);
    editButton.setContentDisplay(ContentDisplay.TOP);
  }
}

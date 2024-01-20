package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.User;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DeleteRecordController {

  private RecordController recordController;
  private RecordItemController recordItemController;

  public void getRecordController(RecordController recordController) {
    this.recordController = recordController;
  }

  public void getRecordItemController(RecordItemController recordItemController) {
    this.recordItemController = recordItemController;
  }

  private User user;

  public void initUser(User user) {
    this.user = user;
  }

  private Integer recordIdToDelete;

  @FXML
  private void handleDelete(ActionEvent event) {
    System.out.print("hello");
    try {
      // Delete the record
      RecordDAO recordDao = new RecordDAO();
      recordIdToDelete = Integer.parseInt(recordItemController.recordId.getText());
      System.out.print(recordIdToDelete);
      recordDao.deleteRecord(recordIdToDelete);

      // Get the current stage
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      recordController.loadDataToTable();

      // Close the current stage
      stage.close();
      // showSuccessDialog();
    } catch (SQLException e) {
      // Handle the SQLException here
      e.printStackTrace();
      // You can show an error message or perform any other error handling logic
    }
  }

  @FXML
  private void handleCancel(ActionEvent event) {
    // Get the current stage
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // Close the current stage
    stage.close();
  }
}

package com.pasquasoft.application;

import java.io.IOException;

import com.pasquasoft.application.model.Post;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Window;

public class PostDialog extends Dialog<Post>
{
  private Post selectedPost;
  private Post createdPost;

  @FXML
  private TextField body;

  @FXML
  private TextField title;

  @FXML
  private TextField userId;

  @FXML
  private ButtonType apply;

  public PostDialog(Window owner, Post selectedPost) throws IOException
  {
    this.selectedPost = selectedPost;

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/com/pasquasoft/application/PostDialog.fxml"));
    loader.setController(this);

    DialogPane dialogPane = loader.load();
    dialogPane.lookupButton(apply).addEventFilter(ActionEvent.ACTION, this::onApplyClicked);

    initOwner(owner);
    initModality(Modality.APPLICATION_MODAL);

    setResizable(false);
    setTitle("Create Post");
    setDialogPane(dialogPane);

    setResultConverter(buttonType -> buttonType.getButtonData().isCancelButton() ? null : createdPost);

    setOnShowing(dialogEvent -> Platform.runLater(() -> title.requestFocus()));
  }

  @FXML
  public void initialize()
  {
    userId.setText(selectedPost.getUserId().toString());
  }

  public void onApplyClicked(ActionEvent event)
  {
    createdPost = new Post();

    createdPost.setUserId(Integer.valueOf(userId.getText()));
    createdPost.setTitle(title.getText());
    createdPost.setBody(body.getText());
  }

}

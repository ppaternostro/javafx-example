package com.pasquasoft.application;

import java.io.IOException;
import java.util.function.UnaryOperator;

import com.pasquasoft.application.exception.NotFoundException;
import com.pasquasoft.application.exception.RestApiException;
import com.pasquasoft.application.model.Post;
import com.pasquasoft.application.rest.PostClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

public class SampleController
{
  private PostClient client = new PostClient();

  @FXML
  private ChoiceBox<String> searchBy;
  @FXML
  private MenuItem about;
  @FXML
  private MenuItem close;
  @FXML
  private Button search;
  @FXML
  private Button delete;
  @FXML
  private Button create;
  @FXML
  private TextField searchText;
  @FXML
  private TableView<Post> table;
  @FXML
  private TableColumn<Post, Integer> colId;
  @FXML
  private TableColumn<Post, Integer> colUserId;
  @FXML
  private TableColumn<Post, String> colTitle;
  @FXML
  private TableColumn<Post, String> colBody;

  @FXML
  public void initialize()
  {
    // Enable the delete and create buttons on row selection
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      delete.setDisable(table.getItems().isEmpty());
      create.setDisable(table.getItems().isEmpty());
    });

    // Bind object attributes to table columns
    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
    colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    colBody.setCellValueFactory(new PropertyValueFactory<>("body"));

    // Set column to text field table cell factory
    colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
    colBody.setCellFactory(TextFieldTableCell.forTableColumn());

    // Add choice box items
    searchBy.getItems().addAll("All", "Id");

    // Set selected choice box item
    searchBy.setValue("All");

    UnaryOperator<TextFormatter.Change> integerFilter = change -> change.getControlNewText().matches("[0-9]*") ? change
        : null;

    // Numeric only text field
    searchText.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));
  }

  public void onAboutSelected()
  {
    showAlert("About", AlertType.INFORMATION, about.getParentPopup().getOwnerWindow(), "JavaFX Example Application"
        + System.lineSeparator() + "Pat Paternostro" + System.lineSeparator() + "Copyright © 2025");
  }

  public void onCloseSelected()
  {
    Stage stage = (Stage) close.getParentPopup().getOwnerWindow();
    stage.close();
  }

  public void onSearchClicked()
  {
    try
    {
      ObservableList<Post> posts = searchBy.getValue().equals("Id")
          ? FXCollections.observableArrayList(client.getPost(Integer.valueOf(searchText.getText())))
          : FXCollections.observableArrayList(client.getAllPosts());

      table.setItems(posts);
      table.refresh();
      table.getSelectionModel().select(null);
      delete.setDisable(true);
      create.setDisable(true);
    }
    catch (NotFoundException e)
    {
      table.getItems().clear();
    }
    catch (IOException | InterruptedException | RestApiException e)
    {
      showAlert("Error", AlertType.ERROR, search.getScene().getWindow(), e.getMessage());
    }
  }

  public void onDeleteClicked()
  {
    Post post = table.getSelectionModel().getSelectedItem();
    try
    {
      client.deletePost(post.getId());

      table.getItems().remove(post);
    }
    catch (IOException | InterruptedException | RestApiException e)
    {
      showAlert("Error", AlertType.ERROR, table.getScene().getWindow(), e.getMessage());
    }
  }

  public void onCreateClicked() throws IOException
  {
    int index = table.getSelectionModel().getSelectedIndex();
    Post post = table.getSelectionModel().getSelectedItem();
    PostDialog dialog = new PostDialog(create.getScene().getWindow(), post);

    dialog.showAndWait().ifPresent(newPost -> {
      try
      {
        Post createdPost = client.createPost(newPost);

        /*
         * The POST endpoint does not return a fully populated object, just the
         * newly created post id. Therefore, we need to set the missing created
         * post object values from the new post object. It is what it is with a
         * free, fake API. ¯\_(ツ)_/¯
         */
        createdPost.setBody(newPost.getBody());
        createdPost.setTitle(newPost.getTitle());
        createdPost.setUserId(newPost.getUserId());

        table.getItems().add(index, createdPost);
      }
      catch (IOException | InterruptedException | RestApiException e)
      {
        showAlert("Error", AlertType.ERROR, create.getScene().getWindow(), e.getMessage());
      }
    });
  }

  public void onSearchTextChanged()
  {
    String text = searchText.getText();

    search.setDisable(text.length() == 0);
  }

  public void onSearchBySelectionChanged()
  {
    boolean idSelected = searchBy.getValue().equals("Id");

    search.setDisable(idSelected);
    searchText.setDisable(!idSelected);

    if (!idSelected)
    {
      searchText.clear();
    }
  }

  public void onTitleColumnEditCommit(CellEditEvent<Post, String> event)
  {
    Post post = event.getRowValue();

    try
    {
      // Implicit deep copy as all class attributes are immutable
      Post clonedPost = (Post) post.clone();

      clonedPost.setTitle(event.getNewValue());
      client.updatePost(clonedPost);

      // Bind the updated value to the table row
      post.setTitle(event.getNewValue());
    }
    catch (IOException | InterruptedException | RestApiException e)
    {
      showAlert("Error", AlertType.ERROR, table.getScene().getWindow(), e.getMessage());
    }
    catch (CloneNotSupportedException e)
    {
      /*
       * Won't happen as Post class is cloneable and the clone method is
       * implemented.
       */
    }
  }

  public void onBodyColumnEditCommit(CellEditEvent<Post, String> event)
  {
    Post post = event.getRowValue();

    try
    {
      // Implicit deep copy as all class attributes are immutable
      Post clonedPost = (Post) post.clone();

      clonedPost.setBody(event.getNewValue());
      client.updatePost(clonedPost);

      post.setBody(event.getNewValue());
    }
    catch (IOException | InterruptedException | RestApiException e)
    {
      showAlert("Error", AlertType.ERROR, table.getScene().getWindow(), e.getMessage());
    }
    catch (CloneNotSupportedException e)
    {
      /*
       * Won't happen as Post class is cloneable and the clone method is
       * implemented.
       */
    }
  }

  private void showAlert(String title, AlertType alertType, Window owner, String message)
  {
    Alert alert = new Alert(alertType);
    alert.setHeaderText(message);
    alert.setTitle(title);
    alert.initOwner(owner);
    alert.showAndWait();
  }

}

package com.pasquasoft.application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

public class Main extends Application
{
  @Override
  public void start(Stage primaryStage)
  {
    try
    {
      VBox root = (VBox) FXMLLoader.load(getClass().getResource("/com/pasquasoft/application/Sample.fxml"));
      Scene scene = new Scene(root);
      scene.getStylesheets()
          .add(getClass().getResource("/com/pasquasoft/application/application.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("JavaFX Example Application");
      primaryStage.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}

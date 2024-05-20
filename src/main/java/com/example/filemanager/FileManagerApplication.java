package com.example.filemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.stage.Stage;

import java.io.IOException;

public class FileManagerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FileManagerApplication.class.getResource("FileManager.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        stage.setTitle("File Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
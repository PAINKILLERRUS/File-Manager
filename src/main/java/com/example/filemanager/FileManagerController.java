package com.example.filemanager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManagerController {
    @FXML
    public VBox leftPanel;
    @FXML
    public VBox rightPanel;

    public void buttonActionExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void buttonActionCopyFile(ActionEvent actionEvent) {
        PanelController leftPanelController = (PanelController) leftPanel.getProperties().get("controller");
        PanelController rightPanelController = (PanelController) rightPanel.getProperties().get("controller");

        if (leftPanelController.getSelectFileName() == null && rightPanelController.getSelectFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No files were selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPanelController = null, dstPanelController = null;
        if (leftPanelController.getSelectFileName() != null) {
            srcPanelController = leftPanelController;
            dstPanelController = rightPanelController;
        }
        if (rightPanelController.getSelectFileName() != null) {
            srcPanelController = rightPanelController;
            dstPanelController = leftPanelController;
        }

        assert srcPanelController != null;
        Path srcPath = Paths.get(srcPanelController.getCurrentPath(), srcPanelController.getSelectFileName());
        Path dstPath = Paths.get(dstPanelController.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, dstPath);
            dstPanelController.updateList(Paths.get(dstPanelController.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The file could not be copied", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void buttonActionMovingFile(ActionEvent actionEvent) {
        PanelController leftPanelController = (PanelController) leftPanel.getProperties().get("controller");
        PanelController rightPanelController = (PanelController) rightPanel.getProperties().get("controller");

        if (leftPanelController.getSelectFileName() == null && rightPanelController.getSelectFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No files were selected", ButtonType.OK);
            return;
        }

        PanelController scrPanelController = null, dstPanelController = null;
        if (leftPanelController.getSelectFileName() != null) {
            scrPanelController = leftPanelController;
            dstPanelController = rightPanelController;
        }

        if (rightPanelController.getSelectFileName() != null) {
            dstPanelController = leftPanelController;
            scrPanelController = rightPanelController;
        }
        assert scrPanelController != null;
        Path srcPath = Paths.get(scrPanelController.getCurrentPath(), scrPanelController.getSelectFileName());
        Path dstPath = Paths.get(dstPanelController.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.move(srcPath, dstPath);
            dstPanelController.updateList(Paths.get(dstPanelController.getCurrentPath()));
            scrPanelController.updateList(Paths.get(scrPanelController.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The file cannot be transferred", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void buttonActionDeleteFile(ActionEvent actionEvent) {
        PanelController leftPanelController = (PanelController) leftPanel.getProperties().get("controller");
        PanelController rightPanelController = (PanelController) rightPanel.getProperties().get("controller");

        if (leftPanelController.getSelectFileName() == null && rightPanelController.getSelectFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No files were selected", ButtonType.OK);
            return;
        }

        PanelController scrPanelController = null, dstPanelController = null;
        if (leftPanelController.getSelectFileName() != null) {
            scrPanelController = leftPanelController;
            dstPanelController = rightPanelController;
        }

        if (rightPanelController.getSelectFileName() != null) {
            dstPanelController = leftPanelController;
            scrPanelController = rightPanelController;
        }
        assert scrPanelController != null;
        Path srcPath = Paths.get(scrPanelController.getCurrentPath(), scrPanelController.getSelectFileName());
        Path dstPath = Paths.get(dstPanelController.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.delete(dstPath);
            dstPanelController.updateList(Paths.get(dstPanelController.getCurrentPath()));
            scrPanelController.updateList(Paths.get(scrPanelController.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The file cannot be deleted", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void createDirectory(ActionEvent actionEvent) {
        PanelController leftPanelController = (PanelController) leftPanel.getProperties().get("controller");
        PanelController rightPanelController = (PanelController) rightPanel.getProperties().get("controller");

        if (leftPanelController.getSelectFileName() == null && rightPanelController.getSelectFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No files were selected", ButtonType.OK);
            return;
        }

        PanelController scrPanelController = null, dstPanelController = null;
        if (leftPanelController.getSelectFileName() != null) {
            scrPanelController = leftPanelController;
            dstPanelController = rightPanelController;
        }

        if (rightPanelController.getSelectFileName() != null) {
            dstPanelController = leftPanelController;
            scrPanelController = rightPanelController;
        }
        assert scrPanelController != null;
        Path srcPath = Paths.get(scrPanelController.getCurrentPath(), scrPanelController.getSelectFileName());
        Path dstPath = Paths.get(dstPanelController.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Path directoryPath = Paths.get(dstPath.toString());
            Files.createDirectory(directoryPath);
            dstPanelController.updateList(Paths.get(dstPanelController.getCurrentPath()));
            scrPanelController.updateList(Paths.get(scrPanelController.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Directory could not be created", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
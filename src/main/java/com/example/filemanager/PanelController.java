package com.example.filemanager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PanelController implements Initializable {

    @FXML
    public ComboBox<String> diskBox;
    @FXML
    public TextField pathField;
    @FXML
    TableView<FileInfo> fileTable;

    Path prevField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        //создание одного столбца
        fileTypeColumn.setCellValueFactory(parameter -> new SimpleStringProperty(parameter.getValue().getType().getName()));
        //установка базовой длины
        fileTypeColumn.setPrefWidth(25);
        //добавление столбца в таблицу

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(parameter -> new SimpleStringProperty(parameter.getValue().getFileName()));
        fileNameColumn.setPrefWidth(250);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(parameter -> new SimpleObjectProperty<>(parameter.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(130);


        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Date of change");
        fileDateColumn.setCellValueFactory(parameter -> new SimpleStringProperty(parameter.getValue().getLastMod().format(timeFormatter)));
        fileDateColumn.setPrefWidth(130);

        fileTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        fileTable.getSortOrder().add(fileTypeColumn);

        diskBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            diskBox.getItems().add(p.toString());
        }
        diskBox.getSelectionModel().select(0);

        fileTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(fileTable.getSelectionModel().getSelectedItem().getFileName());
                    prevField = Paths.get(getCurrentPath());
                    if (Files.isDirectory(path)) {
                        updateList(path);
                    }
                }
            }
        });
        updateList(Paths.get("."));
    }

    /**
     * Метод обновления(наполнения таблицы) списком файлов
     *
     * @param path - путь к папке
     */
    public void updateList(Path path) {
        try {
            //указываем путь к выбранной папке-директории
            pathField.setText(path.normalize().toAbsolutePath().toString());
            //предварительно очищаем таблицу
            fileTable.getItems().clear();
            fileTable.sort();
            //получаем список файлов в таблице, добавляем файлы.
            // С помощью метода map() передаем путь File.list в конструктор FileInfo::new, собираем все в List
            fileTable.getItems().addAll(Files.list(path).map(FileInfo::new).toList());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to update file list", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void buttonPathUp(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        prevField = Paths.get(getCurrentPath());
        if (upperPath != null) {
            updateList(upperPath);
        }
    }

    public void buttonPathDown(ActionEvent actionEvent) {
        updateList(prevField);
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    /**
     * Метод выбора файла
     */
    public String getSelectFileName() {
        if (!fileTable.isFocused()) {
            return null;
        }
        return fileTable.getSelectionModel().getSelectedItem().getFileName();
    }

    /**
     * Метод запроса текущего пути
     */
    public String getCurrentPath() {
        return pathField.getText();
    }
}

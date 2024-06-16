package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class CompressorDecompressorApp extends Application {

    private ListView<File> fileListView;
    private File currentDirectory;
    private TextField pathTextField;
    private Stack<File> directoryHistory;

    @Override
    public void start(Stage primaryStage) {

        Parameters params = getParameters();
        List<String> args = params.getRaw();

        if (args.size() >= 2 && args.get(1).equalsIgnoreCase("extract")) {
            if (args.size() >= 1) {
                String filePath = args.get(0);
                File file = new File(filePath);
                if (file.exists() && file.getName().endsWith(".copi")) {
                    try {
                        Decompressor.decompress(file.getAbsolutePath(), file.getParent());
                        System.out.println("Archivo descomprimido con éxito en: " + file.getParent());
                        
                    } catch (IOException e) {
                        System.err.println("Error al descomprimir el archivo: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("El archivo proporcionado no es un archivo .copi válido: " + filePath);
                }
            } else {
                System.err.println("No se proporcionó un archivo para extraer.");
            }
            System.exit(0);
        }
    	    
        primaryStage.setTitle("Compi"); 
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icono_oscuro.png"))); 
        directoryHistory = new Stack<>();
        BorderPane root = new BorderPane();
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("hbox-toolbar");

        Button compressButton = new Button("Comprimir");

        Button decompressButton = new Button("Descomprimir");
        Button decompressToButton = new Button("Descomprimir en...");
        Button deleteButton = new Button("Eliminar");

        Label statusLabel = new Label();

        fileListView = new ListView<>();
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        pathTextField = new TextField();
        pathTextField.setEditable(false);
        pathTextField.getStyleClass().add("path-text-field");

        Button backButton = new Button("<");
        backButton.getStyleClass().add("back-button");
        backButton.setFont(new Font(12)); 
        backButton.setPadding(new Insets(5, 10, 5, 10)); 
        backButton.setOnAction(event -> {
            if (!directoryHistory.isEmpty()) {
                File previousDirectory = directoryHistory.pop(); 
                updateFileListView(previousDirectory);
            } else {
                File parentDirectory = currentDirectory.getParentFile();
                if (parentDirectory != null) {
                    updateFileListView(parentDirectory);
                }
            }
        });

        HBox backButtonContainer = new HBox(backButton);
        backButtonContainer.getStyleClass().add("hbox-back-button");

        HBox pathTextFieldContainer = new HBox(pathTextField);
        pathTextFieldContainer.getStyleClass().add("path-text-field-container");

        FileChooser fileChooser = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String desktop = System.getProperty("user.home") + "\\Desktop";
        currentDirectory = new File(desktop);

        updateFileListView(currentDirectory);

        fileListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = fileListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    File selectedFile = fileListView.getItems().get(selectedIndex);
                    if (selectedFile.isDirectory()) {
                        updateFileListView(selectedFile);
                    } else if (selectedFile.getName().endsWith(".copi")) {
                        try {
                            directoryHistory.push(currentDirectory); 
                            currentDirectory = selectedFile;
                            List<File> contents = Decompressor.listContents(selectedFile.getAbsolutePath());
                            if (contents != null) {
                                fileListView.getItems().setAll(contents);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        compressButton.setOnAction(event -> {
            ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
            if (!selectedFiles.isEmpty()) {
                if (selectedFiles.size() > 1) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nombre de archivo comprimido");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Introduce un nombre para el archivo comprimido:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> {
                        File destFile = new File(currentDirectory, name + ".copi");
                        try {
                            Compressor.compressMany(selectedFiles, destFile.getAbsolutePath());
                            statusLabel.setText("Archivos comprimidos con éxito.");
                            updateFileListView(currentDirectory);
                        } catch (IOException e) {
                            statusLabel.setText("Error al comprimir los archivos.");
                            e.printStackTrace();
                        }
                    });
                } else {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Nombre de archivo comprimido");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Introduce un nombre para el archivo comprimido:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> {
                        File sourceFile = selectedFiles.get(0);
                        File destFile = new File(currentDirectory, name + ".copi");
                        try {
                            Compressor.compress(sourceFile.getAbsolutePath(), destFile.getAbsolutePath());
                            statusLabel.setText("Archivo comprimido con éxito.");
                            updateFileListView(currentDirectory);
                        } catch (IOException e) {
                            statusLabel.setText("Error al comprimir el archivo: " + sourceFile.getName());
                            e.printStackTrace();
                        }
                    });
                }
            }
        });


        decompressButton.setOnAction(event -> {
            ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
            if (!selectedFiles.isEmpty()) {
                List<String> compressedFiles = new ArrayList<>();
                for (File file : selectedFiles) {
                    compressedFiles.add(file.getAbsolutePath());
                }
                try {
                    Decompressor.decompress(compressedFiles, currentDirectory.getAbsolutePath());
                    statusLabel.setText("Archivo(s) descomprimido(s) con éxito.");
                    updateFileListView(currentDirectory);
                } catch (IOException e) {
                    statusLabel.setText("Error al descomprimir el archivo.");
                    e.printStackTrace();
                }
            }
        });

        decompressToButton.setOnAction(event -> {
            ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
            if (!selectedFiles.isEmpty()) {
                File destDirectory = directoryChooser.showDialog(primaryStage);
                if (destDirectory != null) {
                    List<String> compressedFiles = new ArrayList<>();
                    for (File file : selectedFiles) {
                        compressedFiles.add(file.getAbsolutePath());
                    }
                    try {
                        Decompressor.decompress(compressedFiles, destDirectory.getAbsolutePath());
                        statusLabel.setText("Archivo(s) descomprimido(s) con éxito.");
                        updateFileListView(currentDirectory);
                    } catch (IOException e) {
                        statusLabel.setText("Error al descomprimir el archivo.");
                        e.printStackTrace();
                    }
                }
            }
        });


        deleteButton.setOnAction(event -> {
            ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
            if (!selectedFiles.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar eliminación");
                alert.setHeaderText("¿Estás seguro que quieres eliminar el(los) archivo(s) o carpeta(s) seleccionado(s)?");
                alert.setContentText("Esta acción no se puede deshacer.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    for (File file : selectedFiles) {
                        if (file.isDirectory()) {
                            deleteDirectory(file);
                        } else {
                            file.delete();
                        }
                    }
                    updateFileListView(currentDirectory);
                }
            }
        });

        toolbar.getChildren().addAll(compressButton, decompressButton, decompressToButton, deleteButton);
        HBox pathBox = new HBox(10, backButtonContainer, pathTextFieldContainer);
        pathBox.setAlignment(Pos.CENTER_LEFT);
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(toolbar, pathBox, fileListView);
        VBox.setVgrow(fileListView, Priority.ALWAYS);
        root.setTop(vbox);
        root.setCenter(statusLabel);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        pathTextField.setPrefWidth(scene.getWidth() - 50);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            pathTextField.setPrefWidth(newValue.doubleValue());
        });

        fileListView.setPrefWidth(scene.getWidth());
        fileListView.setPrefHeight(scene.getHeight() - toolbar.getHeight() - pathBox.getHeight() - 20);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            fileListView.setPrefWidth(newValue.doubleValue());
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            fileListView.setPrefHeight(newValue.doubleValue() - toolbar.getHeight() - pathBox.getHeight() - 40);
        });
        startFileWatcher();
        
        if (!args.isEmpty()) {
            String filePath = args.get(0);

            File file = new File(filePath);

            if (file.exists()) {
            	
                updateFileListView(file.getParentFile());

                if (file.getName().endsWith(".copi")) {
                    fileListView.getSelectionModel().select(file);
                }
            } else {
                System.err.println("La ruta proporcionada no es un archivo válido: " + filePath);
            }
        } else {
            updateFileListView(currentDirectory);
        }

    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }


	private File suggestCompressedFile(File sourceFile) {
        String sourcePath = sourceFile.getAbsolutePath();
        String fileName = sourceFile.getName();
        int lastIndex = sourcePath.lastIndexOf('.');
        String nameWithoutExtension = (lastIndex == -1) ? sourcePath : sourcePath.substring(0, lastIndex);

        String destPath = nameWithoutExtension + ".copi";
        return new File(destPath);
    }


    private File suggestDecompressedFile(File compressedFile) {
        String compressedPath = compressedFile.getAbsolutePath();
        if (compressedPath.endsWith(".copi")) {
            String destPath = compressedPath.substring(0, compressedPath.length() - 5);
            return new File(destPath);
        }
        return new File(compressedPath + ".decompressed");
    }

    private void updateFileListView(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            currentDirectory = fileOrDirectory;
        } else {
            currentDirectory = fileOrDirectory.getParentFile();
        }
        pathTextField.setText(currentDirectory.getAbsolutePath());
        fileListView.getItems().clear();
        File[] files = currentDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileListView.getItems().add(file);
            }
        }
        fileListView.setCellFactory(param -> new FileListCell());
    }

    private class FileListCell extends javafx.scene.control.ListCell<File> {
        @Override
        protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null || item.getName() == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getName());
                ImageView imageView = new ImageView();
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                if (item.isDirectory()) {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/resources/folder_icon.png")));
                } else {
                    String extension = getFileExtension(item);
                    if (extension != null && extension.equals("copi")) {
                        imageView.setImage(new Image(getClass().getResourceAsStream("/resources/icono_archivo.png")));
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream("/resources/file_icon.png")));
                    }
                }
                setGraphic(imageView);
            }
        }

        private String getFileExtension(File file) {
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf == -1 || lastIndexOf == 0) {
                return null;
            }
            return name.substring(lastIndexOf + 1);
        }
    }
    
    private void startFileWatcher() {
        Thread fileWatcherThread = new Thread(() -> {
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                currentDirectory.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException ex) {
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        Platform.runLater(() -> updateFileListView(currentDirectory));
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException | ClosedWatchServiceException ex) {
                ex.printStackTrace();
            }
        });
        fileWatcherThread.setDaemon(true);
        fileWatcherThread.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

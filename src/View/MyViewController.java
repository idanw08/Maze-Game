package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class MyViewController implements Observer, IView {

    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField textFieldRowsNum;
    public javafx.scene.control.TextField txtfieldColumnsNum;
    public Button buttonSolve;
    public Button buttonBuild;
    public MenuItem saveItem;
    public Label statusLabelBar;
    int currentHeight = 0;
    int currentWidth = 0;
    public MediaPlayer mediaPlayer;

    public void setMyViewModel(MyViewModel mvm) {
        myViewModel = mvm;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable == myViewModel) {
            Stage s = (Stage) txtfieldColumnsNum.getScene().getWindow();
            buttonBuild.setDisable(false);
            statusLabelBar.setVisible(false);
            displayMaze(myViewModel.getMaze(), s.getWidth() - 300 + currentWidth, s.getHeight() - 150 + currentHeight);
            if (!myViewModel.isSolved())
                buttonSolve.setDisable(false);

            saveItem.setDisable(false);
            if (myViewModel.isDone())
                try {
                    winWindow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void displayMaze(int[][] maze, double width, double height) {
        mazeDisplayer.setMaze(maze, width, height);
        int charPosRow = myViewModel.getCharacterPositionRow();
        int charPosCol = myViewModel.getCharacterPositionColumn();
        mazeDisplayer.setGoalPos(myViewModel.getGoalPositionRow(), myViewModel.getGoalPositionColumn());
        mazeDisplayer.setCharPos(charPosRow, charPosCol, width, height);
        CharacterRow.set(charPosRow + "");
        CharacterColumn.set(charPosCol + "");
    }

    public void generate() {
        currentHeight = 0;
        currentWidth = 0;
        Media sound = new Media(getClass().getResource("/sounds/MUSICֹֹ-HBS.mp3").toString());
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer(sound);
        }

        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        int row = 0, column = 0;
        try {
            column = Integer.valueOf(txtfieldColumnsNum.getText());
            row = Integer.valueOf(textFieldRowsNum.getText());
            if (column <= 0 || row <= 0) {
                showAlertError("Rows and Columns must be positive numbers!");
                return;
            }

            buttonBuild.setDisable(true);
            statusLabelBar.setVisible(true);
            statusLabelBar.setText("Loading new maze...");
            myViewModel.generate(row, column);
        } catch (NumberFormatException e) {
            showAlertError("Rows and Columns must contain only numbers!");
        } catch (Exception e) {
        }
        mazeDisplayer.requestFocus();
    }

    public void mouseMove(MouseEvent move) {
        if (mazeDisplayer.getMaze() == null)
            return;

        int mouseX = (int) ((move.getX() - 250) / (mazeDisplayer.getCellHeight()));
        int mouseY = (int) ((move.getY() - 33) / (mazeDisplayer.getCellWidth()));
        if (Math.abs(myViewModel.getCharacterPositionRow() - mouseY) < 2 || (Math.abs(myViewModel.getCharacterPositionColumn() - mouseX) < 2)) {
            if (mouseY < myViewModel.getCharacterPositionRow()) {
                myViewModel.moveCharacter(KeyCode.UP);
            }
            if (mouseY > myViewModel.getCharacterPositionRow()) {
                myViewModel.moveCharacter(KeyCode.DOWN);
            }
            if (mouseX < myViewModel.getCharacterPositionColumn()) {
                myViewModel.moveCharacter(KeyCode.LEFT);
            }
            if (mouseX > myViewModel.getCharacterPositionColumn()) {
                myViewModel.moveCharacter(KeyCode.RIGHT);
            }
        }
        mazeDisplayer.requestFocus();
    }

    public void solve() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Solve");
        alert.setHeaderText("The solution path will show on the maze");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            buttonBuild.setDisable(true);
            buttonSolve.setDisable(true);
            statusLabelBar.setVisible(true);
            statusLabelBar.setText("Loading solution...");
            myViewModel.solve();
            mazeDisplayer.requestFocus();
        } else {
            return;
        }
    }

    private void showAlertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void showAlertInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    public void keyPressed(KeyEvent e) {
        myViewModel.moveCharacter(e.getCode());
        e.consume();
    }

    public void focus() {
        mazeDisplayer.requestFocus();
    }

    public StringProperty CharacterRow = new SimpleStringProperty();

    public StringProperty CharacterColumn = new SimpleStringProperty();

    public String getCharacterRow() {
        return CharacterRow.get();
    }

    public StringProperty characterRowProperty() {
        return CharacterRow;
    }

    public String getCharacterColumn() {
        return CharacterColumn.get();
    }

    public StringProperty characterColumnProperty() {
        return CharacterColumn;
    }

    public void setResizeEvent(Scene scene) {
        long height = 0;
        long width = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.setWidth(scene.getWidth());
                    mazeDisplayer.setHeight(scene.getHeight());
                    update(myViewModel, new Object());
                }
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                if (mazeDisplayer.getMaze() != null) {
                    mazeDisplayer.setWidth(scene.getWidth());
                    mazeDisplayer.setHeight(scene.getHeight());
                    update(myViewModel, new Object());
                }
            }
        });
    }

    public void winWindow() throws IOException {
        mediaPlayer.stop();
        Stage s = new Stage();
        s.setTitle("You Won!");
        Parent root = FXMLLoader.load(getClass().getResource("Win.fxml"));
        Scene scene = new Scene(root, 500, 285);
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
        s.setOnCloseRequest(event -> reset());
    }

    public void propertyWindow() throws IOException {
        Stage s = new Stage();
        s.setTitle("Maze Properties");
        Parent root = FXMLLoader.load(getClass().getResource("Prop.fxml"));
        Scene scene = new Scene(root, 550, 350);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }

    public void aboutWindow() throws IOException {
        Stage s = new Stage();
        s.setTitle("About");
        AnchorPane root = FXMLLoader.load(getClass().getResource("About.fxml"));
        Scene scene = new Scene(root, 750, 400);
        scene.getStylesheets().add(getClass().getResource("Background.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }

    public void helpWindow() throws IOException {
        Stage s = new Stage();
        s.setTitle("Help");
        AnchorPane root = FXMLLoader.load(getClass().getResource("Help.fxml"));
        Scene scene = new Scene(root, 830, 650);
        scene.getStylesheets().add(getClass().getResource("Background.css").toExternalForm());
        s.setScene(scene);
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.primeryS);
        s.show();
    }

    public void exit() {
        Main.fullExit();
    }

    public void reset() {
        txtfieldColumnsNum.clear();
        textFieldRowsNum.clear();
        buttonSolve.setDisable(true);
        saveItem.setDisable(true);
        mazeDisplayer.cleanBoard();
        showAlertInfo("Please select new parameters and build your next maze!");
        myViewModel.reset();
    }

    public void saveMaze() throws FileNotFoundException {
        if (mazeDisplayer.getMaze() == null)
            return;

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze Files (*.Maze)", "*.Maze");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog((Stage) txtfieldColumnsNum.getScene().getWindow());
        if (file != null) {
            myViewModel.save(file);
            showAlertInfo("Saved!");
        }
    }

    public void loadMaze() throws FileNotFoundException {
        currentWidth = 0;
        currentHeight = 0;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Maze File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Maze Files (*.Maze)", "*.Maze");
        chooser.getExtensionFilters().add(fileExtensions);
        File f = chooser.showOpenDialog((Stage) txtfieldColumnsNum.getScene().getWindow());
        if (f != null) {
            Media sound = new Media(getClass().getResource("/sounds/MUSICֹֹ-HBS.mp3").toString());
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer(sound);
            }

            mediaPlayer.play();
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            myViewModel.Load(f);
            saveItem.setDisable(false);
            if (myViewModel.isSolved())
                buttonSolve.setDisable(true);

            mazeDisplayer.requestFocus();
        }
    }

    public void zoom(ScrollEvent move) {
        Stage s = (Stage) buttonBuild.getScene().getWindow();
        if (move.isControlDown()) {
            if (move.getDeltaY() < 0) {
                currentWidth -= 4;
                currentHeight -= 4;
                displayMaze(myViewModel.getMaze(), s.getWidth() - 300 + currentWidth, s.getHeight() - 150 + currentHeight);
            } else {
                currentWidth += 4;
                currentHeight += 4;
                displayMaze(myViewModel.getMaze(), s.getWidth() - 300 + currentWidth, s.getHeight() - 150 + currentHeight);
            }
        }
    }

}

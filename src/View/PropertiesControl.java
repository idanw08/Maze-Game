package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesControl {

    @FXML
    public TextField textThreadNum;
    public Button buttonApply;
    public ComboBox comboBoxAlgo;
    public ComboBox comboBoxMazeGen;

    ObservableList<String> algo = FXCollections.observableArrayList("BreadthFirstSearch", "BestFirstSearch", "DepthFirstSearch");
    ObservableList<String> mazeGen = FXCollections.observableArrayList("MyMazeGenerator", "SimpleMazeGenerator");

    public Properties prop;

    @FXML
    public void initialize() {
        comboBoxAlgo.setItems(algo);
        comboBoxMazeGen.setItems(mazeGen);
        try {
            FileInputStream fileInputStream = new FileInputStream("./config.properties");
            prop = new Properties();
            prop.load(fileInputStream);
            fileInputStream.close();
            comboBoxAlgo.setValue(prop.getProperty("SearchAlgorithm"));
            comboBoxMazeGen.setValue(prop.getProperty("GenerateAlgorithm"));
            textThreadNum.setText(prop.getProperty("MaxNumOfThreads"));
        } catch (IOException e) {
        }
    }

    private void showAlert(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(message);
        a.show();
    }

    public void apply() {
        int newNumofThreads = 0;
        try {
            newNumofThreads = Integer.valueOf(textThreadNum.getText());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("./config.properties");
                prop.setProperty("SearchAlgorithm", comboBoxAlgo.getSelectionModel().getSelectedItem().toString());
                prop.setProperty("GenerateAlgorithm", comboBoxMazeGen.getSelectionModel().getSelectedItem().toString());
                prop.setProperty("MaxNumOfThreads", "" + newNumofThreads);
                prop.store(fileOutputStream, null);
                fileOutputStream.close();
                showAlert("Properties Saved!");
                Stage s = (Stage) buttonApply.getScene().getWindow();
                s.close();
            } catch (IOException e) {
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid number of threads");
        }
    }
}
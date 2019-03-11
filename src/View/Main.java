package View;

import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primeryS;
    public static Server MazeGen;
    public static Server Solver;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primeryS = primaryStage;
        MazeGen = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        MazeGen.start();
        Solver = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        Solver.start();
        primaryStage.setTitle("The Big Maze");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        MyModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setMyViewModel(viewModel);
        viewModel.addObserver(view);
        primaryStage.setOnCloseRequest(event -> fullExit());
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void fullExit() {
        MazeGen.stop();
        Solver.stop();
        System.exit(0);
    }
}
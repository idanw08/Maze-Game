package ViewModel;

import Model.IModel;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    @FXML
    private IModel iModel;

    public MyViewModel(IModel model) {
        this.iModel = model;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable == iModel) {
            setChanged();
            notifyObservers();
        }
    }

    public void generate(int row, int col) {
        iModel.generate(row, col);
    }

    public void moveCharacter(KeyCode move) {
        iModel.moveCharacter(move);
    }

    public boolean isDone() {
        return iModel.isDone();
    }

    public int[][] getMaze() {
        return iModel.getMaze();
    }

    public int getCharacterPositionRow() {
        return iModel.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() {
        return iModel.getCharacterPositionColumn();
    }

    public int getGoalPositionRow() {
        return iModel.getGoalPositionRow();
    }

    public int getGoalPositionColumn() {
        return iModel.getGoalPositionColumn();
    }

    public boolean isSolved() {
        return iModel.isSolved();
    }

    public void solve() {
        iModel.solve();
    }

    public void save(File f) {
        iModel.saveMaze(f);
    }

    public void Load(File f) {
        try {
            iModel.loadMaze(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        iModel.reset();
    }

}

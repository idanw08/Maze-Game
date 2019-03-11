package Model;

import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {

    void generate(int rows, int cols);

    void solve();

    int getCharacterPositionRow();

    int getCharacterPositionColumn();

    int getGoalPositionRow();

    int getGoalPositionColumn();

    int[][] getMaze();

    void moveCharacter(KeyCode movement);

    boolean isDone();

    void saveMaze(File f);

    void loadMaze(File f) throws Exception;

    boolean isSolved();

    void reset();
}

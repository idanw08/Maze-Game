package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int charRow;
    private int charCol;
    private int goalCol;
    private int goalRow;
    double cellWidth;
    double cellHeight;

    public double getCellHeight() {
        return cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setMaze(int[][] maze, double width, double height) {
        this.maze = maze;
        draw(width, height);
    }

    public int[][] getMaze() {
        return maze;
    }

    public void setCharPos(int row, int col, double w, double h) {
        charRow = row;
        charCol = col;
        draw(w, h);
    }

    public void setGoalPos(int row, int col) {
        goalRow = row;
        goalCol = col;
    }

    public void cleanBoard() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

    public void draw(double width, double height) {
        if (maze != null) {
            double cellHeight = this.cellHeight = width / maze[0].length;
            double cellWidth = this.cellWidth = height / maze.length;

            try {
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image GoalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image solve = new Image(new FileInputStream(ImageFileNameSolve.get()));
                GraphicsContext graphicsContext = getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, getWidth(), getHeight());
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1)
                            graphicsContext.drawImage(wallImage, j * cellHeight, i * cellWidth, cellHeight, cellWidth);

                        if (maze[i][j] == 2)
                            graphicsContext.drawImage(solve, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                    }
                }

                graphicsContext.drawImage(GoalImage, goalCol * cellHeight, goalRow * cellWidth, cellHeight, cellWidth);
                graphicsContext.drawImage(characterImage, charCol * cellHeight, charRow * cellWidth, cellHeight, cellWidth);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameSolve = new SimpleStringProperty();

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public StringProperty imageFileNameGoalProperty() {
        return ImageFileNameGoal;
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameSolve() {
        return ImageFileNameSolve.get();
    }

    public void setImageFileNameSolve(String imageFileNameSolve) {
        this.ImageFileNameSolve.set(imageFileNameSolve);
    }

}
package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel {
    private int charRow, charCol;
    private Maze maze;
    private boolean solved = false;
    private boolean done = false;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public boolean isSolved() {
        return solved;
    }

    @Override
    public void generate(int row, int col) {
        threadPool.execute(() -> {
            done = false;
            solved = false;
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{row, col};
                            toServer.writeObject(mazeDimensions);
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject();
                            InputStream inStream = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            byte[] decompressedMaze = new byte[100000];
                            inStream.read(decompressedMaze);
                            Maze mMaze = new Maze(decompressedMaze);
                            maze = mMaze;
                            charRow = maze.getStartPosition().getRowIndex();
                            charCol = maze.getStartPosition().getColumnIndex();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setChanged();
                notifyObservers();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

    }

    public int[][] getMaze() {
        return maze.getMaze();
    }

    public int getCharacterPositionRow() {
        return charRow;
    }

    public int getCharacterPositionColumn() {
        return charCol;
    }

    public int getGoalPositionRow() {
        return maze.getGoalPosition().getRowIndex();
    }

    public int getGoalPositionColumn() {
        return maze.getGoalPosition().getColumnIndex();
    }

    @Override
    public void solve() {
        threadPool.execute(() -> {
            solved=true;
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            Maze m = new Maze(maze.getMaze(), new Position(charRow, charCol), maze.getGoalPosition());
                            toServer.writeObject(m);
                            toServer.flush();
                            int[][] t = m.getMaze();
                            Solution sol = (Solution) fromServer.readObject();
                            ArrayList<AState> solMoves = sol.getSolutionPath();
                            for (int i = 0; i < solMoves.size(); ++i) {
                                t[((MazeState) solMoves.get(i)).getPosition().getRowIndex()][((MazeState) solMoves.get(i)).getPosition().getColumnIndex()] = 2;
                            }

                            maze.setAllMaze(t);
                            maze.setStartPosition(new Position(charRow, charCol));
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        });
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        try {
            switch (movement) {
                case UP: {
                    if (maze.getMaze(charRow - 1, charCol) != 1) {
                        charRow--;
                    }
                    break;
                }
                case NUMPAD8: {
                    if (maze.getMaze(charRow - 1, charCol) != 1) {
                        charRow--;
                    }
                    break;
                }
                case DOWN: {
                    if (maze.getMaze(charRow + 1, charCol)!= 1) {
                        charRow++;
                    }
                    break;
                }
                case NUMPAD2: {
                    if (maze.getMaze(charRow + 1, charCol)!= 1) {
                        charRow++;
                    }
                    break;
                }
                case RIGHT: {
                    if (maze.getMaze(charRow,charCol + 1)!= 1) {
                        charCol++;
                    }
                    break;
                }
                case NUMPAD6: {
                    if (maze.getMaze(charRow,charCol + 1)!= 1) {
                        charCol++;
                    }
                    break;
                }
                case LEFT: {
                    if (maze.getMaze(charRow, charCol - 1)!= 1) {
                        charCol--;
                    }
                    break;
                }
                case NUMPAD4: {
                    if (maze.getMaze(charRow, charCol - 1)!= 1) {
                        charCol--;
                    }
                    break;
                }
                case NUMPAD7: {
                    if (maze.getMaze(charRow - 1,charCol - 1 )!= 1 && ( maze.getMaze(charRow - 1,charCol )!= 1 || maze.getMaze(charRow ,charCol - 1 )!= 1)){
                        charRow--;
                        charCol--;
                    }
                    break;
                }
                case NUMPAD9: {
                    if (maze.getMaze(charRow - 1,charCol + 1 )!= 1 && ( (maze.getMaze(charRow - 1,charCol )!= 1) || (maze.getMaze(charRow ,charCol + 1 )!= 1))){
                        charRow--;
                        charCol++;
                    }
                    break;
                }
                case NUMPAD1: {
                    if (maze.getMaze(charRow + 1,charCol - 1 )!= 1 && ((maze.getMaze(charRow + 1,charCol )!= 1) || (maze.getMaze(charRow ,charCol - 1 )!= 1))){
                        charRow++;
                        charCol--;
                    }
                    break;
                }
                case NUMPAD3: {
                    if (maze.getMaze(charRow + 1,charCol + 1 )!= 1 && (( maze.getMaze(charRow + 1,charCol )!= 1) || (maze.getMaze(charRow ,charCol + 1 )!= 1 ))){
                        charRow++;
                        charCol++;
                    }
                    break;
                }
                default:
                    return;
            }
        } catch (Exception e) {
        }
        if (charCol == getGoalPositionColumn() && charRow == getGoalPositionRow())
            done = true;
        setChanged();
        notifyObservers();
    }

    public boolean isDone() {
        return done;
    }

    public void saveMaze(File f) {
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream(f));
            outStream.writeObject(maze);
            outStream.writeObject(charRow);
            outStream.writeObject(charCol);
            outStream.writeObject(solved);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMaze(File f) throws Exception {
        done = false;
        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
        maze = (Maze) inStream.readObject();
        charRow = (int) inStream.readObject();
        charCol = (int) inStream.readObject();
        solved=(boolean) inStream.readObject();
        inStream.close();
        setChanged();
        notifyObservers();
    }

    public void reset(){
        maze=null;
        done = false;
        solved=false;
    }
}
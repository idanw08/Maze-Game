package View;

import ViewModel.MyViewModel;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface IView {

    void setMyViewModel(MyViewModel mvm);

    void reset();

    void exit();

    void setResizeEvent(Scene scene);

    void keyPressed(KeyEvent e);

    void focus();

    void mouseMove(MouseEvent move);
}
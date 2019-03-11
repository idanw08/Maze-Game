package View;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    public Label labelText;
    public ImageView imageChar;
    public ImageView imageGoal;
    public ImageView imageWall;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelText.setWrapText(true);
        labelText.setText("Your mission is helping Elyaniv Barda get the championship plate.\n" +
                "You can move him using the keyboard's arrows or numpads\n" +
                "or by dragging it using the mouse.\n" +
                "You can only move him in clear paths where there is no grass walls.\n" +
                "You can also save your maze by clicking the ''File'' in the upper toolsbar\n" +
                "and then press on ''Save'' box. After saving a maze you can load it any time by\n" +
                "clicking the ''Load'' box and continue playing from the last saving point.\n" +
                "You can also change the maze settings by clicking\n" +
                "the ''Options'' in the upper toolsbar and press on ''Properties'' box.\n");

        imageWall.setImage(new Image(getClass().getResourceAsStream("/Images/grass.jpg")));
        imageChar.setImage(new Image(getClass().getResourceAsStream("/Images/barda.png")));
        imageGoal.setImage(new Image(getClass().getResourceAsStream("/Images/plate.png")));
    }
}
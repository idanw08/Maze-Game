package View;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ResourceBundle;

public class WinController implements Initializable {
    public ImageView imageWin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageWin.setImage(new Image(getClass().getResourceAsStream("/Images/won.gif")));
        Media sound = new Media(getClass().getResource("/sounds/hbs_champ.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
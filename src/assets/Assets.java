package assets;

import javafx.scene.image.Image;

public class Assets {

    public static Image applicationIcon() {
        return new Image(Assets.class.getResourceAsStream("applicationIcon.png"));
    }
}

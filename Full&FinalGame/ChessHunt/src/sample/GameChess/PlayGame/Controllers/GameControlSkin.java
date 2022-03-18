package sample.GameChess.PlayGame.Controllers;
//imports
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

//skin is used to hava the skinable properties!! abstract type for the controller
//story behind the scene😒😒😒😒😒😒😒
class GameControlSkin extends SkinBase<GameControl> implements Skin<GameControl> {

	// One Kind of Gay!!!!
	public GameControlSkin(GameControl gameControl) {
		//call the super class constructor
		super(gameControl);
	}
}

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MagicGame;

public class GameLauncher
{
    public static void main (String[] args)
    {
        LwjglApplication launcher = new LwjglApplication( new MagicGame() );
    }
}
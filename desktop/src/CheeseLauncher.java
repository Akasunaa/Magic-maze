import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MagicGame;

public class CheeseLauncher
{
    public static void main (String[] args)
    {
        MagicGame myProgram = new MagicGame();
        LwjglApplication launcher = new LwjglApplication( myProgram );
    }
}
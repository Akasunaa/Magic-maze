import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.menu.MagicGame;

public class GameLauncher
{
    public static void main (String[] args)
    {
        LwjglApplication launcher = new LwjglApplication( new MagicGame() );
    }
}
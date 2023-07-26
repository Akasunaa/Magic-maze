package tsp.genint.screens.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class Logs extends TextArea{
    public Logs(Skin skin){
        super("", skin, "logStyle");
        setVisible(true);
        setText("Début de la partie!");
    }

    public void newMessage(String message){
        appendText(message);
        /*
        Fun fact sur ici
        avant il y avait appendText("\n" + message);
        parce qu'il faut revenir à la ligne.
        Sauf que quand j'ai résolu mon problème d'accent, avec le convertissage en UTF8
        Ou un truc du genre, tbh c'était très mystique
        Eh ben maintenant il faut supprimer ce retour à la ligne sinon il en met trop
        C'est très, très mystique
         */
    }
}

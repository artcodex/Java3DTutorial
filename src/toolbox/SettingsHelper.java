package toolbox;

import org.lwjgl.input.Keyboard;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;

/**
 * Created by abrenner on 3/10/17.
 */
public class SettingsHelper {
    public void update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            //Toggle cell shading
            MasterRenderer.setUseCellShading(!MasterRenderer.isUseCellShading());
        }
    }
}

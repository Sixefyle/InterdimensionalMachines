package be.sixefyle.transdimquarry.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.transdimensional";
    public static final String KEY_OPEN_TOOL_GUI = "key.transdimensional.open_tool_gui";

    public static final KeyMapping TOOL_SETTINGS_KEY = new KeyMapping(KEY_OPEN_TOOL_GUI, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY);
}

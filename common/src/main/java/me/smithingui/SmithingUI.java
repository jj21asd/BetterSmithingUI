package me.smithingui;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SmithingUI {
    public static final String MOD_ID = "better_smithing_ui";
    public static final String DISPLAY_NAME = "Better Smithing UI";
    public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);
    public static final String PACK_ROOT = "resourcepacks";

    public static void init() {
        BuiltInPackProvider packProvider = new BuiltInPackProvider(PACK_ROOT);
        BuiltInPackProvider.register(packProvider);

        packProvider.addPack("dark_ui", Text.literal("Dark Smithing UI"));
        packProvider.addPack("transparent_ui", Text.literal("Transparent Smithing UI"));
    }

    public static Identifier asId(String path) {
        return new Identifier(MOD_ID, path);
    }
}

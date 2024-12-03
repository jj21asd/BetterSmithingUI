package me.smithingui;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SmithingUI {
    public static final String MOD_ID = "better_smithing_ui";
    public static final String NAME = "BetterSmithingUI";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final String PACK_ROOT = "resourcepacks";

    public static void init() {
        BuiltInResourcePackProvider packProvider = new BuiltInResourcePackProvider(PACK_ROOT);
        BuiltInResourcePackProvider.register(packProvider);

        packProvider.addPack("dark_ui", Text.literal("Dark Smithing UI"));
        packProvider.addPack("transparent_ui", Text.literal("Transparent Smithing UI"));
    }

    public static Identifier asId(String path) {
        return Identifier.of(MOD_ID, path);
    }
}

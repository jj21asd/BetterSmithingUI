package me.smithingui;

import dev.architectury.hooks.PackRepositoryHooks;
import dev.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.*;
import net.minecraft.text.Text;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class BuiltInResourcePackProvider implements ResourcePackProvider {
    private final String rootPath;
    private final List<ResourcePackProfile> profiles;

    private static final ResourcePackPosition INSERT_POS;

    public BuiltInResourcePackProvider(String rootPath) {
        this.rootPath = rootPath;
        this.profiles = new ArrayList<>();
    }

    public static void register(BuiltInResourcePackProvider packProvider) {
        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();
        PackRepositoryHooks.addSource(manager, packProvider);
    }

    public void addPack(String path, Text name) {
        Platform.getMod(SmithingUI.MOD_ID).findResource(rootPath, path).ifPresentOrElse((nioPath) -> {
            String id = SmithingUI.MOD_ID + "/" + path;

            ResourcePackProfile pack = ResourcePackProfile.create(
                    new ResourcePackInfo(id, name, ResourcePackSource.BUILTIN, Optional.empty()),
                    new BuiltInPackFactory(nioPath), ResourceType.CLIENT_RESOURCES, INSERT_POS);

            Objects.requireNonNull(pack);
            profiles.add(pack);
        }, () -> {
            SmithingUI.LOGGER.error("Failed to load built-in resourcepack: \"{}\"", path);
        });
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        for (ResourcePackProfile profile : profiles) {
            profileAdder.accept(profile);
        }
    }

    static {
        INSERT_POS = new ResourcePackPosition(
                false, // Always active
                ResourcePackProfile.InsertionPosition.TOP,
                false // Fixed position
        );
    }

    private record BuiltInPackFactory(Path path) implements ResourcePackProfile.PackFactory {
        @Override
        public ResourcePack open(ResourcePackInfo info) {
            return new DirectoryResourcePack(info, path);
        }

        @Override
        public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
            return open(info); // Idk what this does, might cause problems in the future
        }
    }
}

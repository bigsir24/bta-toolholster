package bigsir.toolholster;

import bigsir.toolholster.core.net.ConfigMessage;
import bigsir.toolholster.core.net.HolsterMessage;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;


public class ToolHolster implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "toolholster";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("Tool Holster initialized.");

		NetworkHandler.registerNetworkMessage(HolsterMessage::new);
		NetworkHandler.registerNetworkMessage(ConfigMessage::new);
    }

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object o) {
		return  (T) o;
	}

	public static boolean isValidTool(@Nullable ItemStack stack, @NotNull Player player) {
		return stack != null && stack.itemID >= 16384;
	}

	@Override
	public void beforeGameStart() {
	}

	@Override
	public void afterGameStart() {

	}
}

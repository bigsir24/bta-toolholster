package bigsir.toolholster;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class ToolHolster implements ModInitializer {
    public static final String MOD_ID = "toolholster";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("Tool Holster initialized.");
    }

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object o) {
		return  (T) o;
	}
}

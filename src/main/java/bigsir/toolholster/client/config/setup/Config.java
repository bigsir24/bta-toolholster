package bigsir.toolholster.client.config.setup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class Config<T> {
	private T config;

	public Config(@NotNull T t){
		this.config = t;
    }

	public @NotNull T getConfig() {
		return config;
	}

	@SuppressWarnings("unchecked")
	public void setConfig(Object config) {
		this.config = (T) config;
	}
}

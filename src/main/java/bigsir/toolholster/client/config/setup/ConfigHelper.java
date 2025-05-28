package bigsir.toolholster.client.config.setup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class ConfigHelper {

	private static final Map<String, ModConfigData> modConfigMap = new HashMap<>();

	public static void registerConfig(String modId, Config<?> configRef) {
		modConfigMap.put(modId, new ModConfigData(configRef.getConfig().getClass(), configRef));
	}

	public static Class<?> getConfigClass(String modId){
		ModConfigData data = modConfigMap.get(modId);
		return data == null ? null : data.configClass;
	}

	public static Config<?> getConfigRef(String modId){
		ModConfigData data = modConfigMap.get(modId);
		return data == null ? null : data.configRef;
	}

	public static Set<String> getModIds(){
		return modConfigMap.keySet();
	}

	private static class ModConfigData{
		public Class<?> configClass;
		public Config<?> configRef;
		public ModConfigData(Class<?> configClass, Config<?> configRef){
			this.configClass = configClass;
			this.configRef = configRef;
		}
	}
}

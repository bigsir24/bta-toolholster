package bigsir.toolholster.client.config.setup;

import bigsir.toolholster.ToolHolster;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigReader {
	private static final Gson GSON = new GsonBuilder().setLenient().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
	private static final String SEPARATOR = FabricLoader.getInstance().getConfigDir().getFileSystem().getSeparator();

	public static void loadModConfigs() {
		for (String modId : ConfigHelper.getModIds()) {
			File jsonFile = new File(FabricLoader.getInstance().getConfigDir().toFile().toPath() + SEPARATOR + modId + ".json");
			if (ConfigHelper.getConfigClass(modId) == null || ConfigHelper.getConfigRef(modId) == null) continue;

			if (!jsonFile.exists()) {
				String jsonString = GSON.toJson(ConfigHelper.getConfigRef(modId).getConfig());

				try (FileWriter fileWriter = new FileWriter(jsonFile)) {
					fileWriter.write(jsonString);
				} catch (IOException e) {
					ToolHolster.LOGGER.error("Failed to create default config");
				}

			} else {
				try {
					BufferedReader br = new BufferedReader(new FileReader(jsonFile));
					if (ConfigHelper.getConfigClass(modId) != null) {
						Object configObject = GSON.fromJson(br, ConfigHelper.getConfigClass(modId));

						//if(Objects.equals(modId, ToolHolster.MOD_ID)) System.out.println(Arrays.toString(ToolHolster.holsterConfig.getConfig().holsteredItems));
						ConfigHelper.getConfigRef(modId).setConfig(configObject);
					}
				} catch (FileNotFoundException e) {
					throw new RuntimeException();
				}
			}

			((IModConfig)ConfigHelper.getConfigRef(modId).getConfig()).init();
		}
	}
}

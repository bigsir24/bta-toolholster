package bigsir.toolholster;

import bigsir.toolholster.client.config.THConfig;
import bigsir.toolholster.client.config.setup.ConfigHelper;
import bigsir.toolholster.client.config.setup.ConfigReader;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static bigsir.toolholster.ToolHolster.MOD_ID;

public class THClient implements ClientStartEntrypoint {
	@Override
	public void beforeClientStart() {
		ConfigHelper.registerConfig(MOD_ID, THConfig.configWrapper);
	}

	@Override
	public void afterClientStart() {
		ConfigReader.loadModConfigs();
	}
}

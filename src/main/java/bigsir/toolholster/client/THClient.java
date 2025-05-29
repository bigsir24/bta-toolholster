package bigsir.toolholster.client;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.config.THConfig;
import bigsir.toolholster.client.config.setup.ConfigHelper;
import bigsir.toolholster.client.config.setup.ConfigReader;
import bigsir.toolholster.core.data.ItemConfig;
import bigsir.toolholster.core.net.ConfigMessage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.net.packet.PacketCustomPayload;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static bigsir.toolholster.ToolHolster.MOD_ID;

@Environment(EnvType.CLIENT)
public class THClient implements ClientStartEntrypoint {
	@Override
	public void beforeClientStart() {
		ConfigHelper.registerConfig(MOD_ID, THConfig.configWrapper);
	}

	@Override
	public void afterClientStart() {
		ConfigReader.loadModConfigs();
	}

	public static void receiveModPresentPacket(PacketCustomPayload packet) {
		if (!packet.channel.equals(MOD_ID)) return;

		String string = new String(packet.data, StandardCharsets.UTF_8);
		if ("HELLO".equals(string)) {
			 sendConfigMessage(THConfig.getConfigList());
		}
	}

	public static void sendConfigMessage(List<ItemConfig> configList) {
		NetworkHandler.sendToServer(new ConfigMessage(Minecraft.getMinecraft().session.uuid, configList));
	}
}

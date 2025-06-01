package bigsir.toolholster.client;

import bigsir.toolholster.client.config.THConfig;
import bigsir.toolholster.client.config.setup.ConfigHelper;
import bigsir.toolholster.client.config.setup.ConfigReader;
import bigsir.toolholster.core.data.ItemConfig;
import bigsir.toolholster.core.net.ConfigMessage;
import bigsir.toolholster.core.data.IDKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketCustomPayload;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bigsir.toolholster.ToolHolster.MOD_ID;

@Environment(EnvType.CLIENT)
public class THClient implements ClientStartEntrypoint {
	private static final Map<IDKey, ItemConfig> config = new HashMap<>();

	public static byte getFlags(@Nullable ItemStack stack) {
		return stack == null ? 0 : getFlags(stack.itemID);
	}

	public static byte getFlags(@Nullable Item item) {
		return item == null ? 0 : getFlags(item.id);
	}

	public static byte getFlags(int itemID) {
		ItemConfig ic = config.get(IDKey.getTemp(itemID));
		return ic == null ? 0 : ic.flags;
	}

	public static boolean doHolster(@Nullable ItemStack stack) {
		if (stack == null) return false;

		return config.containsKey(IDKey.getTemp(stack.itemID));
	}

	@Override
	public void beforeClientStart() {
		ConfigHelper.registerConfig(MOD_ID, THConfig.configWrapper);
	}

	@Override
	public void afterClientStart() {
		ConfigReader.loadModConfigs();
		for (ItemConfig ic : THConfig.getConfigList()) {
			config.put(new IDKey(ic.itemID), new ItemConfig(ic.itemID, ic.flags));
		}
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

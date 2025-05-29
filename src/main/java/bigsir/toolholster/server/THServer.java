package bigsir.toolholster.server;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.core.data.ItemConfig;
import bigsir.toolholster.core.net.ConfigMessage;
import bigsir.toolholster.core.net.HolsterMessage;
import bigsir.toolholster.server.data.IDKey;
import bigsir.toolholster.server.data.PlayerDataServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.server.net.handler.PacketHandlerServer;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static bigsir.toolholster.ToolHolster.MOD_ID;

@Environment(EnvType.SERVER)
public class THServer {
	private static final Map<UUID, Map<IDKey, ItemConfig>> configData = new HashMap<>();

	public static void receiveConfigData(ConfigMessage message) {
		Map<IDKey, ItemConfig> configMap = new HashMap<>();

		for (ItemConfig config : message.configs) {
			configMap.put(new IDKey(config.itemID), config);
		}

		configData.put(message.playerUUID, configMap);
		System.out.println(configMap);
	}

	public static void sendHolsterMessage(PlayerDataServer data) {
		Player player = data.player;
		ItemStack stack = data.getHolsteredItem();
		Map<IDKey, ItemConfig> configMap = configData.get(player.uuid);

		ItemConfig config = stack == null ? null : configMap.get(IDKey.getTemp(stack.getItem()));
		byte flags = config == null ? 0 : config.flags;

		NetworkHandler.sendToAllPlayers(new HolsterMessage(player.uuid, stack, flags));
	}

	public static void sendModPresentPacket(PacketHandlerServer packetHandler) {
		packetHandler.sendPacket(new PacketCustomPayload(MOD_ID, "HELLO".getBytes()));
	}
}

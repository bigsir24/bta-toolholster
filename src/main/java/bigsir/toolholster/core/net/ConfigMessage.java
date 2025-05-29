package bigsir.toolholster.core.net;

import bigsir.toolholster.core.data.ItemConfig;
import bigsir.toolholster.server.THServer;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfigMessage implements NetworkMessage {
	public UUID playerUUID;
	public List<ItemConfig> configs;

	public ConfigMessage() {}

	public ConfigMessage(@NotNull UUID playerUUID, @NotNull List<ItemConfig> configs) {
		this.playerUUID = playerUUID;
		this.configs = configs;
	}

	@Override
	public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
		packet.writeLong(playerUUID.getMostSignificantBits());
		packet.writeLong(playerUUID.getLeastSignificantBits());
		packet.writeInt(configs.size());

		for (ItemConfig config : this.configs) {
			packet.writeInt(config.itemID);
			packet.writeByte(config.flags);
		}
	}

	@Override
	public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
		this.playerUUID = new UUID(packet.readLong(), packet.readLong());
		int size = packet.readInt();
		configs = new ArrayList<>(size);

		for (int i = 0; i < size; i++)
			configs.add(new ItemConfig(packet.readInt(), packet.readByte()));
	}

	@Override
	public void handle(NetworkContext context) {
		if (EnvironmentHelper.isServerEnvironment()) {
			THServer.receiveConfigData(this);
		}
	}
}

package bigsir.toolholster.core.net;

import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.interfaces.IPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.UUID;

public class HolsterMessage implements NetworkMessage {
	public UUID playerUUID;
	public @Nullable ItemStack stack;
	public byte flags; // | isPresent? | full3D? | 0:back 1:side | mirrored? |

	public HolsterMessage() {}

	public HolsterMessage(@NotNull UUID uuid, @Nullable ItemStack stack, int flags) {
		this.stack = stack;
		this.flags = (byte) (stack == null ? 0 : flags | 1);
		this.playerUUID = uuid;
	}

	@Override
	public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
		packet.writeByte(flags);
		packet.writeLong(playerUUID.getMostSignificantBits());
		packet.writeLong(playerUUID.getLeastSignificantBits());

		if (stack != null) {
			packet.writeInt(stack.itemID);
			packet.writeInt(stack.stackSize);
			packet.writeInt(stack.getMetadata());
			packet.writeCompoundTag(stack.getData());
		}
	}

	@Override
	public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
		this.flags = packet.readByte();
		this.playerUUID = new UUID(packet.readLong(), packet.readLong());

		if ((this.flags & 1) == 1) {
			this.stack = new ItemStack(
				packet.readInt(),
				packet.readInt(),
				packet.readInt(),
				packet.readCompoundTag()
			);
		}
	}

	@Override
	public void handle(NetworkContext context) {
		if (context.player.world == null) return;

		//Do not process own packets, handle locally
		Player player = context.player.world.getPlayerEntityByUUID(this.playerUUID);
		//if (player == Minecraft.getMinecraft().thePlayer) return;

		PlayerData data = ((IPlayer)player).getData();
		data.setHolsteredItem(this.stack);
		data.flags = this.flags;
	}
}

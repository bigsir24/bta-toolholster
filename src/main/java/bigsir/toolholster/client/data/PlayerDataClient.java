package bigsir.toolholster.client.data;

import bigsir.toolholster.client.THClient;
import bigsir.toolholster.core.data.PlayerData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PlayerDataClient extends PlayerData {
	public PlayerDataClient(Player player) {
		super(player);
	}

	@Override
	public void onChange() {
		this.flags = THClient.getFlags(this.holsteredItemPtr.get());
	}

	@Override
	public void setHolsteredItem(@Nullable ItemStack holsteredItem) {
		super.setHolsteredItem(holsteredItem);
		this.flags = THClient.getFlags(holsteredItem);
	}
}

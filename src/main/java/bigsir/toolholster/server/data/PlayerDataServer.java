package bigsir.toolholster.server.data;

import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.server.THServer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerDataServer extends PlayerData {
	public PlayerDataServer(Player player) {
		super(player);
	}

	@Override
	public void onChange() {
		super.onChange();

		THServer.sendHolsterMessage(this);
	}

	@Override
	public void setHolsteredItem(@Nullable ItemStack holsteredItem) {
		super.setHolsteredItem(holsteredItem);

		THServer.sendHolsterMessage(this);
	}
}

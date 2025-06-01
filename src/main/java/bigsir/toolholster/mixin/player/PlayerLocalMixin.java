package bigsir.toolholster.mixin.player;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.data.PlayerDataClient;
import bigsir.toolholster.interfaces.IPlayer;
import net.minecraft.client.entity.player.PlayerLocal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PlayerLocal.class, remap = false)
public abstract class PlayerLocalMixin implements IPlayer {
	@Override
	public void initData() {
		this.setData(new PlayerDataClient(ToolHolster.cast(this)));
	}
}

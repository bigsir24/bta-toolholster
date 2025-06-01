package bigsir.toolholster.mixin.player;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.THClient;
import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.interfaces.IPlayer;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixinClient implements IPlayer {
	@Shadow
	public abstract ItemStack getCurrentEquippedItem();

	//Runs only on PlayerLocal
	@Inject(method = "tick", at = @At("HEAD"))
	public void tickLocal(CallbackInfo ci) {
		Player thisRef = ToolHolster.cast(this);

		if (thisRef instanceof PlayerLocal) {
			ItemStack currentItem = this.getCurrentEquippedItem();
			PlayerData data = this.getData();

			if ((data.getOldItem() == null || currentItem == null || !data.getOldItem().isStackEqual(currentItem))) {
				if (THClient.doHolster(data.getOldItem())) {
					data.setHolsteredItem(data.getOldItem());
				} else if (data.getHolsteredItem() != null && currentItem != null && data.getHolsteredItem().isStackEqual(currentItem)) {
					data.setHolsteredItem(null);
				}
			}

			data.setOldItem(thisRef.getCurrentEquippedItem());
		}
	}
}

package bigsir.toolholster.mixin.player;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.THClient;
import bigsir.toolholster.client.data.PlayerDataClient;
import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.interfaces.IPlayer;
import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerRemote.class, remap = false)
public abstract class PlayerRemoteMixin extends Player implements IPlayer {
	public PlayerRemoteMixin(World world) {
		super(world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void test(CallbackInfo ci) {
		//Manage items locally if mod is not installed on the server
		if (!THClient.isServerModPresent()) {
			ItemStack currentItem = this.getCurrentEquippedItem();
			Player thisRef = ToolHolster.cast(this);
			PlayerData data = this.getData();

			if((data.getOldItem() == null || currentItem == null || !data.getOldItem().isStackEqual(currentItem))){
				if(THClient.doHolster(data.getOldItem())){
					data.setHolsteredItem(data.getOldItem());
				}else if(data.getHolsteredItem() != null && currentItem != null && data.getHolsteredItem().isStackEqual(currentItem)){
					data.setHolsteredItem(null);
				}
			}
			data.setOldItem(thisRef.getCurrentEquippedItem());
		}
	}

	@Override
	public void initData() {
		if (!THClient.isServerModPresent()) {
			this.setData(new PlayerDataClient(this));
		}else {
			((IPlayer)this).initData();
		}
	}
}

package bigsir.toolholster.mixin.player;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.interfaces.IPlayer;
import bigsir.toolholster.server.THServer;
import bigsir.toolholster.server.data.PlayerDataServer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixin extends Player implements IPlayer {
	public PlayerServerMixin(World world) {
		super(world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tool(CallbackInfo ci) {
		//Handle tool switching on the server, signal changes to the client

		ItemStack currentItem = this.getCurrentEquippedItem();
		Player thisRef = ToolHolster.cast(this);
		PlayerData data = this.getData();

		if((data.getOldItem() == null || currentItem == null || !data.getOldItem().isStackEqual(currentItem))){
			if(THServer.doHolster(thisRef, data.getOldItem())){
				data.setHolsteredItem(data.getOldItem());
			}else if(data.getHolsteredItem() != null && currentItem != null && data.getHolsteredItem().isStackEqual(currentItem)){
				data.setHolsteredItem(null);
				//System.out.println("called");
			}
		}

		data.setOldItem(thisRef.getCurrentEquippedItem());
	}

	@Override
	public void initData() {
		this.setData(new PlayerDataServer(this));
	}
}

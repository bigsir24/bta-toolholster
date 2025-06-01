package bigsir.toolholster.mixin.player;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.THClient;
import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.core.data.Pointer;
import bigsir.toolholster.interfaces.IPlayer;
import bigsir.toolholster.interfaces.IPointerStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.Session;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerLocalMultiplayer.class, remap = false)
public abstract class PlayerLocalMultiplayerMixin extends PlayerLocal implements IPlayer {
	public PlayerLocalMultiplayerMixin(Minecraft minecraft, World world, Session session, int dimension) {
		super(minecraft, world, session, dimension);
	}

	@Inject(method = "dropCurrentItem", at = @At("TAIL"))
	public void removePointer(boolean dropFullStack, CallbackInfo ci) {
		//Delete the Client's tool on server
		Pointer.delete(this.getCurrentEquippedItem());
		if (dropFullStack || getCurrentEquippedItem().stackSize - 1 <= 0) this.destroyCurrentEquippedItem();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tool(CallbackInfo ci) {
		//Handle tool switching on the server, signal changes to the client

		/*ItemStack currentItem = this.getCurrentEquippedItem();
		Player thisRef = ToolHolster.cast(this);
		PlayerData data = this.getData();

		if((data.getOldItem() == null || currentItem == null || !data.getOldItem().isStackEqual(currentItem))){
			if(THClient.doHolster(data.getOldItem())){
				data.setHolsteredItem(data.getOldItem());
			}else if(data.getHolsteredItem() != null && currentItem != null && data.getHolsteredItem().isStackEqual(currentItem)){
				data.setHolsteredItem(null);
				//System.out.println("called");
			}
		}

		data.setOldItem(thisRef.getCurrentEquippedItem());*/
	}
}

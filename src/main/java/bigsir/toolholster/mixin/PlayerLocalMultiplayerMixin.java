package bigsir.toolholster.mixin;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.core.data.Pointer;
import bigsir.toolholster.interfaces.IPlayer;
import bigsir.toolholster.interfaces.IPointerStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
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
		//FIXME item isn't nulled immediately so it gets reassigned to the same item while client is waiting for a response
		Pointer.delete(this.getCurrentEquippedItem());
	}
}

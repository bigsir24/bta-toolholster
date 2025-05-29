package bigsir.toolholster.mixin;

import bigsir.toolholster.interfaces.IPlayer;
import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.core.entity.player.Player;
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
	public void test(CallbackInfo ci) { //FIXME should be replaced by custom packet handling
		//this.getData().setHolsteredItem(this.entityData.getItemStack(19));
	}
}

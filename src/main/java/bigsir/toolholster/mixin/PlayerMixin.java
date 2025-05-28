package bigsir.toolholster.mixin;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.PlayerData;
import bigsir.toolholster.client.Pointer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin {
	@Inject(method = "dropPlayerItemWithRandomChoice", at = @At("HEAD"))
	public void del(ItemStack itemstack, boolean flag, CallbackInfo ci) {
		Pointer.delete(ToolHolster.cast(itemstack));
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	public void clearTools(Entity entityKilledBy, CallbackInfo ci) {
		//TODO clear tool data
	}
}

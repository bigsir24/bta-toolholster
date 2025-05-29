package bigsir.toolholster.mixin;

import bigsir.toolholster.core.data.Pointer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class ContainerInventoryMixin {
	@Shadow
	public ItemStack[] mainInventory;

	@Inject(method = "insertItem", at = @At(value = "RETURN", ordinal = 1))
	public void transferPtr(ItemStack stackToAdd, boolean useHotbarOffset, CallbackInfo ci, @Local(name = "slotId") int slotId) {
		Pointer.transfer(stackToAdd, this.mainInventory[slotId]);
	}
}

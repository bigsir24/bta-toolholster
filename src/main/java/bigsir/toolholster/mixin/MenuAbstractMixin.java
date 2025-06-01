package bigsir.toolholster.mixin;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.core.data.Pointer;
import bigsir.toolholster.interfaces.IPointerStorage;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = MenuAbstract.class, remap = false)
public abstract class MenuAbstractMixin {
	@Shadow
	public abstract Slot getSlot(int i);

	@Unique Slot lastSlot;

	@Inject(method = "storeOrDropItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;dropPlayerItem(Lnet/minecraft/core/item/ItemStack;)V", shift = At.Shift.AFTER))
	public void insert(Player player, ItemStack stack, CallbackInfo ci) {
		//Pointer.delete(stack);
	}

	@Inject(method = "handleItemMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/slot/Slot;set(Lnet/minecraft/core/item/ItemStack;)V"))
	public void passPtrShiftMove(InventoryAction action, Slot slot, int target, Player player, CallbackInfo ci, @Local(name = "item") ItemStack item) {
		if (lastSlot != null) {
			if (!(lastSlot.getContainer() instanceof ContainerInventory)) {
				Pointer.delete(slot.getItemStack());
			}else {
				Pointer.transfer(slot.getItemStack(), lastSlot.getItemStack());
			}

			lastSlot = null;
		}
	}

	@Inject(method = "mergeItems(Lnet/minecraft/core/item/ItemStack;Ljava/util/List;)V", at = @At(value = "RETURN", ordinal = 1))
	public void shiftStack(ItemStack stack, List targetSlots, CallbackInfo ci, @Local(ordinal = 0) Slot slot) {
		lastSlot = slot;
		//Pointer.transfer(ToolHolster.cast(stack), ToolHolster.cast(slot.getItemStack()));
	}

	@Inject(method = "mergeItems(Lnet/minecraft/core/item/ItemStack;Ljava/util/List;)V", at = @At(value = "RETURN", ordinal = 2))
	public void shiftStack2(ItemStack stack, List targetSlots, CallbackInfo ci, @Local(ordinal = 0) Slot slot) {
		lastSlot = slot;
		//Pointer.transfer(ToolHolster.cast(stack), ToolHolster.cast(slot.getItemStack()));
	}

	@Inject(method = "clicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/slot/Slot;set(Lnet/minecraft/core/item/ItemStack;)V", ordinal = 3, shift = At.Shift.AFTER))
	public void placeStackFromHand(InventoryAction action, int[] args, Player player, CallbackInfoReturnable<ItemStack> cir, @Local(name = "slot") Slot slot) {
		if (!(slot.getContainer() instanceof ContainerInventory)) {
			Pointer.delete(player.inventory.getHeldItemStack());
		}else {
			Pointer.transfer(player.inventory.getHeldItemStack(), slot.getItemStack());

		}
	}

	@Inject(method = "clicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/slot/Slot;onTake(Lnet/minecraft/core/item/ItemStack;)V", ordinal = 1))
	public void putStackInHand(InventoryAction action, int[] args, Player player, CallbackInfoReturnable<ItemStack> cir, @Local(name = "stackInSlot") ItemStack stackInSlot) {
		Pointer.transfer(stackInSlot, player.inventory.getHeldItemStack());
	}
}

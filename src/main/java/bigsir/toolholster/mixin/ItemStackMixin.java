package bigsir.toolholster.mixin;

import bigsir.toolholster.core.data.Pointer;
import bigsir.toolholster.interfaces.IPointerStorage;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ItemStack.class, remap = false)
public abstract class ItemStackMixin implements IPointerStorage<ItemStack> {
	@Unique
	private Pointer<ItemStack> ptr;

	/*@Inject(method = "<init>(Lnet/minecraft/core/item/ItemStack;)V", at = @At("TAIL"))
	public void transferPointer(ItemStack itemStack, CallbackInfo ci) {
		//FIXME might be needed?
		//Pointer.transfer(ptr, ToolHolster.cast(this), ToolHolster.cast(itemStack));
	}

	@Redirect(method = "copy", at = @At(value = "NEW", target = "(IIILcom/mojang/nbt/tags/CompoundTag;)Lnet/minecraft/core/item/ItemStack;"))
	public ItemStack transfer(int itemID, int stackSize, int metadata, CompoundTag tag) {
		ItemStack stack = new ItemStack(itemID, stackSize, metadata, tag);
		//Pointer.transfer(this, stack);
		return stack;
	}*/

	@Override
	public Pointer<ItemStack> getPointer() {
		return ptr;
	}

	@Override
	public void setPointer(Pointer<ItemStack> ptr) {
		this.ptr = ptr;
	}

	@Override
	public void clearReference() {
		this.ptr = null;
	}
}

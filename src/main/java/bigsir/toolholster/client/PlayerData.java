package bigsir.toolholster.client;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerData {
	public final @NotNull Pointer<ItemStack> holsteredItemPtr = new Pointer<>();
	public final @NotNull Pointer<ItemStack> oldItemPtr = new Pointer<>();

	public PlayerData(){
	}

	public boolean isHolstered() {
		return holsteredItemPtr.get() != null;
	}

	public void clear() {
		holsteredItemPtr.clear();
		oldItemPtr.clear();
	}

	public void setHolsteredItem(@Nullable ItemStack holsteredItem) {
		this.holsteredItemPtr.set(holsteredItem);
	}

	public @Nullable ItemStack getHolsteredItem() {
		return holsteredItemPtr.get();
	}

	public @Nullable ItemStack getOldItem() {
		return oldItemPtr.get();
	}

	public void setOldItem(@Nullable ItemStack oldItem) {
		this.oldItemPtr.set(oldItem);
	}
}

package bigsir.toolholster.core.data;

import bigsir.toolholster.interfaces.IPointerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerData implements IPointerListener {
	public final @NotNull Pointer<ItemStack> holsteredItemPtr = new Pointer<ItemStack>().withListener(this);
	public final @NotNull Pointer<ItemStack> oldItemPtr = new Pointer<>();
	public final Player player;
	public byte flags;
	private static final int FULL_3D_MASK = 0b0000_0010;
	private static final int SIDE_MASK = 0b0000_0100;
	private static final int MIRRORED_MASK = 0b0000_1000;

	public PlayerData(Player player){
		this.player = player;
	}

	@Override
	public void onChange() {
		/*if (EnvironmentHelper.isServerEnvironment()) {
			HolsterMessage message = new HolsterMessage(player.uuid, holsteredItemPtr.get(), (byte)1);
			NetworkHandler.sendToAllPlayers(message);
		}*/
	}

	public boolean isFull3D() {
		return (this.flags & FULL_3D_MASK) != 0;
	}

	public boolean isBackHolster() {
		return (this.flags & SIDE_MASK) == 0;
	}

	public boolean isMirrored() {
		return (this.flags & MIRRORED_MASK) != 0;
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

		/*if (EnvironmentHelper.isServerEnvironment()) {
			HolsterMessage message = new HolsterMessage(player.uuid, holsteredItem, (byte)1);
			NetworkHandler.sendToAllPlayers(message);
		}*/
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

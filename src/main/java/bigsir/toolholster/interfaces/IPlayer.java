package bigsir.toolholster.interfaces;

import bigsir.toolholster.core.data.PlayerData;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IPlayer {
	void setItem(@Nullable ItemStack stack);

	PlayerData getData();

	void initData();

	void setData(PlayerData data);
}

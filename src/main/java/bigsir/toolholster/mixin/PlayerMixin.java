package bigsir.toolholster.mixin;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.core.data.PlayerData;
import bigsir.toolholster.core.data.Pointer;
import bigsir.toolholster.interfaces.IPlayer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Mob implements IPlayer {
	@Shadow
	public abstract ItemStack getCurrentEquippedItem();

	@Unique
	private PlayerData data;

	public PlayerMixin(@Nullable World world) {
		super(world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void initInject(World world, CallbackInfo ci) {
		this.initData();
	}

	@Inject(method = "dropPlayerItemWithRandomChoice", at = @At("HEAD"))
	public void del(ItemStack itemstack, boolean flag, CallbackInfo ci) {
		Pointer.delete(ToolHolster.cast(itemstack));
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	public void clearTools(Entity entityKilledBy, CallbackInfo ci) {
		//TODO clear tool data
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	public void synchedData(CallbackInfo ci) {
		this.entityData.define(19, null, ItemStack.class);
	}

	@Override
	public void setItem(@Nullable ItemStack stack) {
		this.entityData.set(19, stack);
	}

	@Override
	public PlayerData getData() {
		return data;
	}

	@Override
	public void initData() {
		this.data = new PlayerData((Player) (Object)this);
	}

	@Override
	public void setData(PlayerData data) {
		this.data = data;
	}
}

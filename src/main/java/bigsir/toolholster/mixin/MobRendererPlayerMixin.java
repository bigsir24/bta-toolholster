package bigsir.toolholster.mixin;

import bigsir.toolholster.client.PlayerData;
import bigsir.toolholster.client.config.THConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemBow;
import net.minecraft.core.item.ItemQuiver;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {
	@Shadow
	private ModelBiped modelBipedMain;

	@Shadow
	public abstract void render(Tessellator tessellator, Player entity, double x, double y, double z, float yaw, float partialTick);

	@Unique
	private static final Map<UUID, PlayerData> modelCache = new HashMap<>();

	@Unique
	private PlayerData data;


	public MobRendererPlayerMixin(ModelBase model, float shadowSize) {
		super(model, shadowSize);
	}

	@Redirect(method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelBiped;renderCloak(F)V"))
	public void noCape(ModelBiped instance, float scale, @Local(name = "player") Player player) {
		this.data = modelCache.get(player.uuid); //TODO add option for side holster
		if (data != null && data.isHolstered()) return; //TODO add option to remove cape on holster

		instance.renderCloak(scale);
	}

	@Inject(
		method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/container/ContainerInventory;getCurrentItem()Lnet/minecraft/core/item/ItemStack;")
	)
	public void renderItem(Player player, float partialTick, CallbackInfo ci){
		data =  modelCache.get(player.uuid);
		if(data == null) modelCache.put(player.uuid, data = new PlayerData());


		if (isValidTool(data.getHolsteredItem(), player)) {
			GL11.glPushMatrix();

			this.modelBipedMain.body.translateTo(0.0625F);

			ItemModel model = ItemModelDispatcher.getInstance().getDispatch(data.getHolsteredItem());

			//getSideHolsterTransform(model, player);
			getBackHolsterTransform(model, player); //TODO control this with configs, sync configs in multiplayer

			/*boolean hasChestplate = player.inventory.armorItemInSlot(2) != null && !(player.inventory.armorItemInSlot(2).getItem() instanceof ItemQuiver);

			//boolean full3D = model instanceof ItemModelBow || model instanceof ItemModelStandard && ((ItemModelStandardAccessor)model).getBFull3D();
			boolean full3D = THConfig.isFull3D(model.item.id, player.uuid);
			float scale = (full3D ? 0.625F : 0.375F) * 1.5F;
			float torsoOffset = full3D ? 0 : -1/32F;

			boolean mirrorRender = THConfig.instance.mirrorRender;

			GL11.glTranslatef(-0.51F, -0.22F, hasChestplate ? 4/16F + torsoOffset : 3/16F - 0.01F + torsoOffset);

			//Null is handled in isValidTool
			if(data.getHolsteredItem().getItem() instanceof ItemBow) GL11.glTranslatef(0, 0, -2/16F);

			//TODO: attempt to get small items to render centered also

			//Translate to middle of item
			GL11.glTranslatef(0.5F * 1, 0.5F * 1, 0.03125F * 1);
			//Rotate here, origin is the middle of item
			GL11.glRotatef(mirrorRender ? 0 : -90, 0, 0, 1);
			if(!full3D) GL11.glRotatef(mirrorRender ? 0 : 90, 0, 0, 1);

			//Do scaling stuff here
			//Scale Y should be negative for normal item rendering
			GL11.glScalef(scale, scale, scale);
			if(!full3D) GL11.glScalef(1, -1, 1);
			//if(ToolHolster.holsterConfig.getConfig().mirrorRender) GL11.glScalef(-1, 1, 1);
			if(data.getHolsteredItem().getItem() instanceof ItemBow) GL11.glScalef(1,1,-1);

			GL11.glTranslatef(-0.5F * 1, -0.5F * 1, -0.03125F * 1);*/

			float brightness = LightmapHelper.isLightmapEnabled() ? 1.0F : player.getBrightness(partialTick);

			model.renderItemInWorld(Tessellator.instance, player, data.getHolsteredItem(), brightness, 1.0F, false);
			GL11.glPopMatrix();
		}

		ItemStack currentItem = player.getCurrentEquippedItem();

		if((data.getOldItem() == null || currentItem == null || !data.getOldItem().isStackEqual(currentItem))){
			if(isValidTool(data.getOldItem(), player)){
				data.setHolsteredItem(data.getOldItem());
				//data.holsteredItem = data.oldItem;
			}else if(data.getHolsteredItem() != null && currentItem != null && data.getHolsteredItem().isStackEqual(currentItem)){
				data.setHolsteredItem(null);
				//data.holsteredItem = null;
			}
		}

		data.setOldItem(player.getCurrentEquippedItem());
		//data.oldItem = player.getCurrentEquippedItem();
	}

	@Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V", at = @At("TAIL"))
	public void clearData(Player player, float partialTick, CallbackInfo ci){
		data = null;
	}

	@Unique
	private void getBackHolsterTransform(ItemModel model, Player player) {
		boolean hasChestplate = player.inventory.armorItemInSlot(2) != null && !(player.inventory.armorItemInSlot(2).getItem() instanceof ItemQuiver);

		//boolean full3D = model instanceof ItemModelBow || model instanceof ItemModelStandard && ((ItemModelStandardAccessor)model).getBFull3D();
		boolean full3D = THConfig.isFull3D(model.item.id, player.uuid);
		float scale = (full3D ? 0.625F : 0.375F) * 1.5F;
		float torsoOffset = full3D ? 0 : -1/32F;

		boolean mirrorRender = THConfig.instance.mirrorRender;

		GL11.glTranslatef(-0.51F, -0.22F, hasChestplate ? 4/16F + torsoOffset : 3/16F - 0.01F + torsoOffset);

		/*Null is handled in isValidTool*/
		if(data.getHolsteredItem().getItem() instanceof ItemBow) GL11.glTranslatef(0, 0, -2/16F);

		//TODO: attempt to get small items to render centered also

		//Translate to middle of item
		GL11.glTranslatef(0.5F * 1, 0.5F * 1, 0.03125F * 1);
		//Rotate here, origin is the middle of item
		GL11.glRotatef(mirrorRender ? 0 : -90, 0, 0, 1);
		if(!full3D) GL11.glRotatef(mirrorRender ? 0 : 90, 0, 0, 1);

		//Do scaling stuff here
		//Scale Y should be negative for normal item rendering
		GL11.glScalef(scale, scale, scale);
		if(!full3D) GL11.glScalef(1, -1, 1);
		//if(ToolHolster.holsterConfig.getConfig().mirrorRender) GL11.glScalef(-1, 1, 1);
		if(data.getHolsteredItem().getItem() instanceof ItemBow) GL11.glScalef(1,1,-1);

		GL11.glTranslatef(-0.5F * 1, -0.5F * 1, -0.03125F * 1);
	}

	@Unique
	private void getSideHolsterTransform(ItemModel model, Player player) {
		//boolean hasChestplate = player.inventory.armorItemInSlot(2) != null && !(player.inventory.armorItemInSlot(2).getItem() instanceof ItemQuiver);
		boolean hasChestplate = false;

		//boolean full3D = model instanceof ItemModelBow || model instanceof ItemModelStandard && ((ItemModelStandardAccessor)model).getBFull3D();
		boolean full3D = THConfig.isFull3D(model.item.id, player.uuid);
		float scale = (full3D ? 0.625F : 0.375F) * 1.5F;
		float torsoOffset = full3D ? 0 : -1/32F;

		//boolean mirrorRender = THConfig.instance.mirrorRender;

		GL11.glTranslatef(-0.285F, 0.28F, hasChestplate ? 4/16F + torsoOffset : 1/64F - 0.01F + torsoOffset);

		/*Null is handled in isValidTool*/
		if(data.getHolsteredItem().getItem() instanceof ItemBow) GL11.glTranslatef(0, 0, -2/16F);

		//TODO: attempt to get small items to render centered also

		//Translate to middle of item
		GL11.glTranslatef(0.5F * 1, 0.5F * 1, 0.03125F * 1);
		//Rotate here, origin is the middle of item
		GL11.glRotatef(-90, 0, 0, 1);
		GL11.glRotatef(90, 1, 0, 0);
		GL11.glRotatef(-90, 0, 0, 1);  //Make the blade face outwards
		if(!full3D) GL11.glRotatef( 90, 0, 0, 1);

		//Do scaling stuff here
		//Scale Y should be negative for normal item rendering
		GL11.glScalef(scale, scale, scale);
		if(!full3D) GL11.glScalef(1, -1, 1);
		//if(ToolHolster.holsterConfig.getConfig().mirrorRender) GL11.glScalef(-1, 1, 1);
		if(data.getHolsteredItem().getItem() instanceof ItemBow) GL11.glScalef(1,1,-1);

		GL11.glScalef(1, -1, 1); //Make the blade face outwards

		GL11.glTranslatef(-0.5F * 1, -0.5F * 1, -0.03125F * 1);
	}

	@Unique
	private boolean isValidTool(@Nullable ItemStack stack, @NotNull Player player){
		return stack != null && stack.itemID >= 16384 && THConfig.isHolstered(stack.itemID, player.uuid);
		//return stack != null && (stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemToolSword || stack.getItem() instanceof ItemBow);
	}

}




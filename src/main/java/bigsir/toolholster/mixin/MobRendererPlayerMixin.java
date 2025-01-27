package bigsir.toolholster.mixin;

import bigsir.toolholster.data.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.world.WorldClientMP;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemBow;
import net.minecraft.core.item.ItemQuiver;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.item.tool.ItemToolSword;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {
	@Shadow
	private ModelBiped modelBipedMain;

	@Unique
	private static final Map<UUID, PlayerData> modelCache = new HashMap<>();


	public MobRendererPlayerMixin(ModelBase model, float shadowSize) {
		super(model, shadowSize);
	}

	@Inject(
		method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/container/ContainerInventory;getCurrentItem()Lnet/minecraft/core/item/ItemStack;")
	)
	public void renderItem(Player player, float partialTick, CallbackInfo ci){
		if(Minecraft.getMinecraft().currentWorld instanceof WorldClientMP){
			System.out.println(Minecraft.getMinecraft().getSendQueue().playerList.get(player.nickname));
		}

		PlayerData data =  modelCache.get(player.uuid);
		if(data == null) modelCache.put(player.uuid, data = new PlayerData());


		if(isValidTool(data.holsteredItem)){
			GL11.glPushMatrix();

			boolean hasChestplate = player.inventory.armorItemInSlot(2) != null && !(player.inventory.armorItemInSlot(2).getItem() instanceof ItemQuiver);

			this.modelBipedMain.body.translateTo(0.0625F);
			GL11.glScalef(0.625F, 0.625F, 0.625F);
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			GL11.glRotatef(-90, 0, 0, 1);
			GL11.glTranslatef(-0.8F, -0.5F, hasChestplate ? 4/16F : 3/16F);

			if(data.holsteredItem.getItem() instanceof ItemBow){
				GL11.glScalef(1,1,-1);
				GL11.glTranslatef(0, 0, 1/16F);
			}

			float brightness = LightmapHelper.isLightmapEnabled() ? 1.0F : player.getBrightness(partialTick);

			ItemModelDispatcher.getInstance().getDispatch(data.holsteredItem).renderItemInWorld(Tessellator.instance, player, data.holsteredItem, brightness, 1.0F, false);
			GL11.glPopMatrix();
		}

		ItemStack currentItem = player.getCurrentEquippedItem();

		if((data.oldItem == null || currentItem == null || !data.oldItem.isStackEqual(currentItem))){
			if(isValidTool(data.oldItem)){
				data.holsteredItem = data.oldItem;
			}else if(data.holsteredItem != null && currentItem != null && data.holsteredItem.isStackEqual(currentItem)){
				data.holsteredItem = null;
			}
		}

		data.oldItem = player.getCurrentEquippedItem();
	}

	@Unique
	private boolean isValidTool(ItemStack stack){
		//Hardcoded for the time being
		return stack != null && (stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemToolSword || stack.getItem() instanceof ItemBow);
	}

}




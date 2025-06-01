package bigsir.toolholster.mixin;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.client.THClient;
import bigsir.toolholster.core.data.Pointer;
import bigsir.toolholster.interfaces.IPlayer;
import bigsir.toolholster.interfaces.IPointerStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.core.net.packet.PacketPreLogin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandlerClient.class, remap = false)
public abstract class PacketHandlerClientMixin {
	@Shadow
	@Final
	private Minecraft mc;

	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void handlePacket(PacketCustomPayload customPayloadPacket, CallbackInfo ci) {
		THClient.receiveModPresentPacket(customPayloadPacket);
	}

	@Inject(method = "handleHandshake", at = @At("HEAD"))
	public void preConnect(PacketPreLogin preLoginPacket, CallbackInfo ci) {
		THClient.preConnect();
	}

	@Inject(method = "handleSetSlot", at = @At(value = "HEAD"))
	public void fixHolsterDesync(PacketContainerSetSlot containerSetslotPacket, CallbackInfo ci) {
		/*Player player = this.mc.thePlayer;
		ItemStack stack = player.inventorySlots.getSlot(containerSetslotPacket.itemSlot).getItemStack();
		if (stack != null) {
			System.out.println(ToolHolster.<IPointerStorage<?>>cast(stack).getPointer());
			Pointer.clearPtr(ToolHolster.<IPointerStorage<?>>cast(stack).getPointer());
			if (stack == ((IPlayer)player).getData().getHolsteredItem()) {
				System.out.println("asd");
				((IPlayer)player).getData().setHolsteredItem(null);
			}
		}*/
		//((IPlayer)this.mc.thePlayer).getData().setHolsteredItem(containerSetslotPacket.myItemStack);
	}
}

package bigsir.toolholster.mixin;

import bigsir.toolholster.client.THClient;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.net.packet.PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandlerClient.class, remap = false)
public abstract class PacketHandlerClientMixin {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void handlePacket(PacketCustomPayload customPayloadPacket, CallbackInfo ci) {
		THClient.receiveModPresentPacket(customPayloadPacket);
	}
}

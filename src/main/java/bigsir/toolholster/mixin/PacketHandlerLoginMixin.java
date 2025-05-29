package bigsir.toolholster.mixin;

import bigsir.toolholster.server.THServer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.net.packet.PacketLogin;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandlerLogin.class, remap = false)
public abstract class PacketHandlerLoginMixin {

	@Inject(method = "doLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/handler/PacketHandlerServer;sendPacket(Lnet/minecraft/core/net/packet/Packet;)V"))
	public void sendModInstalledPacket(PacketLogin loginPacket, CallbackInfo ci, @Local(name = "player") PlayerServer player) {
		THServer.sendModPresentPacket(player.playerNetServerHandler);
	}
}

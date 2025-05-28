package bigsir.toolholster.mixin;

import net.minecraft.client.render.model.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Cube.class, remap = false)
public interface CubeAccessor {
	@Accessor
	void setCompiled(boolean compiled);
}

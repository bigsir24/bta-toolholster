package bigsir.toolholster.core.data;

import net.minecraft.core.item.Item;

import java.util.Objects;

public final class IDKey {

	private static final IDKey STATIC_INSTANCE = new IDKey(0);
	public int id;

	public IDKey(int id) {
		this.id = id;
	}

	public static IDKey getTemp(int id) {
		return STATIC_INSTANCE.with(id);
	}

	public static IDKey getTemp(Item item) {
		return getTemp(item.id);
	}

	private IDKey with(int id) {
		this.id	= id;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		IDKey idKey = (IDKey) object;
		return id == idKey.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

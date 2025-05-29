package bigsir.toolholster.core.data;

import java.util.Objects;

public class ItemConfig {
	public int itemID;
	public byte flags;

	private static final int FULL_3D_MASK = 0b0000_0010;
	private static final int SIDE_MASK = 0b0000_0100;
	private static final int MIRRORED_MASK = 0b0000_1000;

	public ItemConfig setFull3D(boolean full3D) {
		if (full3D) flags |= FULL_3D_MASK;
		return this;
	}

	public ItemConfig setSide(boolean side) {
		if (side) flags |= SIDE_MASK;
		return this;
	}

	public ItemConfig setMirrored(boolean mirrored) {
		if (mirrored) flags |= MIRRORED_MASK;
		return this;
	}

	public ItemConfig(int itemID, int flags) {
		this.itemID = itemID;
		this.flags = (byte) flags;
	}

	public ItemConfig(int itemID) {
		this.itemID = itemID;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		ItemConfig config = (ItemConfig) object;
		return itemID == config.itemID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemID);
	}
}

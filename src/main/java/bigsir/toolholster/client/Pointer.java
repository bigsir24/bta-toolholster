package bigsir.toolholster.client;

import bigsir.toolholster.ToolHolster;
import bigsir.toolholster.interfaces.IPointerStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Pointer<T> /*T should implement IPointerStorage*/ {
	private T item;

	public T get() {
		return item;
	}

	public void set(T item) {
		this.item = item;

		if(this.item != null) {
			ToolHolster.<IPointerStorage<T>>cast(this.item).setPointer(this);
		}
	}

	public void clear() {
		if (this.item != null) ptrStorage(this.item).clearReference();
		this.item = null;
	}

	public void clearFrom(T from) {
		IPointerStorage<T> fromPtr = ptrStorage(from);
		if (fromPtr.getPointer() == this) {
			fromPtr.clearReference();
			this.item = null;
		}
	}

	public static void delete(@Nullable Object from) {
		if (from == null) return;

		IPointerStorage<?> fromStore = (IPointerStorage<?>) from;

		clearPtr(fromStore.getPointer());
		fromStore.clearReference();
	}

	public static void clearPtr(@Nullable Pointer<?> pointer) {
		if (pointer == null) return;

		pointer.item = null;
	}

	public static <T> void transfer(@Nullable Pointer<T> ptr, @Nullable IPointerStorage<T> from, @Nullable IPointerStorage<T> to) {
		if (ptr == null || from == null || to == null) return;

		from.clearReference();
		to.setPointer(ptr);
		ptr.item = (T) to;
	}

	@SuppressWarnings("unchecked")
	public static <T> void transfer(@Nullable Object from, @Nullable Object to) {
		if (from == null || to == null) {
			//ToolHolster.LOGGER.warn(from + ", " + to);
			return;
		}
		//ToolHolster.LOGGER.warn(from + ", " + to);

		IPointerStorage<T> fromStore = (IPointerStorage<T>) from;
		IPointerStorage<T> toStore = (IPointerStorage<T>) to;

		Pointer<T> ptr = fromStore.getPointer();
		fromStore.clearReference();
		toStore.setPointer(ptr);
		if (ptr != null) ptr.item = (T) to;
	}

	private IPointerStorage<T> ptrStorage(T o) {
		return (IPointerStorage<T>) o;
	}
}

package bigsir.toolholster.interfaces;

import bigsir.toolholster.core.data.Pointer;

public interface IPointerStorage<T> {
	Pointer<T> getPointer();
	void setPointer(Pointer<T> ptr);
	void clearReference();
}

package bigsir.toolholster.interfaces;

import bigsir.toolholster.client.PlayerData;
import bigsir.toolholster.client.Pointer;

public interface IPointerStorage<T> {
	Pointer<T> getPointer();
	void setPointer(Pointer<T> ptr);
	void clearReference();
}

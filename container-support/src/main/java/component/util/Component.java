package component.util;

import static util.Verify.argNotNull;

import provider.Commissioner;
import provider.Decommissioner;

class Component<T> {
	final Class<T> interfaceClass;
	final T instance;
	final Commissioner commissioner;
	final Decommissioner decommissioner;
	
	public Component(Class<T> interfaceClass, T instance, Commissioner commissioner,
			Decommissioner decommissioner) {
		this.interfaceClass = argNotNull(interfaceClass);
		this.instance = argNotNull(instance);
		this.commissioner = argNotNull(commissioner);
		this.decommissioner = argNotNull(decommissioner);
	}
}

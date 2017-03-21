package component.util;

import static util.Verify.argNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import component.Components;
import provider.Commissioner;
import provider.Decommissioner;
import provider.Provider;

class ComponentsImpl implements Components {

	private final Map<Class<?>, Component<?>> components = new HashMap<>();
	private final List<Component<?>> initialised = new ArrayList<>();

	@Override
	public <T> T get(Class<T> type) {
		@SuppressWarnings("unchecked")
		Component<T> component = (Component<T>) components.get(type);
		if (component == null) {
			throw new IllegalStateException("no instance found for " + type.getClass().getName());
		}
		return component.instance;
	}

	@Override
	public void commission() throws Exception {
		for (Component<?> component : components.values()) {
			component.commissioner.commission();
			initialised.add(component);
		}
	}

	@Override
	public void decommission() {
		for (Component<?> component : initialised) {
			component.decommissioner.decommission();
		}
	}

	<T> void add(Class<T> type, Provider<T> provider) {
		argNotNull(type);
		T instance = provider.get();
		components.put(type,
				new Component<T>(instance, provider.getCommissioner(instance), provider.getDecommissioner(instance)));
	}

	boolean contains(Class<?> type) {
		return components.containsKey(type);
	}

	private static class Component<T> {
		final T instance;
		final Commissioner commissioner;
		final Decommissioner decommissioner;

		Component(T instance, Commissioner commissioner, Decommissioner decommissioner) {
			this.instance = argNotNull(instance);
			this.commissioner = argNotNull(commissioner);
			this.decommissioner = argNotNull(decommissioner);
		}
	}
}

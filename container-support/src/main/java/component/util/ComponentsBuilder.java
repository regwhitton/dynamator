package component.util;

import static util.Verify.argNotNull;

import component.Components;
import provider.Provider;

public class ComponentsBuilder {

	private final ComponentsImpl components = new ComponentsImpl();

	private ComponentsBuilder() {
	}

	public static ComponentsBuilder create() {
		return new ComponentsBuilder();
	}

	public <T> T get(Class<T> type) {
		return components.get(type);
	}

	public <T> ComponentsBuilder add(Class<T> type, Provider<T> provider) {
		argNotNull(type);
		argNotNull(provider);
		
		validateIsAnInterface(type);
		validateNotAlreadyProvidedFor(type);
		
		components.add(type, provider);
		return this;
	}

	private void validateIsAnInterface(Class<?> type) {
		if (!type.isInterface()) {
			throw new IllegalArgumentException("expected interface but got " + type.getName());
		}
	}

	private void validateNotAlreadyProvidedFor(Class<?> type) {
		if (components.contains(type)) {
			throw new IllegalStateException("instance already provided for type " + type.getClass().getName());
		}
	}

	public Components build() {
		return components;
	}
}

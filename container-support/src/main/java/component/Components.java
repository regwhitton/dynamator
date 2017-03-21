package component;

import provider.Commissioner;
import provider.Decommissioner;

/**
 * Holds references to the components of an application. In Spring Framework
 * terms this is the application context.
 * 
 * @see component.util.ComponentsBuilder
 */
public interface Components {

	/**
	 * @returns the component instance that implements the given interface.
	 */
	<T> T get(Class<T> type);

	/**
	 * Applies the {@link Commissioner} provided by each {@link Provider}. If
	 * any throws an exception then that will be thrown out here and further
	 * dependencies are ignored.
	 * <p/>
	 * This is only expected to be called once during the lifetime of an
	 * application.
	 */
	void commission() throws Exception;

	/**
	 * Applies the {@link Decommissioner} to all components that previously had
	 * a {@link Commissioner()} successfully applied.
	 * <p/>
	 * This is only expected to be called once during the lifetime of an
	 * application.
	 */
	void decommission();
}
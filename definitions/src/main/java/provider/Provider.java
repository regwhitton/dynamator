package provider;

/**
 * The provider of an application component.
 * <p>
 * In this pattern the interface that a component provides to the other
 * application components is separated from the component's construction
 * requirements. These are moved to the provider.
 * </p>
 * <p>
 * Dependencies should be passed to the provider's constructor, or (if that is
 * not possible) via setters <i>on the provider</i>.
 * </p>
 * <p>
 * The surrounding application is required to signal to the component when it is
 * starting up or shutting down using the {@link Commissioner} and
 * {@link Decommissioner}.
 * </p>
 * The provider may return an instance which could be any one of:
 * <ul>
 * <li>a simple implementation within the same package,</li>
 * <li>a transport layer to a remote implementation in another JVM,</li>
 * <li>an implementation of <a href=
 * "https://www.martinfowler.com/bliki/BranchByAbstraction.html">BranchByAbstraction</a>
 * allowing a switch between implementators.</li>
 * <li>a {@link java.util.ServiceLoader} to find a implementation on the
 * classpath.</a>
 * </ul>
 * 
 * @param <T>
 *            The type of the component to be created. This will be an interface
 *            class.
 */
public interface Provider<T> {

	/**
	 * @return the component
	 */
	public T get();

	/**
	 * @returns the {@link Commissioner} to be used to commission the component.
	 */
	public Commissioner getCommissioner(T component);

	/**
	 * @returns the {@link Decommissioner} to be used to decommission the
	 *          component.
	 */
	public Decommissioner getDecommissioner(T component);
}

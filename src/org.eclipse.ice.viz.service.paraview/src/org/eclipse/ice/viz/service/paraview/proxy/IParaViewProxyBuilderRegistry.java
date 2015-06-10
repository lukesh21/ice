/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.Set;

/**
 * Implementations of this interface provide a registry that maps supported
 * extensions for files to {@link IParaViewProxyBuilder} instances. Client code
 * can request a builder by calling {@link #getProxyBuilder(URI)}.
 * <p>
 * This interface is designed to be provided and referenced via OSGi.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public interface IParaViewProxyBuilderRegistry {

	/**
	 * Registers a new proxy builder using the builder's supported extensions.
	 * <p>
	 * This method will usually be called by OSGi.
	 * </p>
	 * 
	 * @param builder
	 *            The builder to register. If {@code null}, nothing is
	 *            registered.
	 * @return True if the provided builder was registered, false otherwise
	 *         (including the case where the builder has no supported
	 *         extensions).
	 */
	public boolean registerProxyBuilder(IParaViewProxyBuilder builder);

	/**
	 * Unregisters the specified proxy builder. Its extensions should no longer
	 * be supported if it is the only builder for said extensions.
	 * <p>
	 * This method will usually be called by OSGi.
	 * </p>
	 * 
	 * @param builder
	 *            The builder to unregister. If {@code null}, nothing is
	 *            unregistered.
	 * @return True if the provided builder was unregistered, false otherwise
	 *         (including the case where the builder has no supported
	 *         extensions).
	 */
	public boolean unregisterProxyBuilder(IParaViewProxyBuilder builder);

	/**
	 * Gets a builder for the provided file based on its extension.
	 * 
	 * @param uri
	 *            The file for which a proxy will be created. If {@code null}, a
	 *            builder will not be returned.
	 * @return A builder capable of creating a proxy for the file, or
	 *         {@code null} if a builder could not be created for the file.
	 */
	public IParaViewProxyBuilder getProxyBuilder(URI uri);

	/**
	 * Gets the set of supported extensions for all registered proxy builders.
	 * Note that duplicate extensions are not to be listed. The extensions
	 * should not include the leading period.
	 * 
	 * @return The set of supported extensions for all proxy builders. This
	 *         should never be {@code null}, and should not change throughout
	 *         the registry's lifecycle, as builders will be registered via
	 *         OSGi.
	 */
	public Set<String> getExtensions();
}
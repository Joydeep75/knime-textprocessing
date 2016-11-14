/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   24.08.2016 (andisadewi): created
 */
package org.knime.ext.textprocessing.nodes.transformation.documentvectorhashing;

import java.util.Hashtable;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.knime.core.node.NodeLogger;

/**
 * All implementations of
 * {@link org.knime.ext.textprocessing.nodes.transformation.documentvectorhashing.HashingFunction}s have to be
 * registered in this factory to be able to create a hash function instance (i.e. murmur3_32bitHashingFunction). Access
 * the singleton instance of the factory by calling
 * {@link org.knime.ext.textprocessing.nodes.transformation.documentvectorhashing.HashingFunctionFactory#getInstance()}.
 *
 * @author Andisa Dewi, KNIME.com, Berlin, Germany
 */
final class HashingFunctionFactory {
    private static final NodeLogger LOGGER = NodeLogger.getLogger(HashingFunctionFactory.class);

    /**
     * The id of the aggregation method extension point.
     */
    public static final String EXT_POINT_ID = "org.knime.ext.textprocessing.HashingFunction";

    /**
     * The attribute of the aggregation method extension point.
     */
    public static final String EXT_POINT_ATTR_DF = "HashingFunction";

    private static HashingFunctionFactory instance;

    private Hashtable<String, HashingFunction> m_hashingFunctions = new Hashtable<String, HashingFunction>();

    private HashingFunctionFactory() {
        registerExtensionPoints();
    }

    /**
     * @return The singleton instance of <code>HashingFunctionFactory</code>.
     */
    public static HashingFunctionFactory getInstance() {
        if (instance == null) {
            instance = new HashingFunctionFactory();
        }
        return instance;
    }

    /**
     * @return The names of the registered hash functions.
     */
    public Set<String> getHashNames() {
        return m_hashingFunctions.keySet();
    }

    /**
     * Returns an instance of the hash function with the given name
     *
     * @param name the name of the hashing function
     * @return the hashing function with the given name
     */
    public HashingFunction getHashFunction(final String name) {
        HashingFunction hash = m_hashingFunctions.get(name);

        HashingFunction hashInstance = null;

        try {
            hashInstance = hash.getClass().newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("New HashingFunction instance " + hash.getClass().toString() + " could not be created!");
            LOGGER.error(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error("Empty Consructor of " + hash.getClass().toString() + " is not accessible.");
            LOGGER.error(e.getMessage());
        }

        return hashInstance;
    }

    /**
     * Registers all extension point implementations
     */
    private void registerExtensionPoints() {
        try {
            final IExtensionRegistry registry = Platform.getExtensionRegistry();
            final IExtensionPoint point = registry.getExtensionPoint(EXT_POINT_ID);
            if (point == null) {
                LOGGER.error("Invalid extension point: " + EXT_POINT_ID);
                throw new IllegalStateException("ACTIVATION ERROR: " + " --> Invalid extension point: " + EXT_POINT_ID);
            }
            for (final IConfigurationElement elem : point.getConfigurationElements()) {
                final String operator = elem.getAttribute(EXT_POINT_ATTR_DF);
                final String decl = elem.getDeclaringExtension().getUniqueIdentifier();

                if (operator == null || operator.isEmpty()) {
                    LOGGER.error("The extension '" + decl + "' doesn't provide the required attribute '"
                        + EXT_POINT_ATTR_DF + "'");
                    LOGGER.error("Extension " + decl + " ignored.");
                    continue;
                }

                try {
                    final HashingFunction func = (HashingFunction)elem.createExecutableExtension(EXT_POINT_ATTR_DF);
                    if (func != null) {
                        if (m_hashingFunctions.containsKey(func.getName())) {
                            LOGGER.error("Hashing function with name \"" + func.getName() + "\" already exists!");
                        } else {
                            m_hashingFunctions.put(func.getName(), func);
                            LOGGER.info("Added hash function: " + func.getClass().toString());
                        }
                    }
                } catch (final Throwable t) {
                    LOGGER.error("Problems during initialization of " + operator + "'.)");
                    if (decl != null) {
                        LOGGER.error("Extension " + decl + " ignored.", t);
                    }
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Exception while registering hashing function extensions");
        }
    }

}
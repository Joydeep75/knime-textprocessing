/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 * -------------------------------------------------------------------
 *
 * History
 *   28.09.2009 (thiel): created
 */
package org.knime.ext.textprocessing.nodes.tagging.opennlpner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.NodeLogger;
import org.knime.ext.textprocessing.data.NamedEntityTag;
import org.knime.ext.textprocessing.util.OpenNlpModelPaths;

/**
 * Factory class for built-in OpenNLP NER models.
 *
 * @author Kilian Thiel, University of Konstanz
 *
 */
public final class OpenNlpModelFactory {

    /** NodeLogger for this class. */
    private static final NodeLogger LOGGER = NodeLogger.getLogger(OpenNlpModelFactory.class);

    /** Static factory instance. */
    private static OpenNlpModelFactory instance = null;

    /** Map of model names and their associated {@code OpenNlpModels}. */
    private HashMap<String, OpenNlpModel> m_models = new HashMap<>();

    /** Map of model names and their named-entity tag value. */
    private Map<String, String> m_modelTypeTagMapping = new HashMap<>();

    /** Map of model names and their associated file paths. */
    private Map<String, String> m_modelTypeFileMapping = new HashMap<>();

    /**
     * Creates and returns a singleton instance of {@code OpenNlpModelFactory}.
     *
     * @return a singleton instance of {@code OpenNlpModelFactory}.
     */
    public static final OpenNlpModelFactory getInstance() {
        if (instance == null) {
            instance = new OpenNlpModelFactory();
        }
        return instance;
    }

    /** Creates a new instance of {@code OpenNlpModelFactory}. */
    private OpenNlpModelFactory() {
        LOGGER.debug("Registering Maxent Models ...");

        OpenNlpModelPaths paths = OpenNlpModelPaths.getOpenNlpModelPaths();

        String name = "Person";
        m_modelTypeTagMapping.put(name, NamedEntityTag.PERSON.toString());
        m_modelTypeFileMapping.put(name, paths.getPersonNERModelFile());

        name = "Location";
        m_modelTypeTagMapping.put(name, NamedEntityTag.LOCATION.toString());
        m_modelTypeFileMapping.put(name, paths.getLocationNERModelFile());

        name = "Organization";
        m_modelTypeTagMapping.put(name, NamedEntityTag.ORGANIZATION.toString());
        m_modelTypeFileMapping.put(name, paths.getOrganizationNERModelFile());

        name = "Money";
        m_modelTypeTagMapping.put(name, NamedEntityTag.MONEY.toString());
        m_modelTypeFileMapping.put(name, paths.getMoneyNERModelFile());

        name = "Date";
        m_modelTypeTagMapping.put(name, NamedEntityTag.DATE.toString());
        m_modelTypeFileMapping.put(name, paths.getDateNERModelFile());

        name = "Time";
        m_modelTypeTagMapping.put(name, NamedEntityTag.TIME.toString());
        m_modelTypeFileMapping.put(name, paths.getTimeNERModelFile());

        for (String type : m_modelTypeTagMapping.keySet()) {
            OpenNlpModel m = new OpenNlpModel(type, m_modelTypeFileMapping.get(type), m_modelTypeTagMapping.get(type));
            m_models.put(type, m);
        }
    }

    /**
     * Returns the named entity tag by name.
     *
     * @param name the name to get the tag for.
     * @return the tag corresponding to the given name.
     * @since 2.7
     */
    public String getTagByName(final String name) {
        return m_modelTypeTagMapping.get(name);
    }

    /**
     * Returns the model related to the given name.
     *
     * @param name The name to get the model for.
     * @return The related model.
     */
    public OpenNlpModel getModelByName(final String name) {
        return m_models.get(name);
    }

    /**
     * @return A set of all names of available models.
     */
    public Set<String> getModelNames() {
        return m_models.keySet();
    }

    /**
     * @return The name of the default model.
     */
    public String getDefaultName() {
        return "Person";
    }
}

/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   Apr 18, 2018 (Julian Bunzel): created
 */
package org.knime.ext.textprocessing.nodes.tagging.dict.multicolumn;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.ext.textprocessing.nodes.tagging.DocumentTaggerSettings;

/**
 * The {@code DictionaryTaggerSettings} extends the functionality of the {@link DocumentTaggerSettings} by
 * adding a set of entities (the dictionary) which is used to tag documents.
 *
 * @author Julian Bunzel, KNIME GmbH, Berlin, Germany
 * @since 3.6
 */
final class DictionaryTaggerSettings extends DocumentTaggerSettings {

    /**
     * Node logger for this class.
     */
    private static final NodeLogger LOGGER = NodeLogger.getLogger(DictionaryTaggerSettings.class);

    /**
     * The configuration key of the column name setting.
     */
    private static final String CFGKEY_COLUMNNAME = "column-name";

    /**
     * The name of the column containing the dictionary.
     */
    private String m_columnName;

    /**
     * Creates an instance of {@code DictionaryTaggerSettings} based on a {@link DocumentTaggerSettings} and a
     * column name.
     *
     * @param colName The name of the the column containing the named entities to tag.
     * @param settings The {@code DocumentTaggerSettings} to create the {@code DictionaryTaggerSettings}.
     */
    private DictionaryTaggerSettings(final String colName, final DocumentTaggerSettings settings) {
        this(colName, settings.getCaseSensitivityOption(), settings.getExactMatchOption(), settings.getTagType(),
            settings.getTagValue());
    }

    /**
     * Creates an instance of {@code DictionaryTaggerSettings} based on a column name, values for the case
     * sensitivity and exact match flag, the tag type and tag value.
     *
     * @param colName colName The name of the the column containing the named entities to tag.
     * @param caseSensitivity Set {@code true} for case sensitive matching.
     * @param exactMatch Set {@code true} for exact matching.
     * @param tagType The tag type to set.
     * @param tagValue The tag value to set.
     */
    DictionaryTaggerSettings(final String colName, final boolean caseSensitivity, final boolean exactMatch,
        final String tagType, final String tagValue) {
        super(caseSensitivity, exactMatch, tagType, tagValue);

        setColumnName(colName);
    }

    /**
     * Creates an instance of {@code DictionaryTaggerSettings} based on another
     * {@code DictionaryTaggerSettings.}
     *
     * @param settings The {@code DictionaryTaggerSettings} to create a new instance from.
     */
    DictionaryTaggerSettings(final DictionaryTaggerSettings settings) {
        super(settings.getCaseSensitivityOption(), settings.getExactMatchOption(), settings.getTagType(),
            settings.getTagValue());
        setColumnName(settings.getColumnName());
    }

    /**
     * Creates an instance of {@code DictionaryTaggerSettings} based on a column name. The tagger parameters will
     * be set to their default values.
     *
     * @param colName colName The name of the the column containing the named entities to tag.
     */
    DictionaryTaggerSettings(final String colName) {
        this(colName, new DocumentTaggerSettings());
    }

    /**
     * Returns the column name containing the dictionaries.
     *
     * @return Returns the column name.
     */
    final String getColumnName() {
        return m_columnName;
    }

    /**
     * Sets the column name containing the dictionary.
     *
     * @param columnName The column name to set.
     */
    final void setColumnName(final String columnName) {
        m_columnName = columnName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettingsTo(final NodeSettingsWO settings) {
        super.saveSettingsTo(settings);
        settings.addString(CFGKEY_COLUMNNAME, m_columnName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettingsFrom(final NodeSettingsRO settings) {
        String columnName = "";
        try {
            columnName = settings.getString(CFGKEY_COLUMNNAME);
        } catch (InvalidSettingsException ise) {
            // this method is called from the dialog which inits "this" first
            // and immediately calls this method, name should (must) match
            LOGGER.warn("Can't safely update settings for column \"" + m_columnName + "\": No matching identifier.",
                ise);
            columnName = m_columnName;
        }

        if (!m_columnName.equals(columnName)) {
            LOGGER.warn("Can't update settings for column \"" + m_columnName + "\": got NodeSettings for \""
                + columnName + "\"");
        }
        setColumnName(columnName);
        super.loadSettingsFrom(settings);
    }

    /**
     * Static method to create a {@code DictionaryTaggerSettings} from an instance of {@link NodeSettingsRO}.
     *
     * @param settings The instance of {@code NodeSettingsRO} to create the {@code DictionaryTaggerSettings}
     *            instance from.
     * @throws InvalidSettingsException If settings could not be retrieved.
     * @return Returns an instance of {@code DictionaryTaggerSettings}.
     */
    public static final DictionaryTaggerSettings createFrom(final NodeSettingsRO settings)
        throws InvalidSettingsException {
        return new DictionaryTaggerSettings(settings.getString(CFGKEY_COLUMNNAME),
            DocumentTaggerSettings.createFrom(settings));
    }
}

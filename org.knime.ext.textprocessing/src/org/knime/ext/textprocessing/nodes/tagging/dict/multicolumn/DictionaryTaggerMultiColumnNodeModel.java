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
 * ---------------------------------------------------------------------
 *
 * History
 *   30.04.2008 (thiel): created
 */
package org.knime.ext.textprocessing.nodes.tagging.dict.multicolumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.streamable.InputPortRole;
import org.knime.ext.textprocessing.data.Tag;
import org.knime.ext.textprocessing.nodes.tagging.DocumentTagger;
import org.knime.ext.textprocessing.nodes.tagging.StreamableTaggerNodeModel2;
import org.knime.ext.textprocessing.nodes.tagging.dict.AbstractDictionaryTaggerModel2;
import org.knime.ext.textprocessing.util.DataTableSpecVerifier;

/**
 * The node model of the Dictionary Tagger node. Extends {@link AbstractDictionaryTaggerModel2} and provides methods to
 * create the specific document tagger.
 *
 * @author Kilian Thiel, KNIME.com GmbH, Berlin, Germany
 * @since 3.5
 */
class DictionaryTaggerMultiColumnNodeModel extends StreamableTaggerNodeModel2 {

    /**
     * Config identifier for the NodeSettings object contained in the NodeSettings which contains the settings.
     */
    public static final String CFG_SUB_CONFIG = "all_columns";

    /**
     * Default dictionary table index.
     */
    public static final int DICT_TABLE_INDEX = 1;

    /**
     * Default document table index.
     */
    public static final int DATA_TABLE_INDEX = 0;

    /** contains settings for each individual column. */
    private DictionaryTaggerConfiguration m_config;

    private final List<Set<String>> m_dictionaries;

    /**
     * Creates a new instance of {@code DictionaryTaggerNodeModel2} with two table in ports and one out port.
     */
    DictionaryTaggerMultiColumnNodeModel() {
        super(2, new InputPortRole[]{InputPortRole.NONDISTRIBUTED_NONSTREAMABLE});
        m_dictionaries = new ArrayList<Set<String>>();
    }

    /**
     * Checks if spec of second input data table contains a string column that can be used as dictionary.
     *
     * @param inSpecs The specs of the input data tables.
     * @throws InvalidSettingsException If settings or specs of input data tables are invalid.
     */
    @Override
    protected final void checkInputDataTableSpecs(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        DataTableSpecVerifier verfier = new DataTableSpecVerifier(inSpecs[DICT_TABLE_INDEX]);
        verfier.verifyMinimumStringCells(1, true);

        // TODO: Verify all columns
        //set and verify column selection and set warning message if present
        //ColumnSelectionVerifier.verifyColumn(m_columnModel, inSpecs[DICT_TABLE_INDEX], StringValue.class, null)
        //   .ifPresent(a -> setWarningMessage(a));
    }

    /**
     * {@inheritDoc}
     */
    // TODO: Unmodifiable & Tokenizer.
    @Override
    public DocumentTagger createTagger() throws Exception {
        DictionaryTaggerColumnSetting[] collectionOfSettings =
            m_config.getDictionaryTaggerColumnSettings().toArray(new DictionaryTaggerColumnSetting[0]);
        DocumentTagger[] tagger = new DocumentTagger[collectionOfSettings.length];
        for (int i = 0; i < collectionOfSettings.length; i++) {
            DictionaryTaggerColumnSetting settings = collectionOfSettings[i];
            tagger[i] = new MultipleDictionaryDocumentTagger(false, m_dictionaries.get(i),
                new Tag(settings.tagValue(), settings.tagType()), settings.caseSensitive(), settings.exactMatch(), "");
        }
        return tagger[0];
    }

    /**
     * Reads strings of string column of second input data table to build dictionary.
     *
     * @param inData Input data tables.
     * @param exec The execution context of the node.
     * @throws Exception If tagger cannot be prepared.
     */
    @Override
    protected final void prepareTagger(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
        // Read table with dictionaries
        m_dictionaries.clear();
        Collection<DictionaryTaggerColumnSetting> collectionOfSettings = m_config.getDictionaryTaggerColumnSettings();
        for (DictionaryTaggerColumnSetting settings : collectionOfSettings) {
            final int dictIndex = inData[DICT_TABLE_INDEX].getDataTableSpec().findColumnIndex(settings.getColumnName());
            if (dictIndex >= 0) {
                Set<String> dictionary = new HashSet<String>();
                for (DataRow row : inData[DICT_TABLE_INDEX]) {
                    if (!row.getCell(dictIndex).isMissing()) {
                        dictionary.add(((StringValue)row.getCell(dictIndex)).getStringValue());
                    }
                }
                m_dictionaries.add(dictionary);
            } else {
                throw new InvalidSettingsException(
                    "Could not find dictionary column '" + settings.getColumnName() + "' in input table.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // Nothing to do ...
    }
}

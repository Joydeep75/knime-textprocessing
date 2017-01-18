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
 *   17.01.2017 (Julian): created
 */
package org.knime.ext.textprocessing.nodes.transformation.documentdatainserter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.ext.textprocessing.data.DocumentType;
import org.knime.ext.textprocessing.data.DocumentValue;

/**
 *
 * @author Julian Bunzel, KNIME.com Berlin
 */
public class DocumentDataInserterNodeDialog extends DefaultNodeSettingsPane {

    static final SettingsModelString getDocumentColumnModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_DOCCOL, "");
    }

    static final SettingsModelString getAuthorsColumnModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_AUTHORSCOL, "");
    }

    static final SettingsModelString getCategoryColumnModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_CATCOLUMN, "");
    }

    static final SettingsModelString getSourceColumnModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_SOURCECOLUMN, "");
    }

    static final SettingsModelString getPubDateColumnModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_PUBDATECOL, "");
    }

    static final SettingsModelBoolean getUseAuthorsColumnModel() {
        return new SettingsModelBoolean(DocumentDataInserterConfigKeys.CFGKEY_USE_AUTHORSCOLUMN,
            DocumentDataInserterConfig.DEF_USE_AUTHORSCOLUMN);
    }

    static final SettingsModelBoolean getUseCategoryColumnModel() {
        return new SettingsModelBoolean(DocumentDataInserterConfigKeys.CFGKEY_USE_CATCOLUMN,
            DocumentDataInserterConfig.DEF_USE_CATCOLUMN);
    }

    static final SettingsModelBoolean getUseSourceColumnModel() {
        return new SettingsModelBoolean(DocumentDataInserterConfigKeys.CFGKEY_USE_SOURCECOLUMN,
            DocumentDataInserterConfig.DEF_USE_SOURCECOLUMN);
    }

    static final SettingsModelBoolean getUsePubDateColumnModel() {
        return new SettingsModelBoolean(DocumentDataInserterConfigKeys.CFGKEY_USE_PUBDATECOLUMN,
            DocumentDataInserterConfig.DEF_USE_PUBDATECOLUMN);
    }

    static final SettingsModelString getSourceModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_DOCSOURCE,
            DocumentDataInserterConfig.DEF_DOCUMENT_SOURCE);
    }

    static final SettingsModelString getCategoryModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_DOCCAT,
            DocumentDataInserterConfig.DEF_DOCUMENT_CATEGORY);
    }

    static final SettingsModelString getAuthorsFirstNameModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_AUTHOR_FIRST_NAME,
            DocumentDataInserterConfig.DEF_AUTHOR_FIRST_NAME);
    }

    static final SettingsModelString getAuthorsLastNameModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_AUTHOR_LAST_NAME,
            DocumentDataInserterConfig.DEF_AUTHOR_LAST_NAME);
    }

    static final SettingsModelString getAuthorsSplitStringModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_AUTHORSPLIT_STR,
            DocumentDataInserterConfig.DEF_AUTHORSSPLIT_STR);
    }

    static final SettingsModelString getTypeModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_DOCTYPE,
            DocumentDataInserterConfig.DEF_DOCUMENT_TYPE);
    }

    static final SettingsModelString getPubDateModel() {
        return new SettingsModelString(DocumentDataInserterConfigKeys.CFGKEY_PUBDATE,
            DocumentDataInserterConfig.DEF_DOCUMENT_PUBDATE);
    }

    static final SettingsModelIntegerBounded getNumberOfThreadsModel() {
        return new SettingsModelIntegerBounded(DocumentDataInserterConfigKeys.CFGKEY_THREADS,
            DocumentDataInserterConfig.DEF_THREADS, DocumentDataInserterConfig.MIN_THREADS,
            DocumentDataInserterConfig.MAX_THREADS);
    }

    static final SettingsModelBoolean getReplaceDocColumnModel() {
        return new SettingsModelBoolean(DocumentDataInserterConfigKeys.CFGKEY_REPLACE_DOCCOL,
            DocumentDataInserterConfig.DEF_REPLACE_DOCCOL);
    }

    SettingsModelBoolean m_useAuthorsColumnModel = getUseAuthorsColumnModel();

    SettingsModelString m_authorsColumnModel = getAuthorsColumnModel();

    SettingsModelString m_sourceModel = getSourceModel();

    SettingsModelString m_sourceColumnModel = getSourceColumnModel();

    SettingsModelBoolean m_useSourceColumnModel = getUseSourceColumnModel();

    SettingsModelString m_categoryModel = getCategoryModel();

    SettingsModelString m_categoryColumnModel = getCategoryColumnModel();

    SettingsModelBoolean m_useCategoryColumnModel = getUseCategoryColumnModel();

    SettingsModelString m_pubDateModel = getPubDateModel();

    SettingsModelBoolean m_usePubDateColumnModel = getUsePubDateColumnModel();

    SettingsModelString m_pubDateColumnModel = getPubDateColumnModel();

    SettingsModelString m_authorsFirstNameModel = getAuthorsFirstNameModel();

    SettingsModelString m_authorsLastNameModel = getAuthorsLastNameModel();

    SettingsModelString m_authorsSplitStrModel = getAuthorsSplitStringModel();

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public DocumentDataInserterNodeDialog() {
        createNewGroup("Text");
        addDialogComponent(new DialogComponentColumnNameSelection(getDocumentColumnModel(), "Document column", 0,
            DocumentValue.class));
        setHorizontalPlacement(true);
        m_useAuthorsColumnModel.addChangeListener(new ChangeStateListener());
        addDialogComponent(new DialogComponentBoolean(m_useAuthorsColumnModel, "Use authors from column"));
        addDialogComponent(
            new DialogComponentColumnNameSelection(m_authorsColumnModel, "Authors column", 0, StringValue.class));

        setHorizontalPlacement(false);
        setHorizontalPlacement(true);
        addDialogComponent(new DialogComponentString(m_authorsSplitStrModel, "Author names separator"));
        setHorizontalPlacement(false);
        setHorizontalPlacement(true);
        addDialogComponent(new DialogComponentString(m_authorsFirstNameModel, "Default author first name"));
        addDialogComponent(new DialogComponentString(m_authorsLastNameModel, "Default author last name"));
        setHorizontalPlacement(false);
        closeCurrentGroup();

        createNewGroup("Source and Category");
        addDialogComponent(new DialogComponentString(m_sourceModel, "Document source"));
        setHorizontalPlacement(false);
        setHorizontalPlacement(true);
        m_useSourceColumnModel.addChangeListener(new ChangeStateListener());
        addDialogComponent(new DialogComponentBoolean(m_useSourceColumnModel, "Use sources from column"));
        addDialogComponent(new DialogComponentColumnNameSelection(m_sourceColumnModel, "Document source column", 0,
            StringValue.class));
        setHorizontalPlacement(false);

        addDialogComponent(new DialogComponentString(m_categoryModel, "Document category"));
        setHorizontalPlacement(true);
        m_useCategoryColumnModel.addChangeListener(new ChangeStateListener());
        addDialogComponent(new DialogComponentBoolean(m_useCategoryColumnModel, "Use categories from column"));
        addDialogComponent(new DialogComponentColumnNameSelection(m_categoryColumnModel, "Document category column", 0,
            StringValue.class));
        setHorizontalPlacement(false);
        closeCurrentGroup();

        createNewGroup("Type and Date");
        String[] types = DocumentType.asStringList().toArray(new String[0]);
        addDialogComponent(new DialogComponentStringSelection(getTypeModel(), "Document type", types));
        DialogComponentString dcs = new DialogComponentString(m_pubDateModel, "Publication date (dd-mm-yyyy)");
        dcs.setToolTipText("Date has to be specified like \"dd-mm-yyyy!\"");
        addDialogComponent(dcs);
        // Pub Date
        setHorizontalPlacement(true);
        m_usePubDateColumnModel.addChangeListener(new ChangeStateListener());
        addDialogComponent(new DialogComponentBoolean(m_usePubDateColumnModel, "Use publication date from column"));
        addDialogComponent(new DialogComponentColumnNameSelection(m_pubDateColumnModel, "Publication date column", 0,
            StringValue.class));
        closeCurrentGroup();

        createNewGroup("Column Settings");
        addDialogComponent(new DialogComponentBoolean(getReplaceDocColumnModel(), "Replace document column"));
        closeCurrentGroup();

        createNewGroup("Processes");
        addDialogComponent(
            new DialogComponentNumber(getNumberOfThreadsModel(), "Number of maximal parallel processes", 1));
        closeCurrentGroup();
        checkState();

    }

    private void checkState() {
        m_categoryColumnModel.setEnabled(m_useCategoryColumnModel.getBooleanValue());
        m_categoryModel.setEnabled(!m_useCategoryColumnModel.getBooleanValue());
        m_pubDateColumnModel.setEnabled(m_usePubDateColumnModel.getBooleanValue());
        m_pubDateModel.setEnabled(!m_usePubDateColumnModel.getBooleanValue());
        m_sourceColumnModel.setEnabled(m_useSourceColumnModel.getBooleanValue());
        m_sourceModel.setEnabled(!m_useSourceColumnModel.getBooleanValue());
        m_authorsColumnModel.setEnabled(m_useAuthorsColumnModel.getBooleanValue());
        m_authorsSplitStrModel.setEnabled(m_useAuthorsColumnModel.getBooleanValue());
        m_authorsFirstNameModel.setEnabled(!m_useAuthorsColumnModel.getBooleanValue());
        m_authorsLastNameModel.setEnabled(!m_useAuthorsColumnModel.getBooleanValue());
    }

    class ChangeStateListener implements ChangeListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void stateChanged(final ChangeEvent e) {
            checkState();
        }
    }
}

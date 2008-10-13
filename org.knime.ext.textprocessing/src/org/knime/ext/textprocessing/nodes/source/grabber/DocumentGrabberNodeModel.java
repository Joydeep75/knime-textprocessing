/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2007
 * University of Konstanz, Germany
 * Chair for Bioinformatics and Information Mining (Prof. M. Berthold)
 * and KNIME GmbH, Konstanz, Germany
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.org
 * email: contact@knime.org
 * ---------------------------------------------------------------------
 * 
 * History
 *   13.10.2008 (thiel): created
 */
package org.knime.ext.textprocessing.nodes.source.grabber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.ext.textprocessing.data.Document;
import org.knime.ext.textprocessing.data.DocumentCategory;
import org.knime.ext.textprocessing.util.DataTableBuilderFactory;
import org.knime.ext.textprocessing.util.DocumentDataTableBuilder;

/**
 * 
 * @author Kilian Thiel, University of Konstanz
 */
public class DocumentGrabberNodeModel extends NodeModel {

    /**
     * Default database to query.
     */
    public static final String DEFAULT_DATABASE = "PUBMED";
    
    /**
     * The maximum number of results.
     */
    public static final int MAX_RESULTS = Integer.MAX_VALUE;
    
    /**
     * The minimum number of results.
     */
    public static final int MIN_RESULTS = 1;
    
    /**
     * The default number of results.
     */
    public static final int DEF_RESULTS = 1000;
    
    /**
     * The default setting if results files are deleted after parsing.
     */
    public static final boolean DEF_DELETE_AFTER_PARSE = false;
    
    /**
     * The default target directory.
     */
    public static final String DEF_DIR = System.getProperty("user.home");
    
    
    private SettingsModelString m_queryModel = 
        DocumentGrabberNodeDialog.getQueryModel();
    
    private SettingsModelIntegerBounded m_maxResultsModel = 
        DocumentGrabberNodeDialog.getMaxResultsModel();
    
    private SettingsModelString m_dataBaseModel = 
        DocumentGrabberNodeDialog.getDataBaseModel();
    
    private SettingsModelBoolean m_deleteFilesModel = 
        DocumentGrabberNodeDialog.getDeleteFilesModel();
    
    private SettingsModelString m_directoryModel = 
        DocumentGrabberNodeDialog.getDirectoryModel();
    
    private SettingsModelString m_categoryModel = 
        DocumentGrabberNodeDialog.getDocumentCategoryModel();
    
    private SettingsModelString m_typeModel = 
        DocumentGrabberNodeDialog.getDocumentTypeModel();
    
    private DocumentDataTableBuilder m_dtBuilder;
    
    /**
     * Creates new instance of <code>DocumentGrabberNodeModel</code>.
     */
    public DocumentGrabberNodeModel() {
        super(0, 1);
        m_dtBuilder = DataTableBuilderFactory.createDocumentDataTableBuilder();
    }
    


    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        return new DataTableSpec[]{m_dtBuilder.createDataTableSpec()};
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        List<Document> docs = new ArrayList<Document>();
        
        DocumentGrabber grabber = 
            DocumentGrabberFactory.getInstance().getGrabber(
                    m_dataBaseModel.getStringValue());
        if (grabber != null)  {            
            String queryStr = m_queryModel.getStringValue();
            Query query = new Query(queryStr, m_maxResultsModel.getIntValue());
            
            if (grabber instanceof AbstractDocumentGrabber) {
                boolean delete = m_deleteFilesModel.getBooleanValue();
                DocumentCategory cat = new DocumentCategory(
                        m_categoryModel.getStringValue());
                
                ((AbstractDocumentGrabber)grabber).setDeleteFiles(delete);
                ((AbstractDocumentGrabber)grabber).setDocumentCategory(cat);
                ((AbstractDocumentGrabber)grabber).setExec(exec);
            }
            
            docs = grabber.grabDocuments(
                    new File(m_directoryModel.getStringValue()), query);
        }
        
        return new BufferedDataTable[]{m_dtBuilder.createDataTable(exec, docs)};
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // Nothing to do ...
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        m_queryModel.saveSettingsTo(settings);
        m_categoryModel.saveSettingsTo(settings);
        m_dataBaseModel.saveSettingsTo(settings);
        m_deleteFilesModel.saveSettingsTo(settings);
        m_directoryModel.saveSettingsTo(settings);
        m_maxResultsModel.saveSettingsTo(settings);
        m_typeModel.saveSettingsTo(settings);        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_queryModel.validateSettings(settings);
        m_categoryModel.validateSettings(settings);
        m_dataBaseModel.validateSettings(settings);
        m_deleteFilesModel.validateSettings(settings);
        m_directoryModel.validateSettings(settings);
        m_maxResultsModel.validateSettings(settings);
        m_typeModel.validateSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_queryModel.loadSettingsFrom(settings);
        m_categoryModel.loadSettingsFrom(settings);
        m_dataBaseModel.loadSettingsFrom(settings);
        m_deleteFilesModel.loadSettingsFrom(settings);
        m_directoryModel.loadSettingsFrom(settings);
        m_maxResultsModel.loadSettingsFrom(settings);
        m_typeModel.loadSettingsFrom(settings);
    }    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File nodeInternDir, 
            final ExecutionMonitor exec)
            throws IOException, CanceledExecutionException {
        // Nothing to do ...
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir, 
            final ExecutionMonitor exec)
            throws IOException, CanceledExecutionException {
        // Nothing to do ...
    }    
}

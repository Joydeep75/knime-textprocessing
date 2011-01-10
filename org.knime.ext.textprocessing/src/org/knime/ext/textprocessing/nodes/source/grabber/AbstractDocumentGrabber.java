/*
========================================================================
 *
 *  Copyright (C) 2003 - 2011
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, version 2, as 
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ---------------------------------------------------------------------
 * 
 * History
 *   20.07.2007 (thiel): created
 */
package org.knime.ext.textprocessing.nodes.source.grabber;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.ext.textprocessing.data.DocumentCategory;
import org.knime.ext.textprocessing.data.DocumentType;


/**
 * 
 * @author Kilian Thiel, University of Konstanz
 */
public abstract class AbstractDocumentGrabber implements DocumentGrabber {

    
    private ExecutionMonitor m_exec;
    
    private boolean m_delete;
    
    private DocumentCategory m_documentCategory;
    
    private DocumentType m_documentType;
    
    /**
     * Constructor of <code>AbstractDocumentGrabber</code> with flag 
     * deleteFiles to set, which specifies if downloaded files will be deleted 
     * after parsing, the given document category and type to set
     * and <code>ExecutionMonitor</code> to handle the progress.
     * 
     * @param deleteFiles If set true files will be deleted after parsing.
     * @param documentCategory The category of the documents to set.
     * @param documentType The tpe of the document to set.
     * @param exec <code>ExecutionMonitor</code> to display the progress of
     * the process.
     */
    public AbstractDocumentGrabber(final boolean deleteFiles, 
            final DocumentCategory documentCategory,
            final DocumentType documentType,
            final ExecutionMonitor exec) {
        m_exec = exec;
        m_delete = deleteFiles;
        m_documentCategory = documentCategory;
        m_documentType = documentType;
    }

    /**
     * Constructor of <code>AbstractDocumentGrabber</code> with flag 
     * deleteFiles to set, which specifies if downloaded files will be deleted 
     * after parsing.
     * 
     * @param deleteFiles If set true files will be deleted after parsing.
     * @param documentCategory The category of the documents to set.
     */
    public AbstractDocumentGrabber(final boolean deleteFiles, 
            final DocumentCategory documentCategory) {
        this(deleteFiles, documentCategory, null, null);
    }    
    
    /**
     * Constructor of <code>AbstractDocumentGrabber</code>. Files will
     * not be deleted after parsing, deleteFiles is set false by default.
     */
    public AbstractDocumentGrabber() {
        this(false, null, null, null);
    }  
    
    /**
     * @return the delete flag. If set true files will be deleted after parsing.
     */
    public boolean getDeleteFiles() {
        return m_delete;
    }

    /**
     * @param delete If set <code>true</code> files will be deleted after 
     * parsing.
     */
    public void setDeleteFiles(final boolean delete) {
        m_delete = delete;
    }

    /**
     * @return the <code>ExecutionMonitor</code>.
     */
    public ExecutionMonitor getExec() {
        return m_exec;
    }

    /**
     * @param exec the <code>ExecutionMonitor</code> to set,
     */
    public void setExec(final ExecutionMonitor exec) {
        m_exec = exec;
    }
    
    /**
     * @see org.knime.core.node.NodeProgressMonitor#checkCanceled()
     * @throws CanceledExecutionException which indicated the execution will be
     *             canceled by this call.
     */
    protected void checkCanceled() throws CanceledExecutionException {
        if (m_exec != null) {
            m_exec.checkCanceled();
        }
    }
    
    /**
     * Sets the given progress and message if execution context is available.
     * 
     * @param progress The progress to set.
     * @param message The message to set.
     */
    protected void setProgress(final double progress, final String message) {
        if (m_exec != null) {
            m_exec.setProgress(progress, message);
        }
    }

    /**
     * @return the document category
     */
    public DocumentCategory getDocumentCategory() {
        return m_documentCategory;
    }

    /**
     * @param documentCategory the category of the document to set
     */
    public void setDocumentCategory(final DocumentCategory documentCategory) {
        m_documentCategory = documentCategory;
    }

    /**
     * @return the documentType
     */
    public DocumentType getDocumentType() {
        return m_documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(final DocumentType documentType) {
        m_documentType = documentType;
    }
}

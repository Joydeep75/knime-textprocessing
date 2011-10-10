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
 * -------------------------------------------------------------------
 * 
 * History
 *   19.12.2006 (Kilian Thiel): created
 */
package org.knime.ext.textprocessing.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataCellDataInput;
import org.knime.core.data.DataCellDataOutput;
import org.knime.core.data.DataCellSerializer;
import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.NodeLogger;
import org.knime.ext.textprocessing.nodes.source.parser.DocumentParser;
import org.knime.ext.textprocessing.nodes.source.parser.dml.DmlDocumentParser;

/**
 * A {@link org.knime.core.data.DataCell} implementation holding a 
 * {@link org.knime.ext.textprocessing.data.Document} value by storing this 
 * value in a private <code>Document</code> member. It provides a document 
 * value as well as a string value.
 * 
 * @author Kilian Thiel, University of Konstanz
 */
public class DocumentCell extends DataCell implements StringValue,
        DocumentValue {
    
    private static final NodeLogger LOGGER =
        NodeLogger.getLogger(DocumentCell.class);
    
    /**
     * Convenience access member for
     * <code>DataType.getType(DocumentCell.class)</code>.
     * 
     * @see DataType#getType(Class)
     */
    public static final DataType TYPE = DataType.getType(DocumentCell.class);

    /**
     * Returns the preferred value class of this cell implementation. This
     * method is called per reflection to determine which is the preferred
     * renderer, comparator, etc.
     * 
     * @return DocumentValue.class;
     */
    public static final Class<? extends DataValue> getPreferredValueClass() {
        return DocumentValue.class;
    }

    private static final DocumentSerializer SERIALIZER =
            new DocumentSerializer();

    /**
     * Returns the factory to read/write DataCells of this class from/to a
     * DataInput/DataOutput. This method is called via reflection.
     * 
     * @return A serializer for reading/writing cells of this kind.
     * @see DataCell
     */
    public static final DocumentSerializer getCellSerializer() {
        return SERIALIZER;
    }

    private Document m_document;

    /**
     * Creates new instance of <code>DocumentCell</code> will given document.
     * 
     * @param document The document to set.
     */
    public DocumentCell(final Document document) {
        m_document = document;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean equalsDataCell(final DataCell dc) {
        if (dc == null) {
            return false;
        }
        DocumentCell d = (DocumentCell)dc;

        if (!d.getDocument().equals(m_document)) {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return m_document.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getStringValue();
    }

    /**
     * {@inheritDoc}
     */
    public String getStringValue() {
        StringBuffer buf = new StringBuffer();
        buf.append("\"");
        buf.append(m_document.getTitle());
        buf.append("\"");
        
        return buf.toString();
    }

    /**
     * {@inheritDoc}
     */
    public Document getDocument() {
        return m_document;
    }
    
    /** Factory for (de-)serializing a DocumentCell. */
    private static class DocumentSerializer implements 
        DataCellSerializer<DocumentCell> {

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentCell deserialize(final DataCellDataInput input)
                throws IOException {
            String s = input.readUTF();
            return DocumentCell.createDocumentCell(s);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(final DocumentCell cell, 
                final DataCellDataOutput output)
                throws IOException {
            output.writeUTF(cell.getSerializationString());
        }
    }
    
    private static DocumentCell createDocumentCell(final String str) {
        Document d;
        try {
            d = DocumentCell.createDocument(str);
        } catch (Exception e) {
            LOGGER.warn("Parse error: DocumentCell could not be created!");
            return null;
        }
        return new DocumentCell(d);
    }
    
    
    /**
     * @return The String which is used to serialize the cell.
     */
    private String getSerializationString() {
        return DocumentCell.getSerializationString(m_document);
    }
    
    /**
     * Returns the instance of <code>Document</code> related to the given 
     * string.
     * @param str The string to get the related <code>Document</code> 
     * instance for.
     * @return The instance of <code>Document</code> related to the given 
     * string.
     * @throws Exception If document could not be parsed
     */
    static Document createDocument(final String str) throws Exception {
        DocumentParser parser = new DmlDocumentParser();
        List<Document> docs = parser.parse(new ByteArrayInputStream(
                str.getBytes("UTF-8")));
        Document doc = null;
        if (docs.size() > 0) {
            doc = docs.get(0);
        }
        return doc;
    }
      
    
    /**
     * Returns a xml serialization string for given <code>Document</code>.
     * @param doc The document to get the serialization string for.
     * @return The serialization string for given <code>Document</code>.
     */
    static String getSerializationString(final Document doc) {        
        return DmlDocumentParser.documentAsDml(doc);
    }
}

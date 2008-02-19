/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2008
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
 *   04.01.2007 (thiel): created
 */
package org.knime.ext.textprocessing.data;

import org.knime.core.data.DataValue;
import org.knime.core.data.DataValueComparator;

/**
 * Comparator returned by the 
 * {@link org.knime.ext.textprocessing.data.TermValue} interface. 
 *
 * @see org.knime.ext.textprocessing.data.TermValue#UTILITY
 * @see org.knime.ext.textprocessing.data.TermValue.TermUtilityFactory
 * @author Kilian Thiel, University of Konstanz
 */
public class TermValueComparator extends DataValueComparator {

    /**
     * Compares two {@link org.knime.ext.textprocessing.data.TermValue}s based 
     * on their words (tags are ignored).
     * 
     * {@inheritDoc}
     */
    @Override
    protected int compareDataValues(final DataValue v1, final DataValue v2) {
        String str1 = ((TermValue)v1).getTermValue().getText();
        String str2 = ((TermValue)v2).getTermValue().getText();
        return str1.compareTo(str2);
    }

}

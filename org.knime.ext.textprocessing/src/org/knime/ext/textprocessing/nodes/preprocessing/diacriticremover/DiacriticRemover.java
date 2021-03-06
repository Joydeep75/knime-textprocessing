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
 *   22.09.2016 (andisadewi): created
 */
package org.knime.ext.textprocessing.nodes.preprocessing.diacriticremover;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.knime.ext.textprocessing.data.Term;
import org.knime.ext.textprocessing.data.Word;
import org.knime.ext.textprocessing.nodes.preprocessing.StringPreprocessing;
import org.knime.ext.textprocessing.nodes.preprocessing.TermPreprocessing;

/**
 * This node removes all diacritical marks in a document
 *
 * @see <a href="https://en.wikipedia.org/wiki/Combining_Diacritical_Marks">List of diacritical marks</a>
 * @author Andisa Dewi, KNIME.com, Berlin, Germany
 */
public class DiacriticRemover implements TermPreprocessing, StringPreprocessing {

    /**
     * Creates new instance of DiacriticRemover.
     */
    public DiacriticRemover() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String preprocessString(final String str) {
        return removeDiacriticMarks(str);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Term preprocessTerm(final Term term) {
        List<Word> words = term.getWords();
        List<Word> newWords = new ArrayList<Word>();
        for (Word w : words) {
            newWords.add(new Word(removeDiacriticMarks(w.getWord()), w.getWhitespaceSuffix()));
        }
        return new Term(newWords, term.getTags(), term.isUnmodifiable());
    }

    private String removeDiacriticMarks(final String str) {
        String output;
        output = Normalizer.normalize(str, Normalizer.Form.NFD);
        output = output.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return output;
    }

}

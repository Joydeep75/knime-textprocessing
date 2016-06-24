/*******************************************************************************
 * Copyright by KNIME GmbH, Konstanz, Germany
 * Website: http://www.knime.org; Email: contact@knime.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, Version 3, as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7:
 *
 * KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 * Hence, KNIME and ECLIPSE are both independent programs and are not
 * derived from each other. Should, however, the interpretation of the
 * GNU GPL Version 3 ("License") under any applicable laws result in
 * KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 * you the additional permission to use and propagate KNIME together with
 * ECLIPSE with only the license terms in place for ECLIPSE applying to
 * ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 * license terms of ECLIPSE themselves allow for the respective use and
 * propagation of ECLIPSE together with KNIME.
 *
 * Additional permission relating to nodes for KNIME that extend the Node
 * Extension (and in particular that are based on subclasses of NodeModel,
 * NodeDialog, and NodeView) and that only interoperate with KNIME through
 * standard APIs ("Nodes"):
 * Nodes are deemed to be separate and independent programs and to not be
 * covered works.  Notwithstanding anything to the contrary in the
 * License, the License does not apply to Nodes, you are not required to
 * license Nodes under the License, and you are granted a license to
 * prepare and propagate Nodes, in each case even if such Nodes are
 * propagated with or for interoperation with KNIME.  The owner of a Node
 * may freely choose the license terms applicable to such Node, including
 * when such Node is propagated with or for interoperation with KNIME.
 *******************************************************************************/
package org.knime.ext.textprocessing.dl4j.settings.impl;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.ext.dl4j.base.settings.IParameterSettingsModels;
import org.knime.ext.textprocessing.dl4j.settings.enumerate.WordVectorLearnerParameter;

/**
 * Implementation of {@link IParameterSettingsModels} for Word Vector specific parameters.
 *
 * @author David Kolb, KNIME.com GmbH
 */
public class WordVectorParameterSettingsModels implements IParameterSettingsModels<WordVectorLearnerParameter> {
	
	private SettingsModelIntegerBounded m_minWordFrequency;
	private SettingsModelIntegerBounded m_layerSize;
	private SettingsModelIntegerBounded m_windowSize;
	private SettingsModelString m_wordVectorTrainingsMode;
	private SettingsModelDoubleBounded m_minimumLearningRate;
	
	private List<SettingsModel> m_allInitializedSettings = new ArrayList<>();
	
	@Override
	public SettingsModel createParameter(WordVectorLearnerParameter enumerate) throws IllegalStateException {
		switch (enumerate) {
		case LAYER_SIZE:
			return new SettingsModelIntegerBounded("layer_size", 
					WordVectorLearnerParameter.DEFAULT_LAYER_SIZE, 1, Integer.MAX_VALUE);
		case MIN_WORD_FREQUENCY:
			return new SettingsModelIntegerBounded("min_word_frequency",
					WordVectorLearnerParameter.DEFAULT_MIN_WORD_FREQUENCY, 0, Integer.MAX_VALUE);
		case WINDOW_SIZE:
			return new SettingsModelIntegerBounded("window_size", 
					WordVectorLearnerParameter.DEFAULT_WINDOW_SIZE, 1, Integer.MAX_VALUE);
		case WORD_VECTOR_TRAINING_MODE:
			return new SettingsModelString("word_vector_trainings_mode", 
					WordVectorLearnerParameter.DEFAULT_WORD_VECTOR_TRAININGS_MODE);
		case MIN_LEARNING_RATE:
			return new SettingsModelDoubleBounded("minimum_learning_rate", 
					WordVectorLearnerParameter.DEFAULT_MIN_LEARNING_RATE, 0.0, Double.MAX_VALUE);		
		default:
			throw new IllegalStateException(
                    "WordVectorParameter does not exist: "
                            + enumerate.toString());
		}
	}

	@Override
	public void setParameter(WordVectorLearnerParameter enumerate) throws IllegalStateException {
		switch (enumerate) {
		case LAYER_SIZE:
			m_layerSize = (SettingsModelIntegerBounded)createParameter(enumerate);
			addToSet(m_layerSize);
			break;
		case MIN_WORD_FREQUENCY:
			m_minWordFrequency = (SettingsModelIntegerBounded)createParameter(enumerate);
			addToSet(m_minWordFrequency);
			break;
		case WINDOW_SIZE:
			m_windowSize = (SettingsModelIntegerBounded)createParameter(enumerate);
			addToSet(m_windowSize);
			break;
		case WORD_VECTOR_TRAINING_MODE:
			m_wordVectorTrainingsMode = (SettingsModelString)createParameter(enumerate);
			addToSet(m_wordVectorTrainingsMode);
			break;
		case MIN_LEARNING_RATE:
			m_minimumLearningRate = (SettingsModelDoubleBounded)createParameter(enumerate);
			addToSet(m_minimumLearningRate);
			break;
		default:
			throw new IllegalStateException(
                    "WordVectorParameter does not exist: "
                            + enumerate.toString());
		}
	}

	
	
	@Override
	public List<SettingsModel> getAllInitializedSettings() {
		return m_allInitializedSettings;
	}
	
	private void addToSet(SettingsModel model){
		if(!m_allInitializedSettings.contains(model)){
			m_allInitializedSettings.add(model);
		}
	}

	public SettingsModelDoubleBounded getMinimumLearningRate(){
		return m_minimumLearningRate;
	}
	
	public SettingsModelIntegerBounded getMinWordFrequency() {
		return m_minWordFrequency;
	}

	public SettingsModelIntegerBounded getLayerSize() {
		return m_layerSize;
	}

	public SettingsModelIntegerBounded getWindowSize() {
		return m_windowSize;
	}
	
	public SettingsModelString getWordVectorTrainingsMode() {
		return m_wordVectorTrainingsMode;
	}

}

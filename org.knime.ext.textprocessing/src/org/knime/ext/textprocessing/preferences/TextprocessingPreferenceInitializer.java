/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2009
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
 *   03.03.2008 (Kilian Thiel): created
 */
package org.knime.ext.textprocessing.preferences;

import org.knime.ext.textprocessing.TextprocessingCorePlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 
 * @author Kilian Thiel, University of Konstanz
 */
public class TextprocessingPreferenceInitializer extends
        AbstractPreferenceInitializer {

    private static final boolean DEFAULT_USE_BLOB = true;
    
    /** Preference key for the usage of blob cells setting. */
    public static final String PREF_USE_BLOB = "knime.textprocessing.blobcell";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = TextprocessingCorePlugin.getDefault()
            .getPreferenceStore();

        //set default values
        store.setDefault(PREF_USE_BLOB, DEFAULT_USE_BLOB);
    }

    /**
     * Returns true if Blob cells have to be used.
     * 
     * @return the Blob cell setting
     */
    public static boolean useBlobCell() {
        final IPreferenceStore pStore = 
            TextprocessingCorePlugin.getDefault().getPreferenceStore();
        
        if (!pStore.contains(PREF_USE_BLOB)) {
            return DEFAULT_USE_BLOB;
        }
        return pStore.getBoolean(PREF_USE_BLOB);
    }
}

/**
 * Media Store V3
 * Copyright (C) 2015 Software Design and Quality Group (SDQ), KIT, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sdq.mediastore.basic.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GlobalConstantsContainer {
    public static final String SEPARATOR = System.getProperty("file.separator");
    private static Properties properties = null;
    private static long timestamp = -1;
    
    
    public static String getTempDirPath() {
    	return getProperty("TEMP_DIR_PATH");
    }
    public static String getFileDir() {
        return getProperty("FILE_DIR_PATH");
    }
    public static int getCacheCapacity() {
    	return Integer.parseInt(getProperty("CACHE_CAPACITY"));
    }
    
    private static String getProperty(String prop) {

        File configFile = new File("GlobalConstantsContainer.properties");
        System.out.println(configFile.lastModified());
    	if (properties == null || configFile.lastModified() != timestamp) {
    		loadProperties();
    	}
    	System.out.println("Retrieving Property : " + prop);
    	return properties.getProperty(prop);
    }
    private static void loadProperties() {
    	System.out.println("Loading properties");

        File configFile = new File("GlobalConstantsContainer.properties");
    	properties = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("GlobalConstantsContainer.properties");
			properties.load(input);
			timestamp = configFile.lastModified();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package jp.dip.komusubi.common.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class XmlResourceBundleControl extends ResourceBundle.Control {
	private static final String FORMAT = "xml";
	
	@Override
	public List<String> getFormats(String baseName) {
		if (baseName == null)
			throw new NullPointerException();
		return Arrays.asList(FORMAT);
	}

	@Override
	public ResourceBundle newBundle(String baseName, 
										Locale locale, 
										String format,
										ClassLoader loader, 
										boolean reload) throws IllegalAccessException,
			InstantiationException, IOException {
		if (baseName == null || locale == null || format == null || loader == null)
			throw new NullPointerException();
		ResourceBundle bundle = null;
		if (format.equals(FORMAT)) {
			String bundleName = toBundleName(baseName, locale);
			String resourceName = toResourceName(bundleName, format);
			InputStream stream = null;
			if (reload) {
				URL url = loader.getResource(resourceName);
				if (url != null) {
					URLConnection connection = url.openConnection();
					if (connection != null) {
						// Disable caches to get fresh data for
						// reloading.
						connection.setUseCaches(false);
						stream = connection.getInputStream();
					}
				}
			} else {
				stream = loader.getResourceAsStream(resourceName);
			}
			if (stream != null) {
				BufferedInputStream bis = new BufferedInputStream(stream);
				bundle = new XmlResourceBundle(bis);
				bis.close();
			}
		}
		return bundle;
	}
}



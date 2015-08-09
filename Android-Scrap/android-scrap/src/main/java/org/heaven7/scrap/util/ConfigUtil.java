/*
 * Copyright (C) 2015
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.heaven7.scrap.util;

import android.content.Context;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

public class ConfigUtil {

	public static Properties loadAssetsConfig(Context context,String filename){
		try {
			InputStream in = context.getAssets().open(filename);
			return load(in, "caused by under the /assets, filename ="+filename+" load failed or File Not Found !");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * load the xxx.properties from src
	 * <li>Note: Game Framework may cannot load.
	 * @param configName exclude extension  
	 */
	public static Properties loadSrcConfig(String configName){
		InputStream in = ConfigUtil.class.getResourceAsStream("/"+configName+".properties");
		return load(in, "caused by under the src ,config ="+configName+".properties load failed or File Not Found !");
	}
	
	/** load properties which is under the raw (exclude extension )*/
	public static Properties loadRawConfig(Context context,String rawResName) {

		InputStream  in = context.getResources().openRawResource(
				ResourceUtil.getResId(context,rawResName, ResourceUtil.ResourceType.Raw));
		if (in == null)
			throw new IllegalStateException(
					"caused by under /res/raw the config of "+rawResName+" is not found!");
		
		return load(in, "caused by config ="+rawResName+".properties load failed or File Not Found !");
	}

	/***
	 * load input and close input
	 */
	private static Properties load(InputStream in,String exceptionMsg){
		Properties prop = new Properties();
		try {
			prop.load(in);
			Object value;
			for(Entry<Object, Object> en: prop.entrySet()){
				value = en.getValue();
				if(value instanceof String){
					if(StringUtils.containsWhitespace((CharSequence) value)){
						//去掉末尾的所有空格
						prop.put(en.getKey(), StringUtils.trimTrailingWhitespace((String) value));
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(exceptionMsg);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return prop;
	}

}

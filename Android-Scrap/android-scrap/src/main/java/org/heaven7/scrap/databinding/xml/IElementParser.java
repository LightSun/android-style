package org.heaven7.scrap.databinding.xml;

import com.heaven7.xml.XmlReader.Element;

public interface IElementParser {

	/** 解析是否成功 */
	boolean parse(Element root);
}

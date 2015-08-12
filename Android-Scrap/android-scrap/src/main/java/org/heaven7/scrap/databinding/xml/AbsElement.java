package org.heaven7.scrap.databinding.xml;

import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class AbsElement implements ISerializeXml{
	
	private TreeMap<String, String> mAttrMap;
	private String mElementName;

	private static final Comparator<String> sDefaultComparator = new Comparator<String>() {
		public int compare(String arg0, String arg1) {
			return 1;
		}
	};

	public AbsElement(String mElementName) {
		this.mElementName = mElementName;
		this.mAttrMap = new TreeMap<String, String>(sDefaultComparator);
	}

	public String getElementName(){
		return mElementName;
	}

	public AbsElement addAttribute(String name, String value) {
		mAttrMap.put(name, value);
		return this;
	}
	public String getAttribute(String name){
		return getAttributeMap().get(name);
	}

	public TreeMap<String, String> getAttributeMap() {
		return mAttrMap;
	}

	protected void writeAttrs(XmlWriter writer) {
		try {
			final TreeMap<String, String> attrMap = getAttributeMap();
			Entry<String, String> en = null;
			while ((en = attrMap.pollFirstEntry()) != null) {
				writer.attribute(en.getKey(), en.getValue());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"{" +
				"mAttributeMap=" + mAttrMap +
				'}';
	}
}

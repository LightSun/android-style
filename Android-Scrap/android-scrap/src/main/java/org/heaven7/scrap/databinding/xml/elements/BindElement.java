package org.heaven7.scrap.databinding.xml.elements;

import com.heaven7.xml.XmlWriter;

import org.heaven7.scrap.databinding.xml.AbsElement;
import org.heaven7.scrap.databinding.xml.XmlKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class BindElement extends AbsElement {
    //id = resname
    private List<PropertyElement> list;

    public BindElement(String mElementName) {
        super(mElementName);
    }

    public void setId( String id ){
        addAttribute(XmlKeys.ID, id);
    }
    public String getId(){
        return getAttribute(XmlKeys.ID);
    }

    public void addPropertyElement(PropertyElement pe){
        if(list ==null)
            list = new ArrayList<>();
        list.add(pe);
    }
    public void setPropertyElements(List<PropertyElement> list){
        this.list = list;
    }
    public List<PropertyElement> getPropertyElements(){
        return list;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(getElementName());
        writeAttrs(writer);

        if(list != null ) {
            List<PropertyElement> list = this.list;
            int len = list.size();
            for (int i = len - 1; i >=0 ; i--) {
                list.get(i).write(writer);
            }
        }
        writer.pop();
    }
}

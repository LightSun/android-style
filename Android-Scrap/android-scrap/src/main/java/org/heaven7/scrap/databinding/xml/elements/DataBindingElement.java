package org.heaven7.scrap.databinding.xml.elements;

import android.text.TextUtils;

import com.heaven7.xml.Array;
import com.heaven7.xml.XmlReader;
import com.heaven7.xml.XmlWriter;

import org.heaven7.scrap.databinding.xml.AbsElement;
import org.heaven7.scrap.databinding.xml.IElementParser;
import org.heaven7.scrap.databinding.xml.XmlElementNames;
import org.heaven7.scrap.databinding.xml.XmlKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class DataBindingElement extends AbsElement implements IElementParser{

    private DataElement dataElement;
    private List<BindElement> bindElements;
    private IElementParseListener mElementListener;

    public DataBindingElement(String mElementName) {
        super(mElementName);
    }

    public DataElement getDataElement() {
        return dataElement;
    }
    public void setDataElement(DataElement dataElement) {
        this.dataElement = dataElement;
    }

    public List<BindElement> getBindElements() {
        return bindElements;
    }
    public void addBindElement(BindElement e){
        if(bindElements ==null)
            bindElements = new ArrayList<>();
        bindElements.add(e);
    }
    public void setBindElements(List<BindElement> bindElements) {
        this.bindElements = bindElements;
    }

    public IElementParseListener getElementParseListener() {
        return mElementListener;
    }
    public void setElementParseListener(IElementParseListener l) {
        this.mElementListener = l;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
        writer.element(XmlElementNames.DATA_BINDING);
        writeAttrs(writer);
        if( dataElement !=null )
            dataElement.write(writer);
        if(bindElements != null ) {
            List<BindElement> bindElements = this.bindElements;
            int len = bindElements.size();
            for (int i = len - 1; i >=0 ; i--) {
                bindElements.get(i).write(writer);
            }
        }
        writer.pop();
    }

    @Override
    public boolean parse(XmlReader.Element root) {
       XmlReader.Element dataEle = root.getChildByName(XmlElementNames.DATA);
        DataElement dataElement = new DataElement(XmlElementNames.DATA);
       //parse var and import
        parseVariableAndImport(dataEle, dataElement);
        handleCallbackIfNeed(dataElement);
        setDataElement(dataElement);
        //parse binds
        parseBindElements(root);
        return true;
    }

    private void parseBindElements(XmlReader.Element root) {
        BindElement be = null;
        PropertyElement pe = null;

        Array<XmlReader.Element> array = root.getChildrenByName(XmlElementNames.BIND);
        Array<XmlReader.Element> propArray = null;

        for( int i=0,size = array.size ; i<size ;i++){
            be = new BindElement(XmlElementNames.BIND);
            XmlReader.Element bindEle =  array.get(i);
            String id = bindEle.getAttribute(XmlKeys.ID,null);
            checkEmpty(id,XmlKeys.ID);
            be.setId(id.trim());

            propArray = bindEle.getChildrenByName(XmlElementNames.PROPERTY);
            for( int j=0,size2 = propArray.size ; j<size2 ;j++){
                XmlReader.Element propEle =  propArray.get(j);
                pe = new PropertyElement(XmlElementNames.PROPERTY);

                String name = propEle.getAttribute(XmlKeys.NAME, null);
                checkEmpty(name,XmlKeys.NAME);
                pe.setName(name.trim());

                //referVariable can't be null
                String referVariable = propEle.getAttribute(XmlKeys.REFER_VARIABLE, null);
                checkEmpty(referVariable,XmlKeys.REFER_VARIABLE);
                pe.setReferVariable(referVariable.trim());

                String referImport = propEle.getAttribute(XmlKeys.REFER_IMPORT, null);
                if( !TextUtils.isEmpty(referImport) ){
                    pe.setReferImport(referImport.trim());
                }

                String valueType = propEle.getAttribute(XmlKeys.VALUE_TYPE, null);
                checkEmpty(valueType,XmlKeys.VALUE_TYPE);
                pe.setValueType(valueType);

                String text = propEle.getText();//text can be null?
                pe.setText(TextUtils.isEmpty(text) ? "" : text.trim());

                handleCallbackIfNeed(pe);
                be.addPropertyElement(pe);
            }
            handleCallbackIfNeed(be);
            addBindElement(be);
        }
    }

    /**
     * @param val  the value to check
     * @param tag   the tag to log
     */
    private void checkEmpty(String val,String tag){
        if(TextUtils.isEmpty(val)){
            throw new RuntimeException(tag+" can't be empty");
        }
    }

    private void parseVariableAndImport(XmlReader.Element dataEle, DataElement dataElement) {
        VariableElement ve = null;
        for (XmlReader.Element e : dataEle.getChildrenByName(XmlElementNames.VARIABLE)) {
             ve = new VariableElement(XmlElementNames.VARIABLE);
             String classname = e.getAttribute(XmlKeys.CLASS_NAME,null);
            checkEmpty(classname,XmlKeys.CLASS_NAME);
            ve.setClassname(classname.trim());

            String name = e.getAttribute(XmlKeys.NAME,null);
            checkEmpty(name,XmlKeys.NAME);
            ve.setName(name.trim());

            String type = e.getAttribute(XmlKeys.TYPE,null);
            checkEmpty(type,XmlKeys.TYPE);
            ve.setType(type.trim());

            handleCallbackIfNeed(ve);
            dataElement.addVariableElement(ve);
        }

        ImportElement ie = null;
        for (XmlReader.Element e : dataEle.getChildrenByName(XmlElementNames.IMPORT)) {
            ie = new ImportElement(XmlElementNames.IMPORT);
            String classname = e.getAttribute(XmlKeys.CLASS_NAME,null);
            checkEmpty(classname,XmlKeys.CLASS_NAME);
            ie.setClassname(classname.trim());

            String alias = e.getAttribute(XmlKeys.ALIAS,null);
           // checkEmpty(alias); //can be null
            ie.setAlias(alias==null?"":alias.trim());

            handleCallbackIfNeed(ie);
            dataElement.addImportElement(ie);
        }
    }

    private void handleCallbackIfNeed(AbsElement e) {
       if(mElementListener != null){
           mElementListener.onParsed(e);
       }
    }

    /** element parse listener */
    public interface IElementParseListener{
        /**
         * called when  an element is parsed.
         * @param e
         */
        void onParsed(AbsElement e);
    }
}

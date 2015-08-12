package org.heaven7.scrap.databinding;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.heaven7.xml.ObjectMap;

import org.heaven7.scrap.annotation.NonNull;
import org.heaven7.scrap.databinding.xml.AbsElement;
import org.heaven7.scrap.databinding.xml.XmlElementNames;
import org.heaven7.scrap.databinding.xml.elements.BindElement;
import org.heaven7.scrap.databinding.xml.elements.DataBindingElement;
import org.heaven7.scrap.databinding.xml.elements.ImportElement;
import org.heaven7.scrap.databinding.xml.elements.PropertyElement;
import org.heaven7.scrap.databinding.xml.elements.VariableElement;
import org.heaven7.scrap.util.ResourceUtil;

import java.util.List;

/**
 * Created by heaven7 on 2015/8/11.
 */
public class DataBindParser {

    private final View root;

    private final ObjectMap<String,String> mImportMap ;
    private final ObjectMap<String,String> mVariableBeanMap ;
    private final ObjectMap<String,String> mVariableCallbakMap;
    private  ObjectMap<String,String> mVariableBeansMap;

    private final InternalElementParserListener mParserListenerImpl;

    public DataBindParser(@NonNull View root) {
        if(root == null)
            throw new NullPointerException();
        this.root = root;
        mParserListenerImpl = new InternalElementParserListener();
        mImportMap = new ObjectMap<>(8);
        mVariableBeanMap = new ObjectMap<>(8);
        mVariableCallbakMap = new ObjectMap<>(8);
    }
    public Context getContext(){
        return root.getContext();
    }
    public DataBindingElement.IElementParseListener getElementParserListener(){
       return mParserListenerImpl;
    }

    private void doWithImportElement(ImportElement ie) {
        String alias = ie.getAlias();
        String classname = ie.getClassname();//full class name
        if(!classname.contains(".")) throw new RuntimeException("class name must be full name.");
        if(TextUtils.isEmpty(alias)){
            alias = classname.substring(classname.lastIndexOf(".")+1);
        }
        mImportMap.put(alias,classname);
    }

    private void doWithBindElement(BindElement be) {
        int id = ResourceUtil.getResId(getContext(), be.getId(), ResourceUtil.ResourceType.Id);
        //TODO
        List<PropertyElement> propEles = be.getPropertyElements();
        if(propEles!=null && propEles.size()>0){
            PropertyBindInfo info ;
            for(int i =0,size = propEles.size() ; i <size ;i++){
                PropertyElement e = propEles.get(i);
                info = new PropertyBindInfo();

            }
        }
    }

    private void doWithVariableElement(VariableElement ve) {
        switch (ve.getType()){
            case VariableType.BEAN:
                mVariableBeanMap.put(ve.getName(),ve.getClassname());
                break;
            case VariableType.BEANS: //means list
                if(mVariableBeansMap==null)
                    mVariableBeansMap = new ObjectMap<>(3);
                mVariableBeansMap.put(ve.getName(),ve.getClassname());
                break;
            case VariableType.CALLBACK://event
                mVariableCallbakMap.put(ve.getName(),ve.getClassname());
                break;
        }
    }

    private class InternalElementParserListener implements DataBindingElement.IElementParseListener{
        @Override
        public void onParsed(AbsElement e) {
            switch (e.getElementName()){
                case XmlElementNames.IMPORT:
                    ImportElement ie = (ImportElement) e;
                    doWithImportElement(ie);
                    break;

                case XmlElementNames.VARIABLE:
                    VariableElement ve = (VariableElement) e;
                    doWithVariableElement(ve);
                    break;

                case XmlElementNames.BIND:
                    BindElement be = (BindElement) e;
                    doWithBindElement(be);
                    break;
            }
        }
    }

   public class PropertyBindInfo{
       public String applyViewPropertyName;
       public String [] referVariables;
       public String [] referImports;
       public String expressionValueType;
       public String expression;

       public int applyViewId;
    }
}

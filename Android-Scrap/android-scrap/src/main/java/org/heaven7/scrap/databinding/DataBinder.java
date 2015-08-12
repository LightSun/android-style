package org.heaven7.scrap.databinding;

import android.content.Context;
import android.view.View;

import com.heaven7.xml.XmlReader;

import org.heaven7.scrap.databinding.xml.XmlElementNames;
import org.heaven7.scrap.databinding.xml.elements.DataBindingElement;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class DataBinder{


    public DataBinder() {
    }

    public void bind(Context context,View root,int bindsRawResId){
       // parse bind xml
        InputStream in = context.getResources().openRawResource(bindsRawResId);
        DataBindingElement dbe = new DataBindingElement(XmlElementNames.DATA_BINDING);
      //TODO   dbe.setElementParseListener(this);
        try {
            XmlReader.Element rootEle = new XmlReader().parse(in);
            dbe.parse(rootEle);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }finally{
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        // apply
       // property change listener ( attach and detach) notifyDataChange(user)
    }


    public void destroy(){

    }

}

package com.cn21.web.util;
import com.cn21.web.interceptor.config.OverallSituationToken;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2016/8/23.
 */
public class InterceptorXMLParse {


    public  List<String> parse(String fileName){
        SAXReader saxReader = new SAXReader();
        Document document = null;
        InputStream in =OverallSituationToken.class.getResourceAsStream(fileName);
       // File file = new File();
       // System.out.println(file.exists());

        try {
            document = saxReader.read(in);
        }catch (DocumentException e){
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        List<Element> childElement = root.elements();
        List<String> interceptorsClassName = new ArrayList<String>();
        for(Element child :childElement){
            interceptorsClassName.add(child.attributeValue("class"));
        }
        return interceptorsClassName;
    }
}

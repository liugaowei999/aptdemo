package com.ly.traffic.middleplatform.apt;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FactoryGroupedClasses {

    private static final String SUFFIX = "Factory";
    private String qualifiedClassName;

    private Map<String, FactoryAnnotatedClass> itemsMap = new LinkedHashMap<>();

    public FactoryGroupedClasses(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    public void add(FactoryAnnotatedClass toInsert) {
        FactoryAnnotatedClass factoryAnnotatedClass = itemsMap.get(toInsert.getmId());
        if (factoryAnnotatedClass != null) {
//            throw new IdAlreadyUsedException(factoryAnnotatedClass);
        }
        itemsMap.put(toInsert.getmId(), toInsert);
    }

  public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        //  Generate java file
    }
}

package com.ly.traffic.middleplatform.apt.generatecode;

import com.ly.traffic.middleplatform.apt.exception.ProcessingException;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
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

    public void add(FactoryAnnotatedClass toInsert) throws ProcessingException {
        FactoryAnnotatedClass factoryAnnotatedClass = itemsMap.get(toInsert.getmId());
        if (factoryAnnotatedClass != null) {
//            throw new IdAlreadyUsedException(factoryAnnotatedClass);
            throw new ProcessingException("IdAlreadyUsedException:%s",
                    factoryAnnotatedClass.getmQualifiedSuperClassName());
        } else {
            itemsMap.put(toInsert.getmId(), toInsert);
        }
    }

  public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        //  Generate java file
      TypeElement superClassName = elementUtils.getTypeElement(qualifiedClassName);
      String factoryClassName = superClassName.getSimpleName() + SUFFIX;
      String qualifiedFactoryClassName = qualifiedClassName + SUFFIX;
      PackageElement pkg = elementUtils.getPackageOf(superClassName);
      String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

      MethodSpec.Builder method = MethodSpec.methodBuilder("create")
              .addModifiers(Modifier.PUBLIC)
              .addParameter(String.class, "id")
              .returns(TypeName.get(superClassName.asType()));
      method.beginControlFlow("if (id == null)")
              .addStatement("throw new IllegalArgumentException($S)", "id is null!")
              .endControlFlow();

      for (FactoryAnnotatedClass item : itemsMap.values()) {
          method.beginControlFlow("if ($S.equals(id))", item.getmId())
                  .addStatement("return new $L()", item.getTypeElement().getQualifiedName().toString())
                  .endControlFlow();
      }

      method.addStatement("throw new IllegalArgumentException($S + id)", "Unknown id = ");

      TypeSpec typeSpec = TypeSpec
              .classBuilder(factoryClassName)
              .addModifiers(Modifier.PUBLIC)
              .addMethod(method.build())
              .build();

      JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }
}

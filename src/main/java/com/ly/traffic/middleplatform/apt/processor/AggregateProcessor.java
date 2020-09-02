package com.ly.traffic.middleplatform.apt.processor;

import com.google.auto.service.AutoService;
import com.ly.traffic.middleplatform.apt.ProcessingException;
import com.ly.traffic.middleplatform.apt.annotation.Aggregate;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author liugw
 * @Package com.ly.traffic.middleplatform.utils.apt.processor
 * @Description: 编译期Aggregate注解处理器
 * @date 2020/9/2 8:51
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.ly.traffic.middleplatform.apt.annotation.Aggregate"})
public class AggregateProcessor extends AbstractProcessor {
    private Messager messager;
    private Types typeUtils;
    private Filer filer;
    private Locale locale;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        locale = processingEnv.getLocale();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Aggregate.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                // 被注解的不是一个类，抛出异常
                String msg = "[" + annotatedElement.getSimpleName() + "]: 不是一个类! 类上才能使用该注解:["
                        + Aggregate.class.getCanonicalName() + "]";
                messager.printMessage(Diagnostic.Kind.ERROR, msg);
            }
            // 将annotatedElement中包含的信息封装成一个对象，方便后续使用
            TypeElement currentClass = (TypeElement) annotatedElement;
            Aggregate annotation = currentClass.getAnnotation(Aggregate.class);
            boolean force = annotation.forceRewriteSuperField();
            if (force) {
                // 如果设置强制重新父类字段，则跳过检查
                continue;
            }

            TypeMirror superClassType = currentClass.getSuperclass();
            if (superClassType.getKind() != TypeKind.NONE) {
                TypeElement superclass = (TypeElement)typeUtils.asElement(superClassType);
                List<String> currentClassFiledNameSet = getFiledNameSet(currentClass);
                List<String> superClassFiledNameSet = getFiledNameSet((TypeElement)superclass);
                if (superclass.getQualifiedName().toString().equals("com.ly.traffic.middleplatform.domain.createorder.entity.UnionOrderEntity")) {
                    currentClassFiledNameSet.retainAll(superClassFiledNameSet);
                    if (CollectionUtils.isNotEmpty(currentClassFiledNameSet)) {
                        String msg = currentClass.getQualifiedName() + ".java ：the field [" + currentClassFiledNameSet.get(0)
                                + "] is already defined in his super class :["
                                + superclass.getQualifiedName() + "]";
                        messager.printMessage(Diagnostic.Kind.ERROR, msg);
                    }
                }
            }
        }
        return true;
    }

    private List<String> getFiledNameSet(TypeElement typeElement) {
        List<String> filedNameSet = new ArrayList<>();
        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        for (Element element : enclosedElements) {
            if (element instanceof VariableElement) {
                VariableElement variableElement = (VariableElement)element;
                filedNameSet.add(variableElement.getSimpleName().toString());
            }
        }
        return filedNameSet;
    }

    private String getPrintMsg(TypeElement typeElement) {
        String msg = "";

        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        for (Element element : enclosedElements) {
            if (element instanceof VariableElement) {
                VariableElement variableElement = (VariableElement)element;
                msg += "\nfieldName:" + variableElement.getSimpleName()
                        + "； fieldValue:" + variableElement.getConstantValue();
            }
        }
        return msg;
    }
}

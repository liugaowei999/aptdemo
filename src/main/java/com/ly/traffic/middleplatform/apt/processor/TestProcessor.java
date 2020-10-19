/*
package com.ly.traffic.middleplatform.apt.processor;

import com.google.auto.service.AutoService;
import com.ly.traffic.middleplatform.apt.generatecode.FactoryAnnotatedClass;
import com.ly.traffic.middleplatform.apt.generatecode.FactoryGroupedClasses;
import com.ly.traffic.middleplatform.apt.exception.ProcessingException;
import com.ly.traffic.middleplatform.apt.annotation.Factory;
import com.sun.tools.javac.api.JavacTrees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

*/
/**
 * @author liugw
 * @Package com.ly.traffic.middleplatform.infrastructure.apt
 * @Description: ${TODO}
 * @date 2020/9/1 14:05
 *//*

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.ly.traffic.middleplatform.apt.annotation.Factory"})
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TestProcessor extends AbstractProcessor {
    private Types mTypeUtils;
    private Messager messager;
    private Filer mFiler;
    private Elements mElementUtils;
    private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<>();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        */
/**初始化处理器
         * ProcessingEnvironment是一个注解处理工具的集合。它包含了众多工具类。
         * 例如：
         * Filer可以用来编写新文件；
         * Messager可以用来打印错误信息；
         * Elements是一个可以处理Element的工具类。
         **//*

        super.init(processingEnv);
        mTypeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        JavacTrees javacTrees = JavacTrees.instance(processingEnv);
    }

    */
/**
     * 返回值表示注解是否由当前Processor 处理
     * 如果返回 true，则这些注解由此注解来处理，后续其它的 Processor 无需再处理它们；
     * 如果返回 false，则这些注解未在此Processor中处理并，那么后续 Processor 可以继续处理它们。
     * 其他：可以校验被注解的对象是否合法；自动生成需要的java文件等
     *
     * @param annotations 1
     * @param roundEnv 1
     * @return true or false 表示注解是否由当前Processor 处理
     *//*

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        messager.printMessage(Diagnostic.Kind.NOTE, "开始生成工厂类 ===========");
        //  通过RoundEnvironment获取到所有被@Factory注解的元素
        try {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Factory.class)) {
                if (annotatedElement.getKind() != ElementKind.CLASS) { // TypeKind.FLOAT
                    // 被注解的不是一个类，抛出异常
                    String msg = "[" + annotatedElement.getSimpleName() + "]: 不是一个类! 类上才能使用该注解:["
                            + Factory.class.getCanonicalName() + "]";
                    messager.printMessage(Diagnostic.Kind.ERROR, msg);
                }

                // 将annotatedElement中包含的信息封装成一个对象，方便后续使用
                TypeElement typeElement = (TypeElement) annotatedElement;
                FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement);
                checkValidClass(annotatedClass);

                FactoryGroupedClasses factoryClass = factoryClasses.get(annotatedClass.getmQualifiedSuperClassName());
                if (factoryClass == null) {
                    String qualifiedGroupName = annotatedClass.getmQualifiedSuperClassName();
                    factoryClass = new FactoryGroupedClasses(qualifiedGroupName);
                    factoryClasses.put(qualifiedGroupName, factoryClass);
                }
                factoryClass.add(annotatedClass);
            }
        } catch (ProcessingException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }

        try {
            for (FactoryGroupedClasses factoryClass : factoryClasses.values()) {
                factoryClass.generateCode(mElementUtils, mFiler);
            }
            // 清除factoryClasses
            factoryClasses.clear();
        } catch (IOException e) {
            int size = factoryClasses.size();
            messager.printMessage(Diagnostic.Kind.ERROR, "size="+ size +", "+ e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    */
/**
     * 校验
     * @param item 1
     * @throws ProcessingException 1
     *//*

    private void checkValidClass(FactoryAnnotatedClass item) throws ProcessingException {
        TypeElement classElement = item.getTypeElement();

        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException("The class %s is not public.",
                    classElement.getQualifiedName().toString());
        }

        // 如果是抽象方法则抛出异常终止编译
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException("The class %s is abstract. You can't annotate abstract classes with @%",
                    classElement.getQualifiedName().toString(), Factory.class.getSimpleName());
        }

        // 这个类必须是在@Factory.type()中指定的类的子类，否则抛出异常终止编译 -- getQualifiedFactoryGroupName
        TypeElement superClassElement = mElementUtils.getTypeElement(item.getmQualifiedSuperClassName());
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            // 检查被注解类是否实现或继承了@Factory.type()所指定的类型，此处均为IShape
            if (!classElement.getInterfaces().contains(superClassElement.asType())) {
                throw new ProcessingException("The class %s annotated with @%s must implement the interface %s",
                        classElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                        item.getmQualifiedSuperClassName());
            }
        } else {
            TypeElement currentClass = classElement;
            while (true) {
                TypeMirror superClassType = currentClass.getSuperclass();

                if (superClassType.getKind() == TypeKind.NONE) {
                    // 向上遍历父类，直到Object也没获取到所需父类，终止编译抛出异常
                    throw new ProcessingException("The class %s annotated with @%s must inherit from %s",
                            classElement.getQualifiedName().toString(),
                            Factory.class.getSimpleName(),
                            item.getmQualifiedSuperClassName());
                }

                if (superClassType.toString().equals(item.getmQualifiedSuperClassName())) {
                    // 校验通过，终止遍历
                    break;
                }
                currentClass = (TypeElement) mTypeUtils.asElement(superClassType);
            }
        }

        // 检查是否由public的无参构造方法
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0 &&
                        constructorElement.getModifiers().contains(Modifier.PUBLIC)) {
                    // 存在public的无参构造方法，检查结束
                    return;
                }
            }
        }

        // 为检测到public的无参构造方法，抛出异常，终止编译
        throw new ProcessingException("The class %s must provide an public empty default constructor",
                classElement.getQualifiedName().toString());
    }
}
*/

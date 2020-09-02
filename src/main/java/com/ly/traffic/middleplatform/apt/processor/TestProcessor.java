package com.ly.traffic.middleplatform.apt.processor;

/**
 * @author liugw
 * @Package com.ly.traffic.middleplatform.infrastructure.apt
 * @Description: ${TODO}
 * @date 2020/9/1 14:05
 */
//@AutoService(Processor.class)
//@SupportedAnnotationTypes({"com.ly.traffic.middleplatform.utils.apt.annotation.Factory"})
//public class TestProcessor extends AbstractProcessor {
//    private Types mTypeUtils;
//    private Messager mMessager;
//    private Filer mFiler;
//    private Elements mElementUtils;
//    private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<>();
//
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> annotationTypeSet = new LinkedHashSet<>(1);
//        annotationTypeSet.add(Factory.class.getCanonicalName());
//        return annotationTypeSet;
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
////        String specVersion = System.getProperty("java.specification.version");
////        System.out.println("******* specVersion=" + specVersion);
////        throw new RuntimeException("终止编译");
//        return SourceVersion.latestSupported();
//    }
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        /**初始化处理器
//         * ProcessingEnvironment是一个注解处理工具的集合。它包含了众多工具类。
//         * 例如：Filer可以用来编写新文件；Messager可以用来打印错误信息；Elements是一个可以处理Element的工具类。
//         **/
//        super.init(processingEnv);
//        mTypeUtils = processingEnv.getTypeUtils();
//        mMessager = processingEnv.getMessager();
//        mFiler = processingEnv.getFiler();
//        mElementUtils = processingEnv.getElementUtils();
//    }
//
//    /**
//     * 返回值表示注解是否由当前Processor 处理
//     * 如果返回 true，则这些注解由此注解来处理，后续其它的 Processor 无需再处理它们；
//     * 如果返回 false，则这些注解未在此Processor中处理并，那么后续 Processor 可以继续处理它们。
//     * 其他：可以校验被注解的对象是否合法；自动生成需要的java文件等
//     *
//     * @param annotations 1
//     * @param roundEnv 1
//     * @return true or false 表示注解是否由当前Processor 处理
//     */
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
////        mMessager.printMessage(Diagnostic.Kind.NOTE, "Note process2 ===========");
////        mMessager.printMessage(Diagnostic.Kind.ERROR, "Error process1 ===========");
//        //  通过RoundEnvironment获取到所有被@Factory注解的元素
//        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Factory.class)) {
//            System.out.println("====" + annotatedElement.getSimpleName());
//            if (annotatedElement.getKind() != ElementKind.CLASS) {
//                // 被注解的不是一个类，抛出异常
//                throw new ProcessingException(annotatedElement, "Only classes can be annotated with @%s",
//                        Factory.class.getSimpleName());
//            }
//
//            // 将annotatedElement中包含的信息封装成一个对象，方便后续使用
//            TypeElement typeElement = (TypeElement) annotatedElement;
//            FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement);
//            // .............
//        }
//        return true;
//    }
//
////    private void checkValidClass(FactoryAnnotatedClass item) throws ProcessingException {
////
////        TypeElement classElement = item.getTypeElement();
////
////        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
////            throw new ProcessingException(classElement, "The class %s is not public.",
////                    classElement.getQualifiedName().toString());
////        }
////
////        // 如果是抽象方法则抛出异常终止编译
////        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
////            throw new ProcessingException(classElement,
////                    "The class %s is abstract. You can't annotate abstract classes with @%",
////                    classElement.getQualifiedName().toString(), Factory.class.getSimpleName());
////        }
////
////        // 这个类必须是在@Factory.type()中指定的类的子类，否则抛出异常终止编译 -- getQualifiedFactoryGroupName
////        TypeElement superClassElement = mElementUtils.getTypeElement(item.getmQualifiedSuperClassName());
////        if (superClassElement.getKind() == ElementKind.INTERFACE) {
////            // 检查被注解类是否实现或继承了@Factory.type()所指定的类型，此处均为IShape
////            if (!classElement.getInterfaces().contains(superClassElement.asType())) {
////                throw new ProcessingException(classElement,
////                        "The class %s annotated with @%s must implement the interface %s",
////                        classElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
////                        item.getmQualifiedSuperClassName());
////            }
////        } else {
////            TypeElement currentClass = classElement;
////            while (true) {
////                TypeMirror superClassType = currentClass.getSuperclass();
////
////                if (superClassType.getKind() == TypeKind.NONE) {
////                    // 向上遍历父类，直到Object也没获取到所需父类，终止编译抛出异常
////                    throw new ProcessingException(classElement,
////                            "The class %s annotated with @%s must inherit from %s",
////                            classElement.getQualifiedName().toString(),
////                            Factory.class.getSimpleName(),
////                            item.getmQualifiedSuperClassName());
////                }
////
////                if (superClassType.toString().equals(item.getmQualifiedSuperClassName())) {
////                    // 校验通过，终止遍历
////                    break;
////                }
////                currentClass = (TypeElement) mTypeUtils.asElement(superClassType);
////            }
////        }
////
////        // 检查是否由public的无参构造方法
////        for (Element enclosed : classElement.getEnclosedElements()) {
////            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
////                ExecutableElement constructorElement = (ExecutableElement) enclosed;
////                if (constructorElement.getParameters().size() == 0 &&
////                        constructorElement.getModifiers().contains(Modifier.PUBLIC)) {
////                    // 存在public的无参构造方法，检查结束
////                    return;
////                }
////            }
////        }
////
////        // 为检测到public的无参构造方法，抛出异常，终止编译
////        throw new ProcessingException(classElement,
////                "The class %s must provide an public empty default constructor",
////                classElement.getQualifiedName().toString());
////    }
//}

package com.voisen.rxprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.voisen.rxprocessor.interfaces.IRxNavigation;
import com.voisen.rxprocessor.utils.RxPackageGenUtils;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedOptions("module")
@SupportedAnnotationTypes("com.voisen.rxprocessor.RxPath")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RxPathProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer envFiler;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        envFiler = processingEnvironment.getFiler();
        messager.printMessage(Diagnostic.Kind.WARNING, "注解处理器运行: "+ envFiler.toString());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.WARNING, "发现类: "+ set.size());
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(RxPath.class);
        if (elements.size() == 0){
            return false;
        }
        for (Element element : elements) {
            RxPath path = element.getAnnotation(RxPath.class);
            genClass(path, element.asType().toString());
        }
        return true;
    }

    private void genClass(RxPath path, String activity){
        String value = path.path();
        if (value.length() == 0){
            value = path.value();
        }
        if (value.length() == 0) throw new AssertionError("RxPath: 请指定 `path` 的值");
        String className = RxPackageGenUtils.getClassName(value);
        messager.printMessage(Diagnostic.Kind.WARNING, "生成类: "+ className);
        MethodSpec overMethod = MethodSpec.methodBuilder("getActivityClass")
                .addAnnotation(Override.class)
                .addCode("return $S;", activity)
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(IRxNavigation.class)
                .addMethod(overMethod)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();
        JavaFile build = JavaFile.builder(IRxNavigation.PACKAGE_NAME, typeSpec).build();
        try {
            build.writeTo(envFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

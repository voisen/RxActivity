package com.voisen.rxprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.voisen.rxprocessor.interfaces.IPath;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@SupportedOptions("module")
@SupportedAnnotationTypes("com.voisen.rxprocessor.RxPath")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RxPathProcessor extends AbstractProcessor {

    private Messager messager;
    private TypeSpec.Builder mTypeSpecBuilder;
    private static final Map<String, String> classMap = new HashMap<>();
    private Filer envFiler;
    private String moduleName = "";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        envFiler = processingEnvironment.getFiler();
        moduleName = processingEnvironment.getOptions().get("module");
        mTypeSpecBuilder = TypeSpec.classBuilder(IPath.IMPL_CLASS)
                .superclass(IPath.class)
                .addModifiers(Modifier.FINAL);
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
            String fieldName = "_" + path.value();
            classMap.put(fieldName, element.asType().toString());
        }

        for (String s : classMap.keySet()) {
            FieldSpec.Builder builder = FieldSpec.builder(String.class,
                    s,
                    Modifier.PROTECTED,
                    Modifier.FINAL).initializer("$S", classMap.get(s));
            mTypeSpecBuilder.addField(builder.build());
        }

        try {
            JavaFile.builder(IPath.class.getPackage().getName(),
                    mTypeSpecBuilder.build())
                    .build().writeTo(envFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror, ExecutableElement executableElement, String s) {
        messager.printMessage(Diagnostic.Kind.WARNING, "完成 ");
        return super.getCompletions(element, annotationMirror, executableElement, s);
    }
}

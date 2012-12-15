package org.androidtransfuse.test.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Creates a basic proxy for a class
 *
 * @author John Ericksen
 */
@SupportedAnnotationTypes({"org.androidtransfuse.test.generator.Proxy"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ProxyGenerator extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private int round = 0;
    private Map<Integer, Set<String>> roundElements = new HashMap<Integer, Set<String>>();
    private Elements elements;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        this.elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        try {

            Set<? extends Element> proxyElements = roundEnvironment.getElementsAnnotatedWith(Proxy.class);

            for (Element element : proxyElements) {
                int proxyRound = element.getAnnotation(Proxy.class).round();
                if (!roundElements.containsKey(proxyRound)) {
                    roundElements.put(proxyRound, new HashSet<String>());
                }
                Set<String> roundElementNames = roundElements.get(proxyRound);

                roundElementNames.add(element.asType().toString());
            }

            if (roundElements.containsKey(round)) {
                JCodeModel codeModel = new JCodeModel();

                for (String elementName : roundElements.get(round)) {

                    TypeElement element = elements.getTypeElement(elementName);
                    JDefinedClass jDefinedClass = codeModel._class((element).getQualifiedName() + "Proxy");

                    jDefinedClass._extends(codeModel.ref(element.getQualifiedName().toString()));

                    jDefinedClass._implements(Serializable.class);

                    messager.printMessage(Diagnostic.Kind.NOTE, "Wrote " + jDefinedClass.fullName());
                }

                codeModel.build(new FilerSourceCodeWriter(filer));
            }
        } catch (JClassAlreadyExistsException e) {
            throw new ProxyGenerationRuntimeException("Class already exists", e);
        } catch (IOException e) {
            throw new ProxyGenerationRuntimeException("IOException while writing proxy class", e);
        }

        round++;

        return false;
    }
}

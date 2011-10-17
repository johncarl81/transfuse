package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * @author John Ericksen
 */
public class ASTClassField implements ASTField {

    private Field field;
    private ASTType astType;

    public ASTClassField(Field field, ASTType astType) {
        this.field = field;
        this.astType = astType;
    }

    @Override
    public boolean isAnnotated(Class<Inject> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return field.getName();
    }
}

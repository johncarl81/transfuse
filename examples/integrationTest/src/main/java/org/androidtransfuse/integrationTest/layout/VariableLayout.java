package org.androidtransfuse.integrationTest.layout;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.LayoutHandler;
import org.androidtransfuse.util.DeclareField;

/**
 * @author John Ericksen
 */
@Activity(label = "Variable Layout")
@LayoutHandler(VariableLayoutHandler.class)
@DeclareField
public class VariableLayout {

}

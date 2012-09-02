package com.googlecode.generatefluentinterface;

import com.intellij.openapi.editor.actionSystem.EditorAction;

/**
 * <p>
 * <p> Date: 6/1/11 Time: 4:41 PM </p>
 *
 * @author Felix.ZHU
 * @since v
 */
public class GenerateFluentInterfaceAction extends EditorAction {

    protected GenerateFluentInterfaceAction() {
        super(new GenerateFluentInterfaceActionHandlerImpl());
    }
}

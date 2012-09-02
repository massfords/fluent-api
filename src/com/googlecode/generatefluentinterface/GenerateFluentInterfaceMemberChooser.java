package com.googlecode.generatefluentinterface;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * <p>
 * <p> Date: 6/15/11 Time: 4:00 PM </p>
 *
 * @author Felix.ZHU
 * @since v
 */
public class GenerateFluentInterfaceMemberChooser extends MemberChooser<PsiFieldMember> {
// ------------------------------ FIELDS ------------------------------

    private AdvanceOptionPanel advanceOptionPanel;

// --------------------------- CONSTRUCTORS ---------------------------

    public GenerateFluentInterfaceMemberChooser(PsiFieldMember[] elements,
                                                @org.jetbrains.annotations.NotNull
                                                Project project) {
        super(elements, false, true, project);
        setCopyJavadocVisible(false);

        PropertiesComponent props = PropertiesComponent.getInstance();
        advanceOptionPanel.generateGetters.setSelected(props.getBoolean("fluent.getters.enabled", false));
        advanceOptionPanel.gPrefix.setText(props.getValue("fluent.getters.prefix", ""));
        advanceOptionPanel.prefix.setText(props.getValue("fluent.setters.prefix", ""));

    }

    @Override
    protected void doOKAction() {
        // update our prefs
        PropertiesComponent props = PropertiesComponent.getInstance();
        props.setValue("fluent.getters.enabled", String.valueOf(advanceOptionPanel.generateGetters.isSelected()));
        props.setValue("fluent.getters.prefix", advanceOptionPanel.gPrefix.getText());
        props.setValue("fluent.setters.prefix", advanceOptionPanel.prefix.getText());
        super.doOKAction();
    }

    // -------------------------- OTHER METHODS --------------------------

    @Override
    protected JComponent createSouthPanel() {
        advanceOptionPanel = new AdvanceOptionPanel();
        advanceOptionPanel.add(super.createSouthPanel(),
                new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(10, 0, 0, 0), 0, 0));
        setOKActionEnabled(true);

        return advanceOptionPanel;
    }

    public String getSetterPrefix() {
        return this.advanceOptionPanel.getSetterPrefix();
    }

    public boolean generateGetters() {
        return this.advanceOptionPanel.generateGetters();
    }

    public String getGetterPrefix() {
        return this.advanceOptionPanel.getGetterPrefix();
    }

// -------------------------- INNER CLASSES --------------------------

    protected class AdvanceOptionPanel extends JPanel {
        private final JTextField prefix;
        private final JTextField gPrefix;
        private final JCheckBox generateGetters;

        public String getSetterPrefix() {
            return prefix.getText().replaceAll("\\s", "");
        }

        public boolean generateGetters() {
            return this.generateGetters.isSelected();
        }

        public AdvanceOptionPanel() {
            super(new GridBagLayout());

            generateGetters = new JCheckBox("generate getters");
            generateGetters.setMnemonic(KeyEvent.VK_G);

            generateGetters.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    gPrefix.setEnabled(generateGetters.isSelected());
                }
            });


            prefix = new JTextField();

            JLabel label = new JLabel("setters prefix:");
            label.setDisplayedMnemonic(KeyEvent.VK_P);
            label.setLabelFor(prefix);


            gPrefix = new JTextField();

            JLabel gLabel = new JLabel("getters prefix:");
            gLabel.setLabelFor(gPrefix);

            final Insets insets = new Insets(5, 0, 0, 0);

            add(generateGetters, new GridBagConstraints(
                    0, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                    insets, 0, 0));
            add(label, new GridBagConstraints(1, 0, 1, 1, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                    insets, 0, 0));
            add(prefix, new GridBagConstraints(2, 0, 1, 1, 0.8, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    insets, 20, 0));

            add(gLabel, new GridBagConstraints(1, 1, 1, 1, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                    insets, 0, 0));
            add(gPrefix, new GridBagConstraints(2, 1, 1, 1, 0.8, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    insets, 20, 0));

        }

        public String getGetterPrefix() {
            if (generateGetters())
                return gPrefix.getText().replaceAll("\\s", "");
            else
                return "";
        }
    }
}

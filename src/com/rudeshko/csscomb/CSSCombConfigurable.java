package com.rudeshko.csscomb;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ScrollPaneFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by: Anton Rudeshko
 * Date: 22.01.13 at 23:29
 */
public class CSSCombConfigurable implements Configurable {

    private final Project project;
    private String cssOrder;
    private JTextArea textArea;

    private JPanel panel;

    public CSSCombConfigurable(Project project) {
        this.project = project;
        cssOrder = CssOrder.getCssOrder(project);

        initUi();
    }

    private void initUi() {
        textArea = new JTextArea(cssOrder);
        textArea.setRows(10);
        textArea.setColumns(30);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(IdeBorderFactory.createTitledBorder("CSS Order", false, new Insets(0, 0, 0, 0)));
        panel.add(ScrollPaneFactory.createScrollPane(textArea));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "CSSComb";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return !textArea.getText().equals(cssOrder);
    }

    @Override
    public void apply() throws ConfigurationException {
        CssOrder.setCssOrder(project, cssOrder = textArea.getText());
    }

    @Override
    public void reset() {
        textArea.setText(CssOrder.DEFAULT_ORDER);
    }

    @Override
    public void disposeUIResources() {

    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }
}

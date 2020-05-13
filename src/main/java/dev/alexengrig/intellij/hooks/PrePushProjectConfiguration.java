package dev.alexengrig.intellij.hooks;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrePushProjectConfiguration implements SearchableConfigurable, Configurable.NoScroll {
    private final Project project;
    private JPanel rootPane;
    private JComboBox<String> comboBox;

    public PrePushProjectConfiguration(Project project) {
        this.project = project;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Pre-Push Hook";
    }

    @Override
    public @Nullable JComponent createComponent() {
        List<RunConfiguration> runConfigurations = RunManager.getInstance(project).getAllConfigurationsList();
        List<String> items = runConfigurations.stream().map(RunProfile::getName).collect(Collectors.toList());
        comboBox.removeAllItems();
        comboBox.addItem("");
        items.forEach(comboBox::addItem);
        String selectedItem = getRunConfigurationName();
        if (items.contains(selectedItem)) {
            comboBox.setSelectedItem(selectedItem);
        }
        return rootPane;
    }

    @Override
    public boolean isModified() {
        String selected = getSelectedRunConfigurationName();
        String current = getRunConfigurationName();
        return !Objects.equals(selected, current);
    }

    @Override
    public void apply() {
        String selected = getSelectedRunConfigurationName();
        setRunConfigurationName(selected);
    }

    @Override
    public void reset() {
        String selected = getRunConfigurationName();
        comboBox.setSelectedItem(selected);
    }

    private String getRunConfigurationName() {
        return MyPrePushService.getInstance(project).getRunConfigurationName();
    }

    private void setRunConfigurationName(String runConfigurationName) {
        MyPrePushService.getInstance(project).setRunConfigurationName(runConfigurationName);
    }

    private String getSelectedRunConfigurationName() {
        return (String) comboBox.getSelectedItem();
    }

    @Override
    public @NotNull String getId() {
        return "PrePushSettings";
    }
}

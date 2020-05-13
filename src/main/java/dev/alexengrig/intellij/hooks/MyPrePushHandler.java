package dev.alexengrig.intellij.hooks;

import com.intellij.dvcs.push.PushInfo;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MyPrePushHandler implements com.intellij.dvcs.push.PrePushHandler {
    private final Project myProject;

    public MyPrePushHandler(Project myProject) {
        this.myProject = myProject;
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getPresentableName() {
        return "My Pre Push";
    }

    @Override
    public @NotNull Result handle(@NotNull List<PushInfo> pushDetails, @NotNull ProgressIndicator indicator) {
        String runConfigurationName = getRunConfigurationName();
        RunnerAndConfigurationSettings configuration = getRunConfigurationByName(runConfigurationName);
        try {
            SwingUtilities.invokeAndWait(() -> {
                //FIXME: don't work with opened push modal dialog
                ProgramRunnerUtil.executeConfiguration(configuration, DefaultRunExecutor.getRunExecutorInstance());
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Result.ABORT;
    }

    private String getRunConfigurationName() {
        return MyPrePushService.getInstance(myProject).getRunConfigurationName();
    }

    private RunnerAndConfigurationSettings getRunConfigurationByName(String runConfigurationName) {
        return RunManager.getInstance(myProject).findConfigurationByName(runConfigurationName);
    }
}

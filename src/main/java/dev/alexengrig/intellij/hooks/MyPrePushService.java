package dev.alexengrig.intellij.hooks;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "MyPrePushService", storages = {@Storage("myPrePushServiceStorage.xml")})
public class MyPrePushService implements PersistentStateComponent<MyPrePushService.State> {
    private State myState = new State();

    public static MyPrePushService getInstance(Project project) {
        return ServiceManager.getService(project, MyPrePushService.class);
    }

    public String getRunConfigurationName() {
        return myState.runConfigurationName;
    }

    public void setRunConfigurationName(String runConfigurationName) {
        this.myState.runConfigurationName = runConfigurationName;
    }

    @Nullable
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    static class State {
        public String runConfigurationName = "";
    }
}

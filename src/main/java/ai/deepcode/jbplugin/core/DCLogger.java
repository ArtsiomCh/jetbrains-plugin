package ai.deepcode.jbplugin.core;

import ai.deepcode.javaclient.core.DCLoggerBase;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.slf4j.LoggerFactory;

public class DCLogger extends DCLoggerBase {

  private static final DCLogger INSTANCE = new DCLogger();

  public static DCLogger getInstance() {
    return INSTANCE;
  }

  private DCLogger() {
    super(
        () -> LoggerFactory.getLogger("DeepCode")::info,
        () -> LoggerFactory.getLogger("DeepCode")::warn,
        () -> LoggerFactory.getLogger("DeepCode").isInfoEnabled(),
        () -> LoggerFactory.getLogger("DeepCode").isWarnEnabled());
  }

  @Override
  protected String getExtraInfo() {
    String currentThread = " [" + Thread.currentThread().getName() + "] ";

    final Application application = ApplicationManager.getApplication();
    String rwAccess = (application.isReadAccessAllowed() ? "R" : "-");
    rwAccess += (application.isWriteAccessAllowed() ? "W" : "-");

    // fixme presume we work with one project only
    final Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
    String mode =
            (openProjects.length != 0)
                    ? DumbService.getInstance(openProjects[0]).isDumb() ? "D" : "S"
                    : "X";

    final ProgressIndicator currentProgressIndicator =
            ProgressManager.getInstance().getProgressIndicator();
    String progressIndicator =
            (currentProgressIndicator == null)
                    ? ""
                    : "\n" + "ProgressIndicator [" + currentProgressIndicator.toString() + "]";

    return rwAccess + mode + currentThread + progressIndicator;
  }
}

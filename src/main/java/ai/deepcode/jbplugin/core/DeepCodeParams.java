package ai.deepcode.jbplugin.core;

import ai.deepcode.javaclient.DeepCodeRestApi;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DeepCodeParams {

  // TODO https://www.jetbrains.org/intellij/sdk/docs/basics/persisting_sensitive_data.html
  private static final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

  private DeepCodeParams() {}

  // Settings
  private static boolean isEnable;
  private static String apiUrl;
  private static boolean useLinter;
  private static int minSeverity;
  private static String sessionToken;

  // Inner params
  private static String loginUrl;
  private static final String ideProductName = ApplicationNamesInfo.getInstance().getProductName();

  public static void clearLoginParams() {
    setSessionToken("");
    setLoginUrl("");
  }

  @NotNull
  public static String getSessionToken() {
    return sessionToken;
  }

  public static void setSessionToken(String sessionToken) {
    DeepCodeParams.sessionToken = sessionToken;
    propertiesComponent.setValue("sessionToken", sessionToken);
  }

  @NotNull
  public static String getLoginUrl() {
    return loginUrl;
  }

  public static void setLoginUrl(String loginUrl) {
    DeepCodeParams.loginUrl = loginUrl;
    propertiesComponent.setValue("loginUrl", loginUrl);
  }

  public static boolean useLinter() {
    return useLinter;
  }

  public static void setUseLinter(boolean useLinter) {
    DeepCodeParams.useLinter = useLinter;
    propertiesComponent.setValue("useLinter", useLinter);
  }

  public static int getMinSeverity() {
    return minSeverity;
  }

  public static void setMinSeverity(int minSeverity) {
    DeepCodeParams.minSeverity = minSeverity;
    propertiesComponent.setValue("minSeverity", String.valueOf(minSeverity));
  }

  @NotNull
  public static String getApiUrl() {
    return apiUrl;
  }

  public static void setApiUrl(@NotNull String apiUrl) {
    if (apiUrl.isEmpty()) apiUrl = "https://www.deepcode.ai/";
    if (!apiUrl.endsWith("/")) apiUrl += "/";
    if (apiUrl.equals(DeepCodeParams.apiUrl)) return;
    DeepCodeParams.apiUrl = apiUrl;
    propertiesComponent.setValue("apiUrl", apiUrl);
    DeepCodeRestApi.setBaseUrl(apiUrl);
  }

  public static boolean isEnable() {
    return isEnable;
  }

  public static void setEnable(boolean isEnable) {
    DeepCodeParams.isEnable = isEnable;
    propertiesComponent.setValue("isEnable", isEnable);
  }

  public static boolean consentGiven(@NotNull Project project) {
    return PropertiesComponent.getInstance(project).getBoolean("consentGiven", false);
  }

  public static void setConsentGiven(@NotNull Project project) {
    PropertiesComponent.getInstance(project).setValue("consentGiven", true);
  }

  static {
    isEnable = propertiesComponent.getBoolean("isEnable", true);
    apiUrl = propertiesComponent.getValue("apiUrl", "https://www.deepcode.ai/");
    DeepCodeRestApi.setBaseUrl(apiUrl);
    String pastIdeProductName = propertiesComponent.getValue("ideProductName", "");
    if (pastIdeProductName.equals(ideProductName)) {
      sessionToken = propertiesComponent.getValue("sessionToken", "");
      loginUrl = propertiesComponent.getValue("loginUrl", "");
    } else {
      clearLoginParams();
      propertiesComponent.setValue("ideProductName", ideProductName);
    }
    useLinter = propertiesComponent.getBoolean("useLinter", false);
    minSeverity = propertiesComponent.getInt("minSeverity", 1);
  }
}

package mobi.eyeline.ips.properties

class DefaultMockConfig extends FailingMockConfig {
    String getSmtpHost() { '' }
    int getSmtpPort() { 0 }
    String getSmtpUsername() { '' }
    String getSmtpPassword() { '' }
    String getMailFrom() { '' }
    String getLoginUrl() { '' }

    Properties getDatabaseProperties() { new Properties() }

    boolean isMadvUpdateEnabled() { false }
    int getMadvUpdateDelayMinutes() { 1 }
    String getMadvUrl() { '' }
    String getMadvUserLogin() { '' }
    String getMadvUserPassword() { '' }

    String getSadsPushUrl() { '' }
    String getSadsSmsPushUrl() { '' }
    int getSadsMaxSessions() { 1 }
    String getBaseSurveyUrl() { '' }

    List<LocationProperties> getLocationProperties() { null }

    String getDeliveryUssdPushUrl() { '' }
    String getDeliveryNIPushUrl() { '' }

    int getPushThreadsNumber() { 2 }
    int getMessageQueueBaseline() { 10 }
    int getStateUpdateBatchSize() { 0 }
    int getRetryAttempts() { 3 }
    long getExpirationDelaySeconds() { 600 }

    boolean getExposeJmxBeans() { false }
}
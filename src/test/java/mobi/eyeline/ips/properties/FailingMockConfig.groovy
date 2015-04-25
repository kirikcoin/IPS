package mobi.eyeline.ips.properties

import static junit.framework.TestCase.fail

@SuppressWarnings("GroovyMissingReturnStatement")
class FailingMockConfig implements Config {
  String getSmtpHost() { fail() }
  int getSmtpPort() { fail() }
  String getSmtpUsername() { fail() }
  String getSmtpPassword() { fail() }
  String getMailFrom() { fail() }
  String getLoginUrl() { fail() }

  Properties getDatabaseProperties() { fail() }

  boolean isMadvUpdateEnabled() { fail() }
  int getMadvUpdateDelayMinutes() { fail() }
  String getMadvUrl() { fail() }
  String getMadvUserLogin() { fail() }
  String getMadvUserPassword() { fail() }

  int getSadsMaxSessions() { fail() }
  String getBaseSurveyUrl() { fail() }

  int getPushThreadsNumber() { fail() }
  int getMessageQueueBaseline() { fail() }
  int getStateUpdateBatchSize() { fail() }
  int getRetryAttempts() { fail() }
  long getSentExpirationDelaySeconds() { fail() }
  long getFetchedExpirationDelaySeconds() { fail() }

  String getEsdpEndpointUrl() { fail() }

  boolean isJmxEnabled() { fail() }
  String getJmxHost() { fail() }
  int getJmxPort() { fail() }
}

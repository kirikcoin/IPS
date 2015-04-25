package mobi.eyeline.ips.service;


import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.repository.AccessNumberRepository;
import mobi.eyeline.ips.repository.AnswerRepository;
import mobi.eyeline.ips.repository.DB;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.repository.ExtLinkPageRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.repository.PageRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyInvitationRepository;
import mobi.eyeline.ips.repository.SurveyPatternRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import mobi.eyeline.ips.repository.UserRepository;
import mobi.eyeline.ips.service.deliveries.DeliveryService;
import mobi.eyeline.ips.service.deliveries.NotificationService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Service lookup.
 */
@SuppressWarnings("FieldCanBeLocal")
@ApplicationScoped
public class Services {

  private static ServicesImpl instance;

  public static synchronized Services initialize(ServicesImpl impl) {
    if (instance != null) {
      throw new AssertionError("Instance is already initialized");
    }

    instance = impl;
    instance.start();

    return getInstance();
  }

  public static Services getInstance() {
    if (instance == null) {
      throw new AssertionError("Instance is not initialized");
    }

    return new Services();
  }

  public static void shutdown() {
    instance.shutdown();
  }

  @Produces
  public DB getDb() {
    return instance.getDb();
  }

  @Produces
  public UserRepository getUserRepository() {
    return instance.getUserRepository();
  }

  @Produces
  public RespondentRepository getRespondentRepository() {
    return instance.getRespondentRepository();
  }

  @Produces
  public SurveyStatsRepository getSurveyStatsRepository() {
    return instance.getSurveyStatsRepository();
  }

  @Produces
  public SurveyRepository getSurveyRepository() {
    return instance.getSurveyRepository();
  }

  @Produces
  public QuestionRepository getQuestionRepository() {
    return instance.getQuestionRepository();
  }

  @Produces
  public ExtLinkPageRepository getExtLinkPageRepository() {
    return instance.getExtLinkPageRepository();
  }

  @Produces
  public PageRepository getPageRepository() {
    return instance.getPageRepository();
  }

  @Produces
  public QuestionOptionRepository getQuestionOptionRepository() {
    return instance.getQuestionOptionRepository();
  }

  @Produces
  public AnswerRepository getAnswerRepository() {
    return instance.getAnswerRepository();
  }

  @Produces
  public SurveyInvitationRepository getSurveyInvitationRepository() {
    return instance.getSurveyInvitationRepository();
  }

  @Produces
  public InvitationDeliveryRepository getInvitationDeliveryRepository() {
    return instance.getInvitationDeliveryRepository();
  }

  @Produces
  public DeliverySubscriberRepository getDeliverySubscriberRepository() {
    return instance.getDeliverySubscriberRepository();
  }

  @Produces
  public SurveyPatternRepository getSurveyPatternRepository() {
    return instance.getSurveyPatternRepository();
  }

  @Produces
  public AccessNumberRepository getAccessNumberRepository() {
    return instance.getAccessNumberRepository();
  }

  @Produces
  public MadvSoapApi getMadvSoapApi() {
    return instance.getMadvSoapApi();
  }

  @Produces
  public MadvService getMadvService() {
    return instance.getMadvService();
  }

  @Produces
  public SurveyService getSurveyService() {
    return instance.getSurveyService();
  }

  @Produces
  public MailService getMailService() {
    return instance.getMailService();
  }

  @Produces
  public EsdpServiceSupport getEsdpServiceSupport() {
    return instance.getEsdpServiceSupport();
  }

  @Produces
  public UserService getUserService() {
    return instance.getUserService();
  }

  @Produces
  public CouponService getCouponService() {
    return instance.getCouponService();
  }

  @Produces
  public UssdService getUssdService() {
    return instance.getUssdService();
  }

  @Produces
  public EsdpService getEsdpService() {
    return instance.getEsdpService();
  }

  @Produces
  public MadvUpdateService getMadvUpdateService() {
    return instance.getMadvUpdateService();
  }

  @Produces
  public PushService getPushService() {
    return instance.getPushService();
  }

  @Produces
  @Named
  public SegmentationService getSegmentationService() {
    return instance.getSegmentationService();
  }

  @Produces
  public ResultsExportService getResultsExportService() {
    return instance.getResultsExportService();
  }

  @Produces
  public DeliveryService getDeliveryService() {
    return instance.getDeliveryService();
  }

  @Produces
  public NotificationService getNotificationService() {
    return instance.getNotificationService();
  }

  @Produces
  public CsvParseService getCsvParseService() {
    return instance.getCsvParseService();
  }

  @Produces
  public TimeZoneService getTimeZoneService() {
    return instance.getTimeZoneService();
  }
}
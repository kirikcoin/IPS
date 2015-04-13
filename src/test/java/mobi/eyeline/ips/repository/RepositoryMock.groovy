package mobi.eyeline.ips.repository

/**
 * As repository layer entities are designed to have no dependencies or initialization logic,
 * it's generally easier to provide all those objects with a single mixin.
 */
class RepositoryMock {

  //
  //  All repository classes.
  //

  UserRepository userRepository
  RespondentRepository respondentRepository
  SurveyStatsRepository surveyStatsRepository
  SurveyRepository surveyRepository
  QuestionRepository questionRepository
  QuestionOptionRepository questionOptionRepository
  AnswerRepository answerRepository
  SurveyInvitationRepository surveyInvitationRepository
  InvitationDeliveryRepository invitationDeliveryRepository
  DeliverySubscriberRepository deliverySubscriberRepository
  SurveyPatternRepository surveyPatternRepository
  AccessNumberRepository accessNumberRepository

  void initRepository(db) {
    userRepository = new UserRepository(db)
    respondentRepository = new RespondentRepository(db)
    questionRepository = new QuestionRepository(db)
    surveyStatsRepository = new SurveyStatsRepository(db)
    surveyRepository = new SurveyRepository(db)
    questionOptionRepository = new QuestionOptionRepository(db)
    answerRepository = new AnswerRepository(db)
    surveyInvitationRepository = new SurveyInvitationRepository(db)
    invitationDeliveryRepository = new InvitationDeliveryRepository(db)
    deliverySubscriberRepository = new DeliverySubscriberRepository(db)
    surveyPatternRepository = new SurveyPatternRepository(db)
    accessNumberRepository = new AccessNumberRepository(db)
  }
}

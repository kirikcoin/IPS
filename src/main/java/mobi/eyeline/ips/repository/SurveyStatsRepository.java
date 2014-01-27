package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.DistributionChannel;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyStats;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyStatsRepository extends BaseRepository<SurveyStats, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SurveyStatsRepository.class);

    public SurveyStatsRepository(DB db) {
        super(db);
    }

    public void incRegistered(Session session, int surveyId) {
        final Survey survey = (Survey) session.load(Survey.class, surveyId);

        SurveyStats stats = (SurveyStats) session.get(SurveyStats.class, survey);
        if (stats == null) {
            stats = new SurveyStats();
            stats.setSurvey(survey);
        }

        stats.setRegisteredRespondentsCount(stats.getRegisteredRespondentsCount() + 1);

        session.saveOrUpdate(stats);
    }

    public void incAnswered(Session session, int surveyId) {
        final Survey survey = (Survey) session.load(Survey.class, surveyId);

        SurveyStats stats = (SurveyStats) session.get(SurveyStats.class, survey);
        if (stats == null) {
            stats = new SurveyStats();
            stats.setSurvey(survey);
        }
        stats.setAnsweredUser(stats.getAnsweredUser() + 1);

        session.saveOrUpdate(stats);
    }

    private void update(Session session,
                        int surveyId,
                        int incRegisteredRespondentsCount,
                        int addAnswerCount,
                        Integer sent,
                        DistributionChannel channel,
                        String campaign,
                        String accessNumber) {

        final Survey survey = (Survey) session.load(Survey.class, surveyId);

        SurveyStats stats = survey.getStatistics();
        if (stats == null) {
            stats = new SurveyStats();
            survey.setStatistics(stats);
            stats.setSurvey(survey);
        }

        if (channel != null) {
            stats.setChannel(channel);
        }

        if (campaign != null) {
            stats.setCampaign(campaign);
        }

        // TODO: why not increment sentCount like registered and answered fields?
        if (sent != null) {
            stats.setSentCount(sent);
        }

        stats.setAccessNumber(accessNumber);

        stats.setRegisteredRespondentsCount(stats.getRegisteredRespondentsCount() + incRegisteredRespondentsCount);
        stats.setAnsweredUser(stats.getAnsweredUser() + addAnswerCount);

        session.update(survey);
    }

    public void update(int surveyId,
                       int incRegisteredRespondentsCount,
                       int addAnswerCount,
                       Integer sent,
                       DistributionChannel channel,
                       String campaign,
                       String accessNumber) {

        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            update(session,
                    surveyId,
                    incRegisteredRespondentsCount, addAnswerCount, sent, channel, campaign, accessNumber);

            transaction.commit();

        } catch (HibernateException e) {
            if ((transaction != null) && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (HibernateException ex) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public void updateSurveyChannel(int surveyId,
                                    DistributionChannel channel,
                                    String campaign,
                                    String accessNumber) {

        update(surveyId, 0, 0, null, channel, campaign, accessNumber);
    }

    public void updateSent(Survey survey,
                           int sent) {

        update(survey.getId(), 0, 0, sent, null, null, null);
    }

    public void delete(Session session, Survey survey) {
        session.createQuery("delete SurveyStats where survey = :survey")
                .setEntity("survey",survey)
                .executeUpdate();
    }

}

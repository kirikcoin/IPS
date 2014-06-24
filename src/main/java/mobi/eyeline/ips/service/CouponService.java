package mobi.eyeline.ips.service;

import com.google.common.util.concurrent.Striped;
import mobi.eyeline.ips.generators.GeneratorBuilder;
import mobi.eyeline.ips.generators.SequenceGenerator;
import mobi.eyeline.ips.generators.UnsupportedPatternException;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyDetails;
import mobi.eyeline.ips.model.SurveyPattern;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.repository.SurveyPatternRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

import static mobi.eyeline.ips.util.PatternUtil.asRegex;

public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private final Striped<Lock> generatorLocks = Striped.lazyWeakLock(Integer.MAX_VALUE);

    private final SurveyPatternRepository surveyPatternRepository;
    private final MailService mailService;

    public CouponService(SurveyPatternRepository surveyPatternRepository,
                         MailService mailService) {
        this.surveyPatternRepository = surveyPatternRepository;
        this.mailService = mailService;
    }

    private SequenceGenerator createGenerator(Survey survey) {

        final SurveyPattern active = survey.getActivePattern();
        if (active == null) {
            return null;
        }

        try {
            final GeneratorBuilder builder =
                    new GeneratorBuilder(asRegex(active.getMode(), active.getLength()));

            for (SurveyPattern prev : survey.getInactivePatterns()) {
                if (prev.getPosition() != 0) {
                    builder.exclude(asRegex(prev.getMode(), prev.getLength()));
                }
            }

            return builder.build(active.getPosition());

        } catch (UnsupportedPatternException e) {
            // Patterns are pre-defined and immutable.
            throw new RuntimeException(e);
        }
    }

    public String getCouponTag() {
        return "[coupon]";
    }

    public boolean hasCouponSupport(Survey survey) {
        return survey.getActivePattern() != null;
    }

    public long getAvailable(Survey survey) {
        assert hasCouponSupport(survey);

        return createGenerator(survey).getRemaining();
    }

    public int getPercentAvailable(Survey survey) {
        final SequenceGenerator generator = createGenerator(survey);
        return (generator.getRemaining() == 0) ?
                0 : ((int) (generator.getTotal() / generator.getRemaining()) * 100);
    }

    public boolean shouldGenerateCoupon(Survey survey) {
        final SurveyDetails details = survey.getDetails();
        return details.isEndSmsEnabled() &&
                details.getEndSmsText() != null &&
                details.getEndSmsText().contains(getCouponTag()) &&
                hasCouponSupport(survey);
    }

    public CharSequence generate(Survey survey) {
        final Lock lock = generatorLocks.get(survey.getId());
        try {
            lock.lock();

            return generate0(survey);

        } finally {
            lock.unlock();
        }
    }

    private CharSequence generate0(Survey survey) {
        final SequenceGenerator generator = createGenerator(survey);
        final CharSequence coupon = generator.next();
        if (coupon == null) {
            return null;
        }

        final SurveyPattern pattern = survey.getActivePattern();
        pattern.setPosition(generator.getCurrentPosition());
        surveyPatternRepository.update(pattern);

        notifyIfNeeded(survey, generator);

        if (logger.isTraceEnabled()) {
            logger.trace("Generated: survey = [" + survey + "], coupon = [" + coupon + "]");
        }

        return coupon;
    }

    private void notifyIfNeeded(Survey survey,
                                SequenceGenerator generator) {

        final long remaining = generator.getRemaining();
        final double percentAvailable = (double) generator.getTotal() / remaining;

        if ((percentAvailable <= 20) && (percentAvailable > 10)) {
            notifyOnRemaining(survey, 20, remaining);

        } else if ((percentAvailable <= 10) && (percentAvailable > 5)) {
            notifyOnRemaining(survey, 10, remaining);

        } else if ((percentAvailable <= 5) && (percentAvailable > 1)) {
            notifyOnRemaining(survey, 5, remaining);

        } else if ((percentAvailable <= 1) && (remaining != 0)) {
            notifyOnRemaining(survey, 1, remaining);

        } else if (remaining == 0) {
            notifyOnExhaustion(survey);

        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("No notification," +
                        " survey = [" + survey + "]," +
                        " total = [" + generator.getTotal() + "]," +
                        " remaining = [" + remaining + "]");
            }
        }
    }

    private void notifyOnRemaining(Survey survey, int percent, long remaining) {
        final User manager = survey.getOwner();
        final User client = survey.getClient();

        logger.info("Notifying on coupons usage," +
                " survey = [" + survey + "]," +
                " percent = [" + percent + "]," +
                " remaining = [" + remaining + "]");

        if (manager != null) {
            mailService.sendCouponRemaining(manager, survey, percent, remaining);
        }

        if (client != null) {
            mailService.sendCouponRemaining(client, survey, percent, remaining);
        }
    }

    private void notifyOnExhaustion(Survey survey) {
        final User manager = survey.getOwner();
        final User client = survey.getClient();

        logger.warn("Notifying on coupons exhaustion," +
                " survey = [" + survey + "]");

        if (manager != null) {
            mailService.sendCouponExhaustion(manager, survey);
        }

        if (client != null) {
            mailService.sendCouponExhaustion(client, survey);
        }
    }
}

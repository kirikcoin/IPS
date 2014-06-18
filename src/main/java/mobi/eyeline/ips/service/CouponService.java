package mobi.eyeline.ips.service;

import com.google.common.util.concurrent.Striped;
import mobi.eyeline.ips.generators.GeneratorBuilder;
import mobi.eyeline.ips.generators.SequenceGenerator;
import mobi.eyeline.ips.generators.UnsupportedPatternException;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyDetails;
import mobi.eyeline.ips.model.SurveyPattern;
import mobi.eyeline.ips.repository.SurveyPatternRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

import static mobi.eyeline.ips.model.SurveyPattern.Mode;
import static mobi.eyeline.ips.util.PatternUtil.asRegex;

public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private final Striped<Lock> generatorLocks = Striped.lazyWeakLock(Integer.MAX_VALUE);

    private final SurveyPatternRepository surveyPatternRepository;

    public CouponService(SurveyPatternRepository surveyPatternRepository) {
        this.surveyPatternRepository = surveyPatternRepository;
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

    public SequenceGenerator getGenerator(Survey survey) {
        return createGenerator(survey);
    }

    private SequenceGenerator createGenerator(Survey survey, Mode mode, int length) {
        try {
            final GeneratorBuilder builder = new GeneratorBuilder(asRegex(mode, length));

            for (SurveyPattern prev : survey.getPatterns()) {
                if (prev.getPosition() != 0) {
                    builder.exclude(asRegex(prev.getMode(), prev.getLength()));
                }
            }

            return builder.build();

        } catch (UnsupportedPatternException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canSwitchTo(Survey survey, Mode mode, int length) {
        final SequenceGenerator generator = createGenerator(survey);
        if (generator == null) {
            return true;
        }

        try {
            final GeneratorBuilder builder = new GeneratorBuilder(generator);
            builder.exclude(asRegex(mode, length));

            final SequenceGenerator newGen = builder.build();
            return newGen.getAvailable() > 0;

        } catch (UnsupportedPatternException e) {
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

        return createGenerator(survey).getAvailable();
    }

    public long getTotal(Survey survey) {
        assert hasCouponSupport(survey);

        return createGenerator(survey).getTotal();
    }

    public long getTotal(Survey survey, Mode mode, int length) {
        return createGenerator(survey, mode, length).getTotal();
    }

    public boolean shouldGenerateCoupon(Survey survey) {
        final SurveyDetails details = survey.getDetails();
        return details.isEndSmsEnabled() &&
                details.getEndSmsText() != null &&
                details.getEndSmsText().contains(getCouponTag()) &&
                hasCouponSupport(survey);
    }

    public CharSequence genAndPersist(Survey survey) {
        final Lock lock = generatorLocks.get(survey.getId());
        try {
            lock.lock();

            final SequenceGenerator generator = getGenerator(survey);
            final CharSequence coupon = generator.next();
            if (coupon == null) {
                return null;
            }

            final SurveyPattern pattern = survey.getActivePattern();
            pattern.setPosition(generator.getCurrentPosition());
            surveyPatternRepository.update(pattern);

            return coupon;

        } finally {
            lock.unlock();
        }
    }

}

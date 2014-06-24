package mobi.eyeline.ips.service;

import com.j256.simplejmx.common.JmxOperation;
import com.j256.simplejmx.common.JmxResource;
import mobi.eyeline.ips.external.EsdpSoapApi;
import mobi.eyeline.ips.external.esdp.EsdpServiceException;
import mobi.eyeline.ips.external.esdp.EsdpServiceManager;
import mobi.eyeline.ips.external.esdp.Service;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.j256.simplejmx.common.JmxOperationInfo.OperationAction.ACTION;

@JmxResource(domainName = "mobi.eyeline.ips")
public class EsdpService {

    private static final Logger logger = LoggerFactory.getLogger(EsdpService.class);

    private final Config config;
    private final UssdService ussdService;

    public EsdpService(Config config,
                       UssdService ussdService) {
        this.config = config;
        this.ussdService = ussdService;
    }

    public void save(Survey survey) throws EsdpServiceException {
        logger.debug("Saving service, survey = [" + survey + "]");

        final Service service = new Service();

        service.setTag(getTag(survey));
        service.setProviderId("ips");
        service.setId(getId(survey));
        service.setTitle(survey.getDetails().getTitle());

        service.setProperties(new Service.Properties());

        final List<Service.Properties.Entry> entries = service.getProperties().getEntry();
        entries.add(entry("service-enabled", "true"));
        entries.add(entry("scenario.default", "default"));
        entries.add(entry("description", survey.getDetails().getTitle()));
        entries.add(entry("use-method-post", "false"));
        entries.add(entry("start-page", ussdService.getSurveyUrl(survey)));

        getApi().createService(service);
    }

    public void delete(Survey survey) throws EsdpServiceException {
        logger.debug("Deleting service, survey = [" + survey + "]");

        getApi().deleteService(getKey(survey));
    }

    public void update(Survey survey) throws EsdpServiceException {
        logger.debug("Updating service, survey = [" + survey + "]");

        final Service service = getApi().getService(getKey(survey));

        // Title.
        service.setTitle(survey.getDetails().getTitle());

        // C2S-number.
        final String number = survey.getStatistics().getAccessNumber() == null ?
                "" : survey.getStatistics().getAccessNumber().getNumber();
        final List<Service.Properties.Entry> entries = service.getProperties().getEntry();

        Service.Properties.Entry sip = find(entries, "sip-number");
        if (sip == null) {
            sip = entry("sip-number", number);
            entries.add(sip);
        } else {
            sip.setValue(number);
        }

        getApi().updateService(service);
    }

    // TODO: for a one-time usage, should probably be removed
    //       when migrated to the new service creation scheme.
    @JmxOperation(operationAction = ACTION)
    public void createMissingServices() {
        logger.info("Creating missing services for all the surveys");

        for (Survey survey : Services.instance().getSurveyRepository().list()) {
            try {
                logger.info("Survey ID: " + survey.getId());
                if (!survey.isActive()) {
                    logger.info("Survey is deleted");
                    continue;
                }

                if (!hasService(survey)) {
                    save(survey);

                } else {
                    logger.info("Survey already has a service, ID = " + survey.getId());
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private boolean hasService(Survey survey) {
        try {
            return getApi().getService(getKey(survey)) != null;
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return false;
        }
    }


    //
    //
    //

    private EsdpServiceManager getApi() {
        return new EsdpSoapApi(config).getSoapApi();
    }

    private String getId(Survey survey) {
        return "ips" + String.format("%04d", survey.getId());
    }

    private String getTag(Survey survey) {
        return String.format("ips-%04d-%04d", survey.getOwner().getId(), survey.getId());
    }

    private String getKey(Survey survey) {
        return "ips." + getTag(survey);
    }

    private Service.Properties.Entry entry(String key, String value) {
        final Service.Properties.Entry entry = new Service.Properties.Entry();
        entry.setKey(key);
        entry.setValue(value);
        return entry;
    }

    private Service.Properties.Entry find(List<Service.Properties.Entry> entries, String key) {
        for (Service.Properties.Entry entry : entries) {
            if (key.equals(entry.getKey())) {
                return entry;
            }
        }
        return null;
    }
}

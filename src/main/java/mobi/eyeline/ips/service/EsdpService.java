package mobi.eyeline.ips.service;

import com.j256.simplejmx.common.JmxOperation;
import com.j256.simplejmx.common.JmxResource;
import mobi.eyeline.ips.external.EsdpSoapApi;
import mobi.eyeline.ips.external.esdp.EsdpServiceException;
import mobi.eyeline.ips.external.esdp.EsdpServiceManager;
import mobi.eyeline.ips.external.esdp.Service;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.util.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.j256.simplejmx.common.JmxOperationInfo.OperationAction.ACTION;

@JmxResource(domainName = "mobi.eyeline.ips")
public class EsdpService {

    private static final Logger logger = LoggerFactory.getLogger(EsdpService.class);

    private final Config config;
    private final UssdService ussdService;
    private final EsdpServiceSupport esdpServiceSupport;

    public EsdpService(Config config,
                       UssdService ussdService,
                       EsdpServiceSupport esdpServiceSupport) {
        this.config = config;
        this.ussdService = ussdService;
        this.esdpServiceSupport = esdpServiceSupport;
    }

    public void save(User user, Survey survey) throws EsdpServiceException {
        logger.debug("Saving service, survey = [" + survey + "]");

        final Service service = new Service();

        service.setTag(esdpServiceSupport.getTag(survey));
        service.setProviderId("ips");
        service.setId(esdpServiceSupport.getId(survey));
        service.setTitle(survey.getDetails().getTitle());

        service.setProperties(new Service.Properties());

        final List<Service.Properties.Entry> entries = service.getProperties().getEntry();
        entries.add(entry("service-enabled", "true"));
        entries.add(entry("scenario.default", "default"));
        entries.add(entry("description", survey.getDetails().getTitle()));
        entries.add(entry("use-method-post", "false"));
        entries.add(entry("start-page", ussdService.getSurveyUrl(survey)));
        entries.add(entry("inform-url", config.getBaseSurveyUrl() + "/inform"));

        getApi(user.getEsdpLogin(), user.getEsdpPasswordHash()).createService(service);
    }

    public void delete(User user, Survey survey) throws EsdpServiceException {
        logger.debug("Deleting service, survey = [" + survey + "]");

        getApi(user.getEsdpLogin(), user.getEsdpPasswordHash()).deleteService(esdpServiceSupport.getKey(survey));
    }

    public void update(User user, Survey survey) throws EsdpServiceException {
        logger.debug("Updating service, survey = [" + survey + "]");

        final Service service = getApi(user.getEsdpLogin(), user.getEsdpPasswordHash())
                .getService(esdpServiceSupport.getKey(survey));

        // Title.
        service.setTitle(survey.getDetails().getTitle());

        // C2S-number.
        final String number = survey.getStatistics().getAccessNumber() == null ?
                "" : survey.getStatistics().getAccessNumber().getNumber();

        if (service.getProperties() == null) {
            service.setProperties(new Service.Properties());
        }
        final List<Service.Properties.Entry> entries = service.getProperties().getEntry();

        Service.Properties.Entry sip = find(entries, "sip-number");
        if (sip == null) {
            sip = entry("sip-number", number);
            entries.add(sip);
        } else {
            sip.setValue(number);
        }

        getApi(user.getEsdpLogin(), user.getEsdpPasswordHash())
                .updateService(service);
    }

    // TODO: for a one-time usage, should probably be removed
    //       when migrated to the new service creation scheme.
    @SuppressWarnings("UnusedDeclaration")
    @JmxOperation(operationAction = ACTION)
    public void createMissingServices() {
        logger.info("Creating missing services for all the surveys");

        final String login = "ips";
        final String pwHash;
        try {
            // Note: ESDP console generates hashes in lowercase and performs case-sensitive lookup.
            pwHash = HashUtils.hash("password", "MD5", "UTF-8").toLowerCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        for (Survey survey : Services.instance().getSurveyRepository().list()) {
            try {
                logger.info("Survey ID: " + survey.getId());
                if (!survey.isActive()) {
                    logger.info("Survey is deleted");
                    continue;
                }

                if (!hasService(login, pwHash, survey)) {
                    save(new User() {{
                        setEsdpLogin(login);
                        setEsdpPasswordHash(pwHash);
                    }}, survey);

                } else {
                    logger.info("Survey already has a service, ID = " + survey.getId());
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

/*
    @SuppressWarnings("UnusedDeclaration")
    @JmxOperation(operationAction = ACTION)
    public void addInformUrls() {
        logger.info("Updating inform URL for all the surveys");

        final String login = "ips";
        final String pwHash;
        try {
            // Note: ESDP console generates hashes in lowercase and performs case-sensitive lookup.
            pwHash = HashUtils.hash("password", "MD5", "UTF-8").toLowerCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        for (Survey survey : Services.instance().getSurveyRepository().list()) {
            try {
                logger.info("Survey ID: " + survey.getId());
                if (!survey.isActive()) {
                    logger.info("Survey is deleted");
                    continue;
                }

                if (hasService(login, pwHash, survey)) {
                    final Service service = getApi(login, pwHash)
                            .getService(esdpServiceSupport.getKey(survey));

                    if (service.getProperties() == null) {
                        service.setProperties(new Service.Properties());
                    }
                    final List<Service.Properties.Entry> entries = service.getProperties().getEntry();

                    Service.Properties.Entry inform = find(entries, "inform-url");
                    if (inform == null) {
                        inform = entry("inform-url", config.getBaseSurveyUrl() + "/inform");
                        entries.add(inform);
                    } else {
                        inform.setValue(config.getBaseSurveyUrl() + "/inform");
                    }

                    getApi(login, pwHash).updateService(service);
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
*/

    private boolean hasService(String login, String passwordHash, Survey survey) {
        try {
            return getApi(login, passwordHash).getService(esdpServiceSupport.getKey(survey)) != null;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return false;
        }
    }


    //
    //
    //

    protected EsdpServiceManager getApi(String login, String passwordHash) {
        return new EsdpSoapApi(config, login, passwordHash).getSoapApi();
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

package mobi.eyeline.ips.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static java.text.MessageFormat.format;
import static java.util.ResourceBundle.getBundle;

/**
 * Thread-safe.
 */
public class TemplateService {

    // Using the same encoding for templates and generated content.
    private static final String ENCODING = "UTF-8";

    // All the known templates, used for eager loading
    // to provoke any validation errors on initialization.
    private static final String[] TEMPLATE_NAMES = new String[] {
            "templates/mail-user-registration.ftl",
            "templates/mail-password-restore.ftl",
            "templates/mail-user-deactivation.ftl",
            "templates/mail-survey-coupon-remaining.ftl",
            "templates/mail-survey-coupon-none.ftl"
    };

    private final Configuration configuration;

    private final String loginUrl;

    public TemplateService(String loginUrl) {
        this.loginUrl = loginUrl;

        configuration = new Configuration() {{
            setOutputEncoding(ENCODING);
            setClassForTemplateLoading(TemplateService.class, "/");
        }};

        // Loaded templates are actually cached in the configuration, so reloading
        // doesn't bring in much overhead.
        for (String templateName : TEMPLATE_NAMES) {
            loadTemplate(templateName);
        }
    }

    /**
     * Loads template by name from classpath.
     *
     * @param templatePath Path to the template for classpath search,
     *                     e.g. {@code pkg0/pkg1/template.ftl}
     */
    protected Template loadTemplate(String templatePath) {
        try {
            return configuration.getTemplate(templatePath, ENCODING);
        } catch (IOException e) {
            throw new RuntimeException("Template not found for URI: " + templatePath, e);
        }
    }

    protected String processTemplate(Template template, Map<String, Object> data) {
        try {
            final Writer writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();

        } catch (TemplateException e) {
            throw new RuntimeException("Invalid template: " + template.getName(), e);

        } catch (IOException e) {
            throw new RuntimeException("Error during form generation using template: " +
                    template.getName(), e);
        }
    }

    private Map<String, String> bundle2map(ResourceBundle bundle) {
        final Map<String, String> map = new HashMap<>();

        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, bundle.getString(key));
        }

        return map;
    }

    protected String processEmailTemplate(Locale locale, Template template, Map<String, Object> data) {
        final Map<String, Object> newParams = new HashMap<>(data);
        newParams.put("bundle", bundle2map(getBundle("email", locale)));
        return processTemplate(template, newParams);
    }

    //
    //  Email templates.
    //


    public String formatUserRegistration(final User user,
                                         final String rawPassword) {
        final Template template = loadTemplate("templates/mail-user-registration.ftl");

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("password", rawPassword);
            put("loginUrl", loginUrl);
        }};

        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }

    public String formatUserModified(final User user) {
        final Template template = loadTemplate("templates/mail-user-modified.ftl");

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("loginUrl", loginUrl);
        }};

        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }

    public String formatUserDeactivation(final User user) {
        final Template template = loadTemplate("templates/mail-user-deactivation.ftl");

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("loginUrl", loginUrl);
        }};

        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }

    public String formatUserActivation(final User user) {
        final Template template = loadTemplate("templates/mail-user-activation.ftl");

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("loginUrl", loginUrl);
        }};

        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }

    public String formatPasswordRestore(final User user,
                                        final String rawNewPassword) {
        final Template template = loadTemplate("templates/mail-password-restore.ftl");

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("password", rawNewPassword);
            put("loginUrl", loginUrl);
        }};

        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }

    public String formatCouponRemaining(final User user,
                                        final Survey survey,
                                        final int percentRemaining,
                                        final long remaining) {
        final Template template = loadTemplate("templates/mail-survey-coupon-remaining.ftl");

        final String messageTemplate = getBundle("email", user.getLocale().asLocale())
                .getString("email.user.coupon.remaining.text");
        final String message = format(messageTemplate,
                survey.getDetails().getTitle(),
                percentRemaining,
                remaining);

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("message", message);
        }};

        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }

    public String formatCouponExhaustion(final User user, final Survey survey) {
        final Template template = loadTemplate("templates/mail-survey-coupon-none.ftl");

        final String messageTemplate = getBundle("email", user.getLocale().asLocale())
                .getString("email.user.coupon.empty.text");
        final String message = format(messageTemplate, survey.getDetails().getTitle());

        final Map<String, Object> data = new HashMap<String, Object>() {{
            put("user", user);
            put("message", message);
        }};
        return processEmailTemplate(user.getLocale().asLocale(), template, data);
    }


}

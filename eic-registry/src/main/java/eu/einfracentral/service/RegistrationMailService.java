package eu.einfracentral.service;

import eu.einfracentral.domain.*;
import eu.einfracentral.exception.ResourceNotFoundException;
import eu.einfracentral.registry.manager.InfraServiceManager;
import eu.einfracentral.registry.manager.PendingProviderManager;
import eu.einfracentral.registry.manager.PendingServiceManager;
import eu.einfracentral.registry.manager.ProviderManager;
import eu.openminted.registry.core.domain.FacetFilter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RegistrationMailService {

    private static final Logger logger = LogManager.getLogger(RegistrationMailService.class);
    private final MailService mailService;
    private final Configuration cfg;
    private final ProviderManager providerManager;
    private final PendingProviderManager pendingProviderManager;
    private final InfraServiceManager infraServiceManager;
    private final PendingServiceManager pendingServiceManager;
    private final SecurityService securityService;


    @Value("${webapp.homepage}")
    private String endpoint;

    @Value("${project.debug:false}")
    private boolean debug;

    @Value("${project.name:CatRIS}")
    private String projectName;

    @Value("${project.registration.email:registration@catris.eu}")
    private String registrationEmail;

    private final List<String> projectAdmins;


    @Autowired
    public RegistrationMailService(MailService mailService, Configuration cfg,
                                   ProviderManager providerManager,
                                   @Lazy PendingProviderManager pendingProviderManager,
                                   InfraServiceManager infraServiceManager,
                                   PendingServiceManager pendingServiceManager,
                                   SecurityService securityService,
                                   @Value("${project.admins}") String admins) {
        this.mailService = mailService;
        this.cfg = cfg;
        this.providerManager = providerManager;
        this.pendingProviderManager = pendingProviderManager;
        this.infraServiceManager = infraServiceManager;
        this.pendingServiceManager = pendingServiceManager;
        this.securityService = securityService;
        this.projectAdmins = Arrays.asList(admins.split("\\s*\\,\\s*"));
    }

    @Async
    public void sendProviderMails(ProviderBundle providerBundle) {
        Map<String, Object> root = new HashMap<>();
        StringWriter out = new StringWriter();
        String providerMail;
        String regTeamMail;

        String providerSubject;
        String regTeamSubject;

        if (providerBundle == null || providerBundle.getProvider() == null) {
            throw new ResourceNotFoundException("Provider is null");
        }

        List<Service> serviceList = providerManager.getServices(providerBundle.getId());
        Service serviceTemplate = null;
        if (!serviceList.isEmpty()) {
            root.put("service", serviceList.get(0));
            serviceTemplate = serviceList.get(0);
        } else {
            serviceTemplate = new Service();
            serviceTemplate.setName("");
        }

        providerSubject = getProviderSubject(providerBundle, serviceTemplate);
        regTeamSubject = getRegTeamSubject(providerBundle, serviceTemplate);

        root.put("providerBundle", providerBundle);
        root.put("endpoint", endpoint);
        root.put("project", projectName);
        root.put("registrationEmail", registrationEmail);
        // get the first user's information for the registration team email
        root.put("user", providerBundle.getProvider().getUsers().get(0));

        try {
            Template temp = cfg.getTemplate("registrationTeamMailTemplate.ftl");
            temp.process(root, out);
            regTeamMail = out.getBuffer().toString();
            if (!debug) {
                mailService.sendMail(registrationEmail, regTeamSubject, regTeamMail);
            }
            logger.info("\nRecipient: {}\nTitle: {}\nMail body: \n{}", registrationEmail,
                    regTeamSubject, regTeamMail);

            temp = cfg.getTemplate("providerMailTemplate.ftl");
            for (User user : providerBundle.getProvider().getUsers()) {
                if (user.getEmail() == null || user.getEmail().equals("")) {
                    continue;
                }
                root.remove("user");
                out.getBuffer().setLength(0);
                root.put("user", user);
                root.put("project", projectName);
                temp.process(root, out);
                providerMail = out.getBuffer().toString();
                if (!debug) {
                    mailService.sendMail(user.getEmail(), providerSubject, providerMail);
                }
                logger.info("\nRecipient: {}\nTitle: {}\nMail body: \n{}", user.getEmail(), providerSubject, providerMail);
            }

            out.close();
        } catch (IOException e) {
            logger.error("Error finding mail template", e);
        } catch (TemplateException e) {
            logger.error("ERROR", e);
        } catch (MessagingException e) {
            logger.error("Could not send mail", e);
        }
    }

    @Scheduled(cron = "0 0 12 ? * 2/7") // At 12:00:00pm, every 7 days starting on Monday, every month
    public void sendEmailNotificationsToProviders() {
        FacetFilter ff = new FacetFilter();
        ff.setQuantity(10000);
        List<ProviderBundle> activeProviders = providerManager.getAll(ff, securityService.getAdminAccess()).getResults();
        List<ProviderBundle> pendingProviders = pendingProviderManager.getAll(ff, securityService.getAdminAccess()).getResults();
        List<ProviderBundle> allProviders = Stream.concat(activeProviders.stream(), pendingProviders.stream()).collect(Collectors.toList());

        Map<String, Object> root = new HashMap<>();
        root.put("project", projectName);
        root.put("endpoint", endpoint);

        for (ProviderBundle providerBundle : allProviders) {
            if (providerBundle.getStatus().equals(Provider.States.ST_SUBMISSION.getKey())) {
                if (providerBundle.getProvider().getUsers() == null || providerBundle.getProvider().getUsers().isEmpty()) {
                    continue;
                }
                String subject = String.format("[%s] Friendly reminder for your Provider [%s]", projectName, providerBundle.getProvider().getName());
                root.put("providerBundle", providerBundle);
                for (User user : providerBundle.getProvider().getUsers()) {
                    root.put("user", user);
                    sendMailsFromTemplate("providerOnboarding.ftl", root, subject, user.getEmail());
                }
            }
        }
    }

    @Scheduled(cron = "0 0 12 ? * 2/2") // At 12:00:00pm, every 2 days starting on Monday, every month
    public void sendEmailNotificationsToAdmins() {
        FacetFilter ff = new FacetFilter();
        ff.setQuantity(10000);
        List<ProviderBundle> allProviders = providerManager.getAll(ff, null).getResults();

        List<String> providersWaitingForInitialApproval = new ArrayList<>();
        List<String> providersWaitingForSTApproval = new ArrayList<>();
        for (ProviderBundle providerBundle : allProviders) {
            if (providerBundle.getStatus().equals(Provider.States.PENDING_1.getKey())) {
                providersWaitingForInitialApproval.add(providerBundle.getProvider().getName());
            }
            if (providerBundle.getStatus().equals(Provider.States.PENDING_2.getKey())) {
                providersWaitingForSTApproval.add(providerBundle.getProvider().getName());
            }
        }

        Map<String, Object> root = new HashMap<>();
        root.put("project", projectName);
        root.put("endpoint", endpoint);
        root.put("iaProviders", providersWaitingForInitialApproval);
        root.put("stProviders", providersWaitingForSTApproval);

        String subject = String.format("[%s] Some new Providers are pending for your approval", projectName);
        if (!providersWaitingForInitialApproval.isEmpty() || !providersWaitingForSTApproval.isEmpty()) {
            List<String> recipients = new ArrayList<>(projectAdmins);
            recipients.add(registrationEmail);
            sendMailsFromTemplate("adminOnboardingDigest.ftl", root, subject, recipients);
        }
    }

    @Scheduled(cron = "0 0 12 ? * *") // At 12:00:00pm every day
    public void dailyNotificationsToAdmins() {
        // Create timestamps for today and yesterday
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Timestamp todayTimestamp = Timestamp.valueOf(today.atStartOfDay());
        Timestamp yesterdayTimestamp = Timestamp.valueOf(yesterday.atStartOfDay());

        List<String> newProviders = new ArrayList<>();
        List<String> newServices = new ArrayList<>();
        List<String> updatedProviders = new ArrayList<>();
        List<String> updatedServices = new ArrayList<>();

        // Fetch Active/Pending Services and Active/Pending Providers
        FacetFilter ff = new FacetFilter();
        ff.setQuantity(10000);
        List<ProviderBundle> activeProviders = providerManager.getAll(ff, securityService.getAdminAccess()).getResults();
        List<ProviderBundle> pendingProviders = pendingProviderManager.getAll(ff, securityService.getAdminAccess()).getResults();
        List<InfraService> activeServices = infraServiceManager.getAll(ff, securityService.getAdminAccess()).getResults();
        List<InfraService> pendingServices = pendingServiceManager.getAll(ff, securityService.getAdminAccess()).getResults();
        List<ProviderBundle> allProviders = Stream.concat(activeProviders.stream(), pendingProviders.stream()).collect(Collectors.toList());
        List<InfraService> allServices = Stream.concat(activeServices.stream(), pendingServices.stream()).collect(Collectors.toList());
        List<Bundle> allResources = Stream.concat(allProviders.stream(), allServices.stream()).collect(Collectors.toList());

        for (Bundle bundle : allResources) {
            Timestamp modified;
            Timestamp registered;
            if (bundle.getMetadata() != null) {
                if (bundle.getMetadata().getModifiedAt() == null || !bundle.getMetadata().getModifiedAt().matches("[0-9]+")) {
                    modified = new Timestamp(Long.parseLong("0"));
                } else {
                    modified = new Timestamp(Long.parseLong(bundle.getMetadata().getModifiedAt()));
                }
                if (bundle.getMetadata().getRegisteredAt() == null || !bundle.getMetadata().getRegisteredAt().matches("[0-9]+")) {
                    registered = new Timestamp(Long.parseLong("0"));
                } else {
                    registered = new Timestamp(Long.parseLong(bundle.getMetadata().getRegisteredAt()));
                }
            } else {
                continue;
            }

            if (modified.after(yesterdayTimestamp) && modified.before(todayTimestamp)) {
                if (bundle.getId().contains(".")) {
                    updatedServices.add(bundle.getId());
                } else {
                    updatedProviders.add(bundle.getId());
                }
            }
            if (registered.after(yesterdayTimestamp) && registered.before(todayTimestamp)) {
                if (bundle.getId().contains(".")) {
                    newServices.add(bundle.getId());
                } else {
                    newProviders.add(bundle.getId());
                }
            }
        }

        boolean changes = true;
        if (newProviders.isEmpty() && updatedProviders.isEmpty() && newServices.isEmpty() && updatedServices.isEmpty()) {
            changes = false;
        }

        Map<String, Object> root = new HashMap<>();
        root.put("changes", changes);
        root.put("project", projectName);
        root.put("newProviders", newProviders);
        root.put("updatedProviders", updatedProviders);
        root.put("newServices", newServices);
        root.put("updatedServices", updatedServices);

        String subject = String.format("[%s] Daily Notification - Changes to Resources", projectName);
        List<String> recipients = new ArrayList<>(projectAdmins);
        recipients.add(registrationEmail);
        sendMailsFromTemplate("adminDailyDigest.ftl", root, subject, recipients);
    }

    private void sendMailsFromTemplate(String templateName, Map<String, Object> root, String subject, String email) {
        sendMailsFromTemplate(templateName, root, subject, Collections.singletonList(email));
    }

    private void sendMailsFromTemplate(String templateName, Map<String, Object> root, String subject, List<String> emails) {
        if (emails == null || emails.isEmpty()) {
            logger.error("emails empty or null");
            return;
        }
        try (StringWriter out = new StringWriter()) {
            Template temp = cfg.getTemplate(templateName);
            temp.process(root, out);
            String mailBody = out.getBuffer().toString();

            if (!debug) {
                mailService.sendMail(emails, subject, mailBody);
            }
            logger.info("\nRecipients: {}\nTitle: {}\nMail body: \n{}", String.join(", ", emails), subject, mailBody);

        } catch (IOException e) {
            logger.error("Error finding mail template '{}'", templateName, e);
        } catch (TemplateException e) {
            logger.error("ERROR", e);
        } catch (MessagingException e) {
            logger.error("Could not send mail", e);
        }
    }

    private String getProviderSubject(ProviderBundle providerBundle, Service serviceTemplate) {
        if (providerBundle == null || providerBundle.getProvider() == null) {
            logger.error("Provider is null");
            return String.format("[%s]", this.projectName);
        }

        String subject;
        String providerName = providerBundle.getProvider().getName();

        switch (Provider.States.fromString(providerBundle.getStatus())) {
            case PENDING_1:
                subject = String.format("[%s] Your application for registering [%s] " +
                        "as a new service provider has been received", this.projectName, providerName);
                break;
            case ST_SUBMISSION:
                subject = String.format("[%s] The information you submitted for the new service provider " +
                        "[%s] has been approved - the submission of a first service is required " +
                        "to complete the registration process", this.projectName, providerName);
                break;
            case REJECTED:
                subject = String.format("[%s] Your application for registering [%s] " +
                        "as a new service provider has been rejected", this.projectName, providerName);
                break;
            case PENDING_2:
                assert serviceTemplate != null;
                subject = String.format("[%s] Your service [%s] has been received " +
                        "and its approval is pending", projectName, serviceTemplate.getName());
                break;
            case APPROVED:
                if (providerBundle.isActive()) {
                    assert serviceTemplate != null;
                    subject = String.format("[%s] Your service [%s] – [%s]  has been accepted",
                            projectName, providerName, serviceTemplate.getName());
                    break;
                } else {
                    assert serviceTemplate != null;
                    subject = String.format("[%s] Your service provider [%s] has been set to inactive",
                            projectName, providerName);
                    break;
                }
            case REJECTED_ST:
                assert serviceTemplate != null;
                subject = String.format("[%s] Your service [%s] – [%s]  has been rejected",
                        projectName, providerName, serviceTemplate.getName());
                break;
            default:
                subject = String.format("[%s] Provider Registration", this.projectName);
        }

        return subject;
    }


    private String getRegTeamSubject(ProviderBundle providerBundle, Service serviceTemplate) {
        if (providerBundle == null || providerBundle.getProvider() == null) {
            logger.error("Provider is null");
            return String.format("[%s]", this.projectName);
        }

        String subject;
        String providerName = providerBundle.getProvider().getName();

        switch (Provider.States.fromString(providerBundle.getStatus())) {
            case PENDING_1:
                subject = String.format("[%s] A new application for registering [%s] " +
                        "as a new service provider has been submitted", this.projectName, providerName);
                break;
            case ST_SUBMISSION:
                subject = String.format("[%s] The application of [%s] for registering " +
                        "as a new service provider has been accepted", this.projectName, providerName);
                break;
            case REJECTED:
                subject = String.format("[%s] The application of [%s] for registering " +
                        "as a new service provider has been rejected", this.projectName, providerName);
                break;
            case PENDING_2:
                assert serviceTemplate != null;
                subject = String.format("[%s] Approve or reject the information about the new service: " +
                        "[%s] – [%s]", projectName, providerBundle.getProvider().getName(), serviceTemplate.getName());
                break;
            case APPROVED:
                if (providerBundle.isActive()) {
                    assert serviceTemplate != null;
                    subject = String.format("[%s] The service [%s] has been accepted",
                            projectName, serviceTemplate.getId());
                    break;
                } else {
                    assert serviceTemplate != null;
                    subject = String.format("[%s] The service provider [%s] has been set to inactive",
                            projectName, providerName);
                    break;
                }
            case REJECTED_ST:
                assert serviceTemplate != null;
                subject = String.format("[%s] The service [%s] has been rejected",
                        projectName, serviceTemplate.getId());
                break;
            default:
                subject = String.format("[%s] Provider Registration", this.projectName);
        }

        return subject;
    }
}

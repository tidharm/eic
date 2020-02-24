package eu.einfracentral.service;

import eu.einfracentral.domain.*;
import eu.einfracentral.exception.ResourceNotFoundException;
import eu.einfracentral.registry.manager.ProviderManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Component
public class RegistrationMailService {

    private static final Logger logger = LogManager.getLogger(RegistrationMailService.class);
    private MailService mailService;
    private Configuration cfg;
    private ProviderManager providerManager;


    @Value("${webapp.front:portal.catris.eu}")
    private String endpoint;

    @Value("${project.debug:false}")
    private boolean debug;

    @Value("${project.name:CatRIS}")
    private String projectName;

    @Value("${project.registration.email:registration@catris.eu}")
    private String registrationEmail;


    @Autowired
    public RegistrationMailService(MailService mailService, Configuration cfg,
                                   ProviderManager providerManager) {
        this.mailService = mailService;
        this.cfg = cfg;
        this.providerManager = providerManager;
    }

    @Async
    public void sendProviderMails(ProviderBundle providerBundle) {
        Map<String, Object> root = new HashMap<>();
        StringWriter out = new StringWriter();
        String providerMail;
        String regTeamMail;

        String providerSubject = null;
        String regTeamSubject = null;

        String providerName;
        if (providerBundle != null && providerBundle.getProvider() != null) {
            providerName = providerBundle.getProvider().getName();
        } else {
            throw new ResourceNotFoundException("Provider is null");
        }

        List<Service> serviceList = providerManager.getServices(providerBundle.getId());
        Service serviceTemplate = null;
        if (!serviceList.isEmpty()) {
            serviceTemplate = serviceList.get(0);
            root.put("service", serviceTemplate);
        } else {
            try {
                serviceTemplate = providerManager.getDeactivatedServices(providerBundle.getProvider().getId()).get(0);
                root.put("service", serviceTemplate);
            } catch (IndexOutOfBoundsException e) {
                serviceTemplate = new Service();
                serviceTemplate.setName("");
            }
        }
        switch (Provider.State.fromString(providerBundle.getProviderState())) {
            case PENDING_1:
                providerSubject = String.format("[%s] Your application for registering [%s] " +
                        "as a new service provider has been received", projectName, providerName);
                regTeamSubject = String.format("[%s] A new application for registering [%s] " +
                        "as a new service provider has been submitted", projectName, providerName);
                break;
            case ST_SUBMISSION:
                providerSubject = String.format("[%s] The information you submitted for the new service provider " +
                        "[%s] has been approved - the submission of a first service is required " +
                        "to complete the registration process", projectName, providerName);
                regTeamSubject = String.format("[%s] The application of [%s] for registering " +
                        "as a new service provider has been accepted", projectName, providerName);
                break;
            case PENDING_2:
                providerSubject = String.format("[%s] Your service [%s] has been received " +
                        "and its approval is pending", projectName, serviceTemplate.getName());
                regTeamSubject = String.format("[%s] Approve or reject the information about the new service: " +
                        "[%s] – [%s]", projectName, providerBundle.getProvider().getName(), serviceTemplate.getName());
                break;
            case APPROVED:
                if (providerBundle.getStatus().equals(Bundle.StatusType.PUBLISHED.getKey())) {
                    assert serviceTemplate != null;
                    providerSubject = String.format("[%s] Your service [%s] – [%s]  has been accepted",
                            projectName, providerName, serviceTemplate.getName());
                    regTeamSubject = String.format("[%s] The service [%s] has been accepted",
                            projectName, serviceTemplate.getId());
                    break;
                } else {
                    assert serviceTemplate != null;
                    providerSubject = String.format("[%s] Your service provider [%s] has been set to inactive",
                            projectName, providerName);
                    regTeamSubject = String.format("[%s] The service provider [%s] has been set to inactive",
                            projectName, providerName);
                    break;
                }
            case REJECTED_ST:
                assert serviceTemplate != null;
                providerSubject = String.format("[%s] Your service [%s] – [%s]  has been rejected",
                        projectName, providerName, serviceTemplate.getName());
                regTeamSubject = String.format("[%s] The service [%s] has been rejected",
                        projectName, serviceTemplate.getId());
                break;
            case REJECTED:
                providerSubject = String.format("[%s] Your application for registering [%s] " +
                        "as a new service provider has been rejected", projectName, providerName);
                regTeamSubject = String.format("[%s] The application of [%s] for registering " +
                        "as a new service provider has been rejected", projectName, providerName);
                break;
        }

        root.put("providerBundle", providerBundle);
        root.put("endpoint", endpoint);
        root.put("project", projectName);
        root.put("registrationEmail", registrationEmail);
        root.put("state", providerBundle.getProviderState());
        // get the first user's information for the registration team email
        root.put("user", providerBundle.getProvider().getUsers().get(0));

        try {
            Template temp = cfg.getTemplate("registrationTeamMailTemplate.ftl");
            temp.process(root, out);
            regTeamMail = out.getBuffer().toString();
            if (!debug) {
                mailService.sendMail(registrationEmail, regTeamSubject, regTeamMail);
            }
            logger.info("Recipient: {}\nTitle: {}\nMail body: \n{}", registrationEmail,
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
                logger.info("Recipient: {}\nTitle: {}\nMail body: \n{}", user.getEmail(), providerSubject, providerMail);
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
}

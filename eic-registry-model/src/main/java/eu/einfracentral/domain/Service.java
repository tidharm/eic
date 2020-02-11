package eu.einfracentral.domain;

import eu.einfracentral.annotation.FieldValidation;
import eu.einfracentral.annotation.VocabularyValidation;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@XmlType
@XmlRootElement(namespace = "http://einfracentral.eu")
public class Service implements Identifiable {


    // Service Basic Information
    /**
     * Global unique and persistent identifier of the service/resource.
     */
    @XmlElement
    @ApiModelProperty(position = 1, example = "(required on PUT only)")
    @FieldValidation
    private String id;

    /**
     * Brief and descriptive name of service/resource as assigned by the service/resource provider.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 2, example = "String (required)", required = true)
    @FieldValidation
    private String name;

    /**
     * The organisation that manages and delivers the service/resource, or the organisation which takes lead in coordinating service delivery and communicates with customers in case of a federated scenario.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 3, example = "String (required)", required = true)
    @FieldValidation(containsId = true, idClass = Provider.class)
    private String mainProvider;

    /**
     * The organisation(s) that participate in service delivery in case of a federated scenario
     */
    @XmlElementWrapper(name = "collaboratingProviders")
    @XmlElement(name = "collaboratingProvider")
    @ApiModelProperty(position = 4, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Provider.class)
    private List<String> collaboratingProviders;

    /**
     * Webpage with information about the service/resource usually hosted and maintained by the service/resource provider.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 5, example = "URL (required)", required = true)
    @FieldValidation
    private URL webpage;


    // Service Marketing Information
    /**
     * A high-level description in fairly non-technical terms of a) what the service/resource does, functionality it provides and resources it enables to access, b) the benefit to a user/customer delivered by a service; benefits are usually related to alleviating pains (e.g., eliminate undesired outcomes, obstacles or risks) or producing gains (e.g. increased performance, social gains, positive emotions or cost saving), c) list of customers, communities, users, etc. using the service.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 6, example = "String (required)", required = true)
    @FieldValidation
    private String description;

    /**
     * Short catch-phrase for marketing and advertising purposes. It will be usually displayed close the service name and should refer to the main value or purpose of the service.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 7, example = "String (optional)", required = true)
    @FieldValidation
    private String tagline;

    /**
     * Link to the logo/visual identity of the service. The logo will be visible at the Portal.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 8, example = "URL (required)", required = true)
    @FieldValidation
    private URL logo;

    /**
     * Link to video, screenshots or slides showing details of the service/resource.
     */
    @XmlElementWrapper(name = "multimediaUrls")
    @XmlElement(name = "multimediaUrl")
    @ApiModelProperty(position = 9, dataType = "List", example = "URL[] (optional)")
    @FieldValidation(nullable = true)
    private List<URL> multimediaUrls;

    /**
     * Type of users/customers that commissions a service/resource provider to deliver a service.
     */
    @XmlElementWrapper(name = "targetUsers", required = true)
    @XmlElement(name = "targetUser")
    @ApiModelProperty(position = 10, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.TARGET_USERS)
    private List<String> targetUsers;

    /**
     * Target Customer Tags.
     */
    @XmlElementWrapper(name = "targetCustomerTags")
    @XmlElement(name = "targetCustomerTag")
    @ApiModelProperty(position = 11, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> targetCustomerTags;

    /**
     * List of use cases supported by this service/resource.
     */
    @XmlElementWrapper(name = "useCases")
    @XmlElement(name = "useCase")
    @ApiModelProperty(position = 12, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> useCases;


    // Service Classification Information
    /**
     * The subbranch of science, scientific subdicipline that is related to the service/resource.
     */
    @XmlElementWrapper(name = "scientificSubdomains", required = true)
    @XmlElement(name = "scientificSubdomain")
    @ApiModelProperty(position = 13, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.SCIENTIFIC_SUBDOMAIN)
    private List<String> scientificSubdomains;

    /**
     * A named group of services/resources that offer access to the same type of resource or capabilities, within the defined service category.
     */
    @XmlElementWrapper(name = "subcategories", required = true)
    @XmlElement(name = "subcategory")
    @ApiModelProperty(position = 14, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.SUBCATEGORY)
    private List<String> subcategories;

    /**
     * Keywords associated to the service/resource to simplify search by relevant keywords.
     */
    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    @ApiModelProperty(position = 15, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> tags;


    // Service Location Information
    /**
     * Countries where the service/resource is offered.
     */
    @XmlElementWrapper(name = "geographicalAvailabilities", required = true)
    @XmlElement(name = "geographicalAvailability")
    @ApiModelProperty(position = 16, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.PLACE)
    private List<String> geographicalAvailability;

    /**
     * Languages of the user interface of the service or the resource.
     */
    @XmlElementWrapper(name = "languages", required = true)
    @XmlElement(name = "language")
    @ApiModelProperty(position = 17, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.LANGUAGE)
    private List<String> languages;


    // Service Contact Information
    /**
     * Service's main contact person / manager info.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 18, required = true)
    @FieldValidation
    private ServiceMainContact serviceMainContact;

    /**
     * List of service's contact persons info.
     */
    @XmlElementWrapper(name = "servicePublicContacts")
    @XmlElement(name = "servicePublicContact")
    @ApiModelProperty(position = 19, dataType = "List")
    @FieldValidation(nullable = true)
    private List<ServicePublicContact> servicePublicContacts;


    // Service Maturity Information
    /**
     * Phase of the service/resource lifecycle.
     */
    @XmlElement
    @ApiModelProperty(position = 20, example = "String (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PHASE)
    private String phase;

    /**
     * The Technology Readiness Level of the Tag of the service/resource.
     */
    @XmlElement
    @ApiModelProperty(position = 21, example = "String (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.TRL)
    private String trl;

    /**
     * List of certifications obtained for the service (including the certification body).
     */
    @XmlElementWrapper(name = "certifications")
    @XmlElement(name = "certification")
    @ApiModelProperty(position = 22, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> certifications;

    /**
     * List of standards supported by the service.
     */
    @XmlElementWrapper(name = "standards")
    @XmlElement(name = "standard")
    @ApiModelProperty(position = 23, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> standards;

    /**
     * List of open source technologies supported by the service.
     */
    @XmlElementWrapper(name = "openSourceTechnologies")
    @XmlElement(name = "openSourceTechnology")
    @ApiModelProperty(position = 24, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> openSourceTechnologies;

    /**
     * Version of the service/resource that is in force.
     */
    @XmlElement
    @ApiModelProperty(position = 25, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String version;

    /**
     * Date of the latest update of the service/resource.
     */
    @XmlElement
    @ApiModelProperty(position = 26, example = "XMLGregorianCalendar (optional)")
    @FieldValidation(nullable = true)
    private XMLGregorianCalendar lastUpdate;

    /**
     * Summary of the service/resource features updated from the previous version.
     */
    @XmlElement
    @ApiModelProperty(position = 27, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String changeLog;


    // Service Dependencies Information
    /**
     * List of other services/resources required with this service/resource.
     */
    @XmlElementWrapper(name = "requiredServices")
    @XmlElement(name = "requiredService")
    @ApiModelProperty(position = 28, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Service.class)
    private List<String> requiredServices;

    /**
     * List of other services/resources that are commonly used with this service/resource.
     */
    @XmlElementWrapper(name = "relatedServices")
    @XmlElement(name = "relatedService")
    @ApiModelProperty(position = 29, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Service.class)
    private List<String> relatedServices;

    /**
     * List of suites or thematic platforms in which the service/resource is engaged or providers (provider groups) contributing to this service
     */
    @XmlElementWrapper(name = "relatedPlatforms")
    @XmlElement(name = "relatedPlatform")
    @ApiModelProperty(position = 30, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> relatedPlatforms;


    // Service Attribution Information
    /**
     * Name of the funding body that supported the development and/or operation of the service.
     */
    @XmlElementWrapper(name = "funders")
    @XmlElement(name = "funder")
    @ApiModelProperty(position = 31, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Funder.class)
    private List<String> funders;

    /**
     * Name of the funding program that supported the development and/or operation of the service.
     */
    @XmlElementWrapper(name = "fundingPrograms")
    @XmlElement(name = "fundingProgram")
    @ApiModelProperty(position = 32, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> fundingPrograms;

    /**
     * Name of the project that supported the development and/or operation of the service.
     */
    @XmlElementWrapper(name = "grantProjectNames")
    @XmlElement(name = "grantProjectName")
    @ApiModelProperty(position = 33, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> grantProjectNames;


    // Service Management Information
    /**
     * The URL to a webpage with the contact person or helpdesk to ask more information from the service/resource provider about this service.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 34, example = "URL (required)", required = true)
    @FieldValidation
    private URL helpdeskWebpage;

    /**
     * Email of the heldpesk department.
     */
    @XmlElement
    @ApiModelProperty(position = 35, example = "String (required)")
    @FieldValidation(nullable = true)
    private String helpdeskEmail;

    /**
     * Link to the service/resource user manual and documentation.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 36, example = "URL (required)", required = true)
    @FieldValidation
    private URL userManual;

    /**
     * Webpage describing the rules, service/resource conditions and usage policy which one must agree to abide by in order to use the service.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 37, example = "URL (required)", required = true)
    @FieldValidation
    private URL termsOfUse;

    /**
     * Link to the privacy policy applicable to the service.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 38, example = "URL (required)", required = true)
    @FieldValidation
    private URL privacyPolicy;

    /**
     * Webpage with the information about the levels of performance that a service/resource provider is expected to deliver.
     */
    @XmlElement
    @ApiModelProperty(position = 39, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL sla;

    /**
     * Webpage to training information on the service.
     */
    @XmlElement
    @ApiModelProperty(position = 40, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL trainingInformation;

    /**
     * Webpage with monitoring information about this service.
     */
    @XmlElement
    @ApiModelProperty(position = 41, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL statusMonitoring;

    /**
     * Webpage with information about planned maintenance windows for this service.
     */
    @XmlElement
    @ApiModelProperty(position = 42, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL maintenance;


    // Service Access & Order Information
    /**
     * The way a user can access the service/resource (Remote, Physical, Virtual, etc.).
     */
    @XmlElementWrapper(name = "accessTypes")
    @XmlElement(name = "accessType")
    @ApiModelProperty(position = 43, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.ACCESS_TYPE)
    private List<String> accessTypes;

    /**
     * Eligibility/criteria for granting access to users (excellence-based, free-conditionally, free etc.)
     *
     */
    @XmlElementWrapper(name = "accessModes")
    @XmlElement(name = "accessMode")
    @ApiModelProperty(position = 44, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.ACCESS_MODE)
    private List<String> accessModes;

    /**
     * Information about the access policies that apply.
     */
    @XmlElement
    @ApiModelProperty(position = 45, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String accessPolicyDescription;

    /**
     * Declare whether the service is available to place an order for via the EOSC portal
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 46, example = "Yes or No (required)", required = true)
    @FieldValidation
    private String orderViaEoscPortal;

    /**
     * Webpage through which an order for the service can be placed
     */
    @XmlElement
    @ApiModelProperty(position = 47, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL order;

    /**
     * Email of the quotations department.
     */
    @XmlElement
    @ApiModelProperty(position = 48, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String quotation;


    // Financial Information
    /**
     * Webpage with the supported payment models and restrictions that apply to each of them.
     */
    @XmlElement
    @ApiModelProperty(position = 49, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL paymentModel;

    /**
     * Webpage with the information on the price scheme for this service in case the customer is charged for.
     */
    @XmlElement
    @ApiModelProperty(position = 50, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL pricing;


    // Service v2.00 Deprecated Information
//    /**
//     * Described id the service/resource can be accessed with an ordering process.
//     */
//    @XmlElement(required = true)
//    @ApiModelProperty(position = 30, example = "String (required)", required = true)
//    @VocabularyValidation(type = Vocabulary.Type.ORDER_TYPE)
//    private String orderType;
//
//    /**
//     * Link to the service/resource admin manual and documentation.
//     */
//    @XmlElement
//    @ApiModelProperty(position = 39, example = "URL (optional)")
//    @FieldValidation(nullable = true)
//    private URL adminManual;
//
//    /**
//     * The benefit to a user/customer delivered by a service; benefits are usually related to alleviating pains (e.g., eliminate undesired outcomes, obstacles or risks) or producing gains (e.g. increased performance, social gains, positive emotions or cost saving).
//     */
//    @XmlElement
//    @ApiModelProperty(position = 8, example = "String (optional)")
//    @FieldValidation(nullable = true)
//    private String userValue;
//
//    /**
//     * List of customers, communities, users, etc. using the service.
//     */
//    @XmlElementWrapper(name = "userBaseList")
//    @XmlElement(name = "userBase")
//    @ApiModelProperty(position = 9, dataType = "List", example = "String[] (optional)")
//    @FieldValidation(nullable = true)
//    private List<String> userBaseList;
//
//    /**
//     * High-level description of the various options or forms in which the service/resource can be instantiated.
//     */
//    @XmlElementWrapper(name = "options")
//    @XmlElement(name = "option")
//    @ApiModelProperty(position = 11)
//    @FieldValidation(nullable = true)
//    private List<ServiceOption> options;
//
//    /**
//     * Main URL to use the service (in the case of networked service).
//     */
//    @XmlElement
//    @ApiModelProperty(position = 12, example = "URL (optional)")
//    @FieldValidation(nullable = true)
//    private URL endpoint;
//
//
//    // Service Aggregator Information
//    /**
//     * Number of services offered under the record.
//     */
//    @XmlElement(defaultValue = "1")
//    @ApiModelProperty(position = 48, example = "(default = 1) (optional)")
//    private Integer aggregatedServices = 1;
//
//    /**
//     * Number of publications offered under the record.
//     */
//    @XmlElement(defaultValue = "0")
//    @ApiModelProperty(position = 49, example = "(default = 0) (optional)")
//    private Integer publications = 0;
//
//    /**
//     * Number of datasets offered under the record.
//     */
//    @XmlElement(defaultValue = "0")
//    @ApiModelProperty(position = 50, example = "(default = 0) (optional)")
//    private Integer datasets = 0;
//
//    /**
//     * Number of softwares offered under the record.
//     */
//    @XmlElement(defaultValue = "0")
//    @ApiModelProperty(position = 51, example = "(default = 0) (optional)")
//    private Integer software = 0;
//
//    /**
//     * Number of applications offered under the record.
//     */
//    @XmlElement(defaultValue = "0")
//    @ApiModelProperty(position = 52, example = "(default = 0) (optional)")
//    private Integer applications = 0;
//
//    /**
//     * Other resources offered under the record.
//     */
//    @XmlElement(defaultValue = "0")
//    @ApiModelProperty(position = 53, example = "(default = 0) (optional)")
//    private Integer otherProducts = 0;


    public Service() {
        // No arg constructor
    }

    public Service(Service service) {
        this.id = service.id;
        this.name = service.name;
        this.mainProvider = service.mainProvider;
        this.collaboratingProviders = service.collaboratingProviders;
        this.webpage = service.webpage;
        this.description = service.description;
        this.tagline = service.tagline;
        this.logo = service.logo;
        this.multimediaUrls = service.multimediaUrls;
        this.targetUsers = service.targetUsers;
        this.targetCustomerTags = service.targetCustomerTags;
        this.useCases = service.useCases;
        this.scientificSubdomains = service.scientificSubdomains;
        this.subcategories = service.subcategories;
        this.tags = service.tags;
        this.geographicalAvailability = service.geographicalAvailability;
        this.languages = service.languages;
        this.serviceMainContact = service.serviceMainContact;
        this.servicePublicContacts = service.servicePublicContacts;
        this.phase = service.phase;
        this.trl = service.trl;
        this.certifications = service.certifications;
        this.standards = service.standards;
        this.openSourceTechnologies = service.openSourceTechnologies;
        this.version = service.version;
        this.lastUpdate = service.lastUpdate;
        this.changeLog = service.changeLog;
        this.requiredServices = service.requiredServices;
        this.relatedServices = service.relatedServices;
        this.relatedPlatforms = service.relatedPlatforms;
        this.funders = service.funders;
        this.fundingPrograms = service.fundingPrograms;
        this.grantProjectNames = service.grantProjectNames;
        this.helpdeskWebpage = service.helpdeskWebpage;
        this.helpdeskEmail = service.helpdeskEmail;
        this.userManual = service.userManual;
        this.termsOfUse = service.termsOfUse;
        this.privacyPolicy = service.privacyPolicy;
        this.sla = service.sla;
        this.trainingInformation = service.trainingInformation;
        this.statusMonitoring = service.statusMonitoring;
        this.maintenance = service.maintenance;
        this.accessTypes = service.accessTypes;
        this.accessModes = service.accessModes;
        this.accessPolicyDescription = service.accessPolicyDescription;
        this.orderViaEoscPortal = service.orderViaEoscPortal;
        this.order = service.order;
        this.quotation = service.quotation;
        this.paymentModel = service.paymentModel;
        this.pricing = service.pricing;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mainProvider='" + mainProvider + '\'' +
                ", collaboratingProviders=" + collaboratingProviders +
                ", webpage=" + webpage +
                ", description='" + description + '\'' +
                ", tagline='" + tagline + '\'' +
                ", logo=" + logo +
                ", multimediaUrls=" + multimediaUrls +
                ", targetUsers=" + targetUsers +
                ", targetCustomerTags=" + targetCustomerTags +
                ", useCases=" + useCases +
                ", scientificSubdomains=" + scientificSubdomains +
                ", subcategories=" + subcategories +
                ", tags=" + tags +
                ", geographicalAvailability=" + geographicalAvailability +
                ", languages=" + languages +
                ", serviceMainContact=" + serviceMainContact +
                ", servicePublicContacts=" + servicePublicContacts +
                ", phase='" + phase + '\'' +
                ", trl='" + trl + '\'' +
                ", certifications=" + certifications +
                ", standards=" + standards +
                ", openSourceTechnologies=" + openSourceTechnologies +
                ", version='" + version + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", changeLog='" + changeLog + '\'' +
                ", requiredServices=" + requiredServices +
                ", relatedServices=" + relatedServices +
                ", relatedPlatforms=" + relatedPlatforms +
                ", funders=" + funders +
                ", fundingPrograms=" + fundingPrograms +
                ", grantProjectNames=" + grantProjectNames +
                ", helpdeskWebpage=" + helpdeskWebpage +
                ", helpdeskEmail='" + helpdeskEmail + '\'' +
                ", userManual=" + userManual +
                ", termsOfUse=" + termsOfUse +
                ", privacyPolicy=" + privacyPolicy +
                ", sla=" + sla +
                ", trainingInformation=" + trainingInformation +
                ", statusMonitoring=" + statusMonitoring +
                ", maintenance=" + maintenance +
                ", accessTypes=" + accessTypes +
                ", accessModes=" + accessModes +
                ", accessPolicyDescription='" + accessPolicyDescription + '\'' +
                ", orderViaEoscPortal='" + orderViaEoscPortal + '\'' +
                ", order=" + order +
                ", quotation='" + quotation + '\'' +
                ", paymentModel=" + paymentModel +
                ", pricing=" + pricing +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        Service service = (Service) o;
        return id.equals(service.id) &&
                name.equals(service.name) &&
                mainProvider.equals(service.mainProvider) &&
                Objects.equals(collaboratingProviders, service.collaboratingProviders) &&
                webpage.equals(service.webpage) &&
                description.equals(service.description) &&
                tagline.equals(service.tagline) &&
                logo.equals(service.logo) &&
                Objects.equals(multimediaUrls, service.multimediaUrls) &&
                targetUsers.equals(service.targetUsers) &&
                Objects.equals(targetCustomerTags, service.targetCustomerTags) &&
                Objects.equals(useCases, service.useCases) &&
                scientificSubdomains.equals(service.scientificSubdomains) &&
                subcategories.equals(service.subcategories) &&
                Objects.equals(tags, service.tags) &&
                geographicalAvailability.equals(service.geographicalAvailability) &&
                languages.equals(service.languages) &&
                serviceMainContact.equals(service.serviceMainContact) &&
                Objects.equals(servicePublicContacts, service.servicePublicContacts) &&
                Objects.equals(phase, service.phase) &&
                Objects.equals(trl, service.trl) &&
                Objects.equals(certifications, service.certifications) &&
                Objects.equals(standards, service.standards) &&
                Objects.equals(openSourceTechnologies, service.openSourceTechnologies) &&
                Objects.equals(version, service.version) &&
                Objects.equals(lastUpdate, service.lastUpdate) &&
                Objects.equals(changeLog, service.changeLog) &&
                Objects.equals(requiredServices, service.requiredServices) &&
                Objects.equals(relatedServices, service.relatedServices) &&
                Objects.equals(relatedPlatforms, service.relatedPlatforms) &&
                Objects.equals(funders, service.funders) &&
                Objects.equals(fundingPrograms, service.fundingPrograms) &&
                Objects.equals(grantProjectNames, service.grantProjectNames) &&
                helpdeskWebpage.equals(service.helpdeskWebpage) &&
                Objects.equals(helpdeskEmail, service.helpdeskEmail) &&
                userManual.equals(service.userManual) &&
                termsOfUse.equals(service.termsOfUse) &&
                privacyPolicy.equals(service.privacyPolicy) &&
                Objects.equals(sla, service.sla) &&
                Objects.equals(trainingInformation, service.trainingInformation) &&
                Objects.equals(statusMonitoring, service.statusMonitoring) &&
                Objects.equals(maintenance, service.maintenance) &&
                Objects.equals(accessTypes, service.accessTypes) &&
                Objects.equals(accessModes, service.accessModes) &&
                Objects.equals(accessPolicyDescription, service.accessPolicyDescription) &&
                orderViaEoscPortal.equals(service.orderViaEoscPortal) &&
                Objects.equals(order, service.order) &&
                Objects.equals(quotation, service.quotation) &&
                Objects.equals(paymentModel, service.paymentModel) &&
                Objects.equals(pricing, service.pricing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mainProvider, collaboratingProviders, webpage, description, tagline, logo, multimediaUrls, targetUsers, targetCustomerTags, useCases, scientificSubdomains, subcategories, tags, geographicalAvailability, languages, serviceMainContact, servicePublicContacts, phase, trl, certifications, standards, openSourceTechnologies, version, lastUpdate, changeLog, requiredServices, relatedServices, relatedPlatforms, funders, fundingPrograms, grantProjectNames, helpdeskWebpage, helpdeskEmail, userManual, termsOfUse, privacyPolicy, sla, trainingInformation, statusMonitoring, maintenance, accessTypes, accessModes, accessPolicyDescription, orderViaEoscPortal, order, quotation, paymentModel, pricing);
    }

    private boolean stringListsAreEqual(List<String> list1, List<String> list2) {
        if (stringListIsEmpty(list1) && stringListIsEmpty(list2)) {
            return true;
        }
        return Objects.equals(list1, list2);
    }

    /**
     * Method checking if a {@link List<String>} object is null or is empty or it contains only one entry
     * with an empty String ("")
     *
     * @param list
     * @return
     */
    private boolean stringListIsEmpty(List<String> list) {
        if (list == null || list.isEmpty()) {
            return true;
        } else return list.size() == 1 && "".equals(list.get(0));
    }

    public static String createId(Service service) {
        String provider = service.getMainProvider();
        return String.format("%s.%s", provider, StringUtils
                .stripAccents(service.getName())
                .replaceAll("[^a-zA-Z0-9\\s\\-\\_]+", "")
                .replace(" ", "_")
                .toLowerCase());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainProvider() {
        return mainProvider;
    }

    public void setMainProvider(String mainProvider) {
        this.mainProvider = mainProvider;
    }

    public List<String> getCollaboratingProviders() {
        return collaboratingProviders;
    }

    public void setCollaboratingProviders(List<String> collaboratingProviders) {
        this.collaboratingProviders = collaboratingProviders;
    }

    public URL getWebpage() {
        return webpage;
    }

    public void setWebpage(URL webpage) {
        this.webpage = webpage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public URL getLogo() {
        return logo;
    }

    public void setLogo(URL logo) {
        this.logo = logo;
    }

    public List<URL> getMultimediaUrls() {
        return multimediaUrls;
    }

    public void setMultimediaUrls(List<URL> multimediaUrls) {
        this.multimediaUrls = multimediaUrls;
    }

    public List<String> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(List<String> targetUsers) {
        this.targetUsers = targetUsers;
    }

    public List<String> getTargetCustomerTags() {
        return targetCustomerTags;
    }

    public void setTargetCustomerTags(List<String> targetCustomerTags) {
        this.targetCustomerTags = targetCustomerTags;
    }

    public List<String> getUseCases() {
        return useCases;
    }

    public void setUseCases(List<String> useCases) {
        this.useCases = useCases;
    }

    public List<String> getScientificSubdomains() {
        return scientificSubdomains;
    }

    public void setScientificSubdomains(List<String> scientificSubdomains) {
        this.scientificSubdomains = scientificSubdomains;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getGeographicalAvailability() {
        return geographicalAvailability;
    }

    public void setGeographicalAvailability(List<String> geographicalAvailability) {
        this.geographicalAvailability = geographicalAvailability;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public ServiceMainContact getServiceMainContact() {
        return serviceMainContact;
    }

    public void setServiceMainContact(ServiceMainContact serviceMainContact) {
        this.serviceMainContact = serviceMainContact;
    }

    public List<ServicePublicContact> getServicePublicContacts() {
        return servicePublicContacts;
    }

    public void setServicePublicContacts(List<ServicePublicContact> servicePublicContacts) {
        this.servicePublicContacts = servicePublicContacts;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getTrl() {
        return trl;
    }

    public void setTrl(String trl) {
        this.trl = trl;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<String> getStandards() {
        return standards;
    }

    public void setStandards(List<String> standards) {
        this.standards = standards;
    }

    public List<String> getOpenSourceTechnologies() {
        return openSourceTechnologies;
    }

    public void setOpenSourceTechnologies(List<String> openSourceTechnologies) {
        this.openSourceTechnologies = openSourceTechnologies;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public XMLGregorianCalendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(XMLGregorianCalendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public List<String> getRequiredServices() {
        return requiredServices;
    }

    public void setRequiredServices(List<String> requiredServices) {
        this.requiredServices = requiredServices;
    }

    public List<String> getRelatedServices() {
        return relatedServices;
    }

    public void setRelatedServices(List<String> relatedServices) {
        this.relatedServices = relatedServices;
    }

    public List<String> getRelatedPlatforms() {
        return relatedPlatforms;
    }

    public void setRelatedPlatforms(List<String> relatedPlatforms) {
        this.relatedPlatforms = relatedPlatforms;
    }

    public List<String> getFunders() {
        return funders;
    }

    public void setFunders(List<String> funders) {
        this.funders = funders;
    }

    public List<String> getFundingPrograms() {
        return fundingPrograms;
    }

    public void setFundingPrograms(List<String> fundingPrograms) {
        this.fundingPrograms = fundingPrograms;
    }

    public List<String> getGrantProjectNames() {
        return grantProjectNames;
    }

    public void setGrantProjectNames(List<String> grantProjectNames) {
        this.grantProjectNames = grantProjectNames;
    }

    public URL getHelpdeskWebpage() {
        return helpdeskWebpage;
    }

    public void setHelpdeskWebpage(URL helpdeskWebpage) {
        this.helpdeskWebpage = helpdeskWebpage;
    }

    public String getHelpdeskEmail() {
        return helpdeskEmail;
    }

    public void setHelpdeskEmail(String helpdeskEmail) {
        this.helpdeskEmail = helpdeskEmail;
    }

    public URL getUserManual() {
        return userManual;
    }

    public void setUserManual(URL userManual) {
        this.userManual = userManual;
    }

    public URL getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(URL termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public URL getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(URL privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public URL getSla() {
        return sla;
    }

    public void setSla(URL sla) {
        this.sla = sla;
    }

    public URL getTrainingInformation() {
        return trainingInformation;
    }

    public void setTrainingInformation(URL trainingInformation) {
        this.trainingInformation = trainingInformation;
    }

    public URL getStatusMonitoring() {
        return statusMonitoring;
    }

    public void setStatusMonitoring(URL statusMonitoring) {
        this.statusMonitoring = statusMonitoring;
    }

    public URL getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(URL maintenance) {
        this.maintenance = maintenance;
    }

    public List<String> getAccessTypes() {
        return accessTypes;
    }

    public void setAccessTypes(List<String> accessTypes) {
        this.accessTypes = accessTypes;
    }

    public List<String> getAccessModes() {
        return accessModes;
    }

    public void setAccessModes(List<String> accessModes) {
        this.accessModes = accessModes;
    }

    public String getAccessPolicyDescription() {
        return accessPolicyDescription;
    }

    public void setAccessPolicyDescription(String accessPolicyDescription) {
        this.accessPolicyDescription = accessPolicyDescription;
    }

    public String getOrderViaEoscPortal() {
        return orderViaEoscPortal;
    }

    public void setOrderViaEoscPortal(String orderViaEoscPortal) {
        this.orderViaEoscPortal = orderViaEoscPortal;
    }

    public URL getOrder() {
        return order;
    }

    public void setOrder(URL order) {
        this.order = order;
    }

    public String getQuotation() {
        return quotation;
    }

    public void setQuotation(String quotation) {
        this.quotation = quotation;
    }

    public URL getPaymentModel() {
        return paymentModel;
    }

    public void setPaymentModel(URL paymentModel) {
        this.paymentModel = paymentModel;
    }

    public URL getPricing() {
        return pricing;
    }

    public void setPricing(URL pricing) {
        this.pricing = pricing;
    }
}

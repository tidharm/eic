package eu.einfracentral.domain;

import eu.einfracentral.annotation.FieldValidation;
import eu.einfracentral.annotation.VocabularyValidation;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@XmlType
@XmlRootElement(namespace = "http://einfracentral.eu")
public class Provider implements Identifiable {


    // Provider Basic Information
    /**
     * Unique identifier of the provider.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 1, example = "String (required)", required = true)
//    @FieldValidation
    private String id;

    /**
     * Full Legal Name of the organisation providing/offering the service/resource and acting as main contact point. The Legal Name must correspond to the official legal name in the statute or the registration act/decree establishing the organisation.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 2, example = "String (required)", required = true)
    @FieldValidation
    private String name;

    /**
     * Acronym or abbreviation of the provider.
     */
    @XmlElement
    @ApiModelProperty(position = 3, example = "String (optional)")
    @FieldValidation
    private String acronym;

    /**
     * The legal status is usually noted in the registration act/statute of the organisation. For independent legal entities (1) - legal status of the provider. For embedded providers (2) - legal status of the hosting legal entity.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 4, example = "String (required)")
    @FieldValidation(containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_LEGAL_STATUS)
    private String legalStatus;

    /**
     * Webpage with information about the provider.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 5, example = "URL (required)", required = true)
    @FieldValidation
    private URL website;


    // Provider Marketing Information
    /**
     * The description of the provider.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 6, example = "String (required)", required = true)
    @FieldValidation
    private String description;

    /**
     * Link to the logo/visual identity of the provider.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 7, example = "URL (required)", required = true)
    @FieldValidation
    private URL logo;

    /**
     * Link to video, slideshow, photos, screenshots with details of the provider.
     */
    @XmlElementWrapper(name = "multimedia")
//    @XmlElement(name = "multimedia")
    @ApiModelProperty(position = 8, dataType = "List", example = "URL[] (optional)")
    @FieldValidation(nullable = true)
    private List<URL> multimedia;


    // Provider Classification Information
    /**
     * A named group of providers that offer access to the same type of resource or capabilities, within the defined domain.
     */
    @XmlElementWrapper(name = "scientificSubdomains", required = true)
    @XmlElement(name = "scientificSubdomain")
    @ApiModelProperty(position = 9, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_SCIENTIFIC_SUBDOMAIN)
    private List<String> scientificSubdomains;

    /**
     * Defines if the Provider is single-sited, distributed, mobile, virtual, etc.
     */
    @XmlElementWrapper(name = "types", required = true)
    @XmlElement(name = "type")
    @ApiModelProperty(position = 10, dataType = "List", example = "String[] (required)", required = true)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_TYPE)
    private List<String> types;

    /**
     * Select the affiliations, networks of the provider.
     */
    @XmlElementWrapper(name = "affiliations")
    @XmlElement(name = "affiliation")
    @ApiModelProperty(position = 11, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_AFFILIATIONS)
    private List<String> affiliations;

    /**
     * Keywords associated to the Provider to simplify search by relevant keywords.
     */
    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    @ApiModelProperty(position = 12, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> tags;


    // Provider Location Information
    /**
     * Physical location of the Provider or its coordinating centre in the case of distributed, virtual, and mobile Providers.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 13, required = true)
    @FieldValidation
    private ProviderLocation location;


    // Provider Contact Information
    /**
     * Provider's main contact person / provider manager info.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 14, required = true)
    @FieldValidation
    private ProviderMainContact mainContact;

    /**
     * List of provider's contact persons info.
     */
    @XmlElementWrapper(name = "publicContacts")
    @XmlElement(name = "publicContact")
    @ApiModelProperty(position = 15, dataType = "List")
    @FieldValidation(nullable = true)
    private List<ProviderPublicContact> publicContacts;


    // Provider Maturity Information
    /**
     * List of certifications obtained for the provider (including the certification body and any certificate number or URL if available).   NOTE this is not for certifications specific to the service, which are under Service Description.
     */
    @XmlElementWrapper(name = "certifications")
    @XmlElement(name = "certification")
    @ApiModelProperty(position = 16, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> certifications;


    // Provider Research Infrastructure Information
    /**
     * ESFRI domain classification.
     */
    @XmlElementWrapper(name = "esfriDomains")
    @XmlElement(name = "esfriDomain")
    @ApiModelProperty(position = 17, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_ESFRI_DOMAIN)
    private List<String> esfriDomains;

    /**
     * If the RI is (part of) an ESFRI project indicate how the RI participates: a) RI is node of an ESFRI project, b) RI is an ESFRI project, c) RI is an ESFRI landmark, d) RI is not an ESFRI project or landmark.
     */
    @XmlElement
    @ApiModelProperty(position = 18, example = "String (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_ESFRI_PARTICIPATION)
    private String esfriParticipation;

    /**
     * Name of the organisation/institution legally hosting (housing) the RI or its coordinating centre. A distinction is made between: (1) RIs that are self-standing and have a defined and distinct legal entity, (2) RI that are embedded into another institution which is a legal entity (such as a university, a research organisation, etc.). If (1) - name of the RI, If (2) - name of the hosting organisation.
     */
    @XmlElement
    @ApiModelProperty(position = 19, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String hostingLegalEntity;

    /**
     * Providers that are funded by several countries should list here all supporting countries (including the Coordinating country).
     */
    @XmlElementWrapper(name = "participatingCountries")
    @XmlElement(name = "participatingCountry")
    @ApiModelProperty(position = 20, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PLACE)
    private List<String> participatingCountries;

    /**
     * Current status of the RI life-cycle.
     */
    @XmlElement
    @ApiModelProperty(position = 21, example = "String (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_LIFE_CYCLE_STATUS)
    private String lifeCycleStatus;

    /**
     * Basic research, Applied research or Technological development.
     */
    @XmlElementWrapper(name = "areasOfActivity")
    @XmlElement(name = "areaOfActivity")
    @ApiModelProperty(position = 22, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_AREA_OF_ACTIVITY)
    private List<String> areasOfActivity;

    /**
     * Provider’s participation in the grand societal challenges as defined by the European Commission (Horizon 2020).
     */
    @XmlElementWrapper(name = "societalGrandChallenges")
    @XmlElement(name = "societalGrandChallenge")
    @ApiModelProperty(position = 23, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true, containsId = true, idClass = Vocabulary.class)
    @VocabularyValidation(type = Vocabulary.Type.PROVIDER_SOCIETAL_GRAND_CHALLENGES)
    private List<String> societalGrandChallenges;

    /**
     * Is the Provider featured in the national roadmap for research infrastructures.
     */
    @XmlElement
    @ApiModelProperty(position = 24, example = "Yes or No (optional)")
    @FieldValidation(nullable = true)
    private String nationalRoadmap;


    // Provider User Information
    @XmlElementWrapper(name = "users", required = true)
    @XmlElement(name = "user")
    @ApiModelProperty(position = 25, required = true)
    @FieldValidation
    private List<User> users;


    public Provider() {
    }

    public enum States {
        PENDING_1("pending initial approval"),
        ST_SUBMISSION("pending service template submission"),
        PENDING_2("pending service template approval"),
        REJECTED_ST("rejected service template"),
        APPROVED("approved"),
        REJECTED("rejected");

        private final String type;

        States(final String type) {
            this.type = type;
        }

        public String getKey() {
            return type;
        }

        /**
         * @return the Enum representation for the given string.
         * @throws IllegalArgumentException if unknown string.
         */
        public static States fromString(String s) throws IllegalArgumentException {
            return Arrays.stream(States.values())
                    .filter(v -> v.type.equals(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
        }
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", legalStatus='" + legalStatus + '\'' +
                ", website=" + website +
                ", description='" + description + '\'' +
                ", logo=" + logo +
                ", multimedia=" + multimedia +
                ", scientificSubdomains=" + scientificSubdomains +
                ", types=" + types +
                ", affiliations=" + affiliations +
                ", tags=" + tags +
                ", location=" + location +
                ", mainContact=" + mainContact +
                ", publicContacts=" + publicContacts +
                ", certifications=" + certifications +
                ", esfriDomains=" + esfriDomains +
                ", esfriParticipation='" + esfriParticipation + '\'' +
                ", hostingLegalEntity='" + hostingLegalEntity + '\'' +
                ", participatingCountries=" + participatingCountries +
                ", lifeCycleStatus='" + lifeCycleStatus + '\'' +
                ", areasOfActivity=" + areasOfActivity +
                ", societalGrandChallenges=" + societalGrandChallenges +
                ", nationalRoadmap='" + nationalRoadmap + '\'' +
                ", users=" + users +
                '}';
    }

    public static String createId(Provider provider) {
        if (provider.getId() == null || "".equals(provider.getId())) {
            if (provider.getAcronym() != null && !"".equals(provider.getAcronym())) {
                return StringUtils
                        .stripAccents(provider.getAcronym())
                        .replaceAll("[^a-zA-Z0-9\\s\\-\\_]+", "")
                        .replace(" ", "_");
            } else {
                return StringUtils
                        .stripAccents(provider.getName())
                        .replaceAll("[^a-zA-Z0-9\\s\\-\\_]+", "")
                        .replace(" ", "_");
            }
        }
        return StringUtils
                .stripAccents(provider.getId())
                .replaceAll("[^a-zA-Z0-9\\s\\-\\_]+", "")
                .replace(" ", "_");
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getLogo() {
        return logo;
    }

    public void setLogo(URL logo) {
        this.logo = logo;
    }

    public List<URL> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<URL> multimedia) {
        this.multimedia = multimedia;
    }

    public List<String> getScientificSubdomains() {
        return scientificSubdomains;
    }

    public void setScientificSubdomains(List<String> scientificSubdomains) {
        this.scientificSubdomains = scientificSubdomains;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<String> affiliations) {
        this.affiliations = affiliations;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ProviderLocation getLocation() {
        return location;
    }

    public void setLocation(ProviderLocation location) {
        this.location = location;
    }

    public ProviderMainContact getMainContact() {
        return mainContact;
    }

    public void setMainContact(ProviderMainContact mainContact) {
        this.mainContact = mainContact;
    }

    public List<ProviderPublicContact> getPublicContacts() {
        return publicContacts;
    }

    public void setPublicContacts(List<ProviderPublicContact> publicContacts) {
        this.publicContacts = publicContacts;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<String> getEsfriDomains() {
        return esfriDomains;
    }

    public void setEsfriDomains(List<String> esfriDomains) {
        this.esfriDomains = esfriDomains;
    }

    public String getEsfriParticipation() {
        return esfriParticipation;
    }

    public void setEsfriParticipation(String esfriParticipation) {
        this.esfriParticipation = esfriParticipation;
    }

    public String getHostingLegalEntity() {
        return hostingLegalEntity;
    }

    public void setHostingLegalEntity(String hostingLegalEntity) {
        this.hostingLegalEntity = hostingLegalEntity;
    }

    public List<String> getParticipatingCountries() {
        return participatingCountries;
    }

    public void setParticipatingCountries(List<String> participatingCountries) {
        this.participatingCountries = participatingCountries;
    }

    public String getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    public void setLifeCycleStatus(String lifeCycleStatus) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    public List<String> getAreasOfActivity() {
        return areasOfActivity;
    }

    public void setAreasOfActivity(List<String> areasOfActivity) {
        this.areasOfActivity = areasOfActivity;
    }

    public List<String> getSocietalGrandChallenges() {
        return societalGrandChallenges;
    }

    public void setSocietalGrandChallenges(List<String> societalGrandChallenges) {
        this.societalGrandChallenges = societalGrandChallenges;
    }

    public String getNationalRoadmap() {
        return nationalRoadmap;
    }

    public void setNationalRoadmap(String nationalRoadmap) {
        this.nationalRoadmap = nationalRoadmap;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

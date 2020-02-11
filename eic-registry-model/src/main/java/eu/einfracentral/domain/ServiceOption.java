package eu.einfracentral.domain;

import eu.einfracentral.annotation.FieldValidation;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URL;
import java.util.List;

@XmlType
@XmlRootElement(namespace = "http://einfracentral.eu")
public class ServiceOption {

    // ServiceOption Basic Information
//    /**
//     * Identifier of the service/resource option.
//     */
//    @XmlElement(required = true)
//    @ApiModelProperty(position = 1, example = "String (required)", required = true)
//    @FieldValidation
//    private String id;

    /**
     * Name of the service/resource option.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 2, example = "String (required)", required = true)
    @FieldValidation
    private String name;

    /**
     * Webpage with information about the service/resource option.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 3, example = "URL (required)", required = true)
    @FieldValidation
    private URL webpage;

    /**
     * The description of the service/resource option.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 4, example = "String (required)", required = true)
    @FieldValidation
    private String description;

    /**
     * Link to the logo/visual identity of the service provider.
     */
    @XmlElement
    @ApiModelProperty(position = 5, example = "URL (optional)")
    @FieldValidation(nullable = true)
    private URL logo;


    // ServiceOption Contact Information
    /**
     * List of option's contact persons info.
     */
    @XmlElementWrapper(name = "contacts", required = true)
    @XmlElement(name = "contact")
    @ApiModelProperty(position = 6, required = true)
    @FieldValidation
    private List<ServiceOptionContact> contacts;


    // ServiceOption Other Information
    /**
     * List of option's attributes.
     */
    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    @ApiModelProperty(position = 7, dataType = "List", example = "String[] (optional)")
    @FieldValidation(nullable = true)
    private List<String> attributes;


    public ServiceOption() {
    }

    @Override
    public String toString() {
        return "ServiceOption{" +
//                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", webpage=" + webpage +
                ", description='" + description + '\'' +
                ", logo=" + logo +
                ", contacts=" + contacts +
                ", attributes=" + attributes +
                '}';
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public URL getLogo() {
        return logo;
    }

    public void setLogo(URL logo) {
        this.logo = logo;
    }

    public List<ServiceOptionContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ServiceOptionContact> contacts) {
        this.contacts = contacts;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }
}

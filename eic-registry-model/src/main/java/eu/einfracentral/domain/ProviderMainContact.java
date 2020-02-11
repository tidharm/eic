package eu.einfracentral.domain;

import eu.einfracentral.annotation.FieldValidation;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType
@XmlRootElement(namespace = "http://einfracentral.eu")
public class ProviderMainContact {


    // Contact Basic Information
    /**
     * First Name of the provider's main contact person / provider manager.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 1, example = "String (required)", required = true)
    @FieldValidation
    private String firstName;

    /**
     * Last Name of the provider's main contact person / provider manager.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 2, example = "String (required)", required = true)
    @FieldValidation
    private String lastName;

    /**
     * Email of the provider's main contact person / provider manager.
     */
    @XmlElement(required = true)
    @ApiModelProperty(position = 3, example = "String (required)", required = true)
    @FieldValidation
    private String email;

    /**
     * Phone of the provider's main contact person / provider manager.
     */
    @XmlElement
    @ApiModelProperty(position = 4, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String phone;

    /**
     * Position of the provider's main contact person / provider manager.
     */
    @XmlElement
    @ApiModelProperty(position = 5, example = "String (optional)")
    @FieldValidation(nullable = true)
    private String position;

    public ProviderMainContact() {
    }

    public ProviderMainContact(String firstName, String lastName, String email, String phone, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

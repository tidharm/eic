package eu.einfracentral.domain;

import eu.einfracentral.annotation.FieldValidation;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.stream.Collectors;

@XmlType
@XmlRootElement(namespace = "http://einfracentral.eu")
public abstract class Bundle<T extends Identifiable> implements Identifiable {

    @ApiModelProperty(hidden = true)
    @XmlTransient
    @FieldValidation
    private T payload;

    @XmlElement(name = "metadata")
    @FieldValidation
    private Metadata metadata;

    @XmlElement
    private String status;

    public enum StatusType {
        PUBLISHED("published"),
        DEACTIVATED("deactivated"),
        DEPRECATED("deprecated"),
        DELETED("deleted");

        private final String statusType;

        StatusType(final String statusType) {
            this.statusType = statusType;
        }

        public String getKey() {
            return statusType;
        }

        /**
         * @return the Enum representation for the given string.
         * @throws IllegalArgumentException if unknown string.
         */
        public static Bundle.StatusType fromString(String s) throws IllegalArgumentException {
            return Arrays.stream(Bundle.StatusType.values())
                    .filter(v -> v.statusType.equals(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown value: " + s + " ; Valid options: "
                            + Arrays.stream(values())
                            .map(Bundle.StatusType::getKey)
                            .collect(Collectors.joining(", "))));
        }
    }


    public Bundle() {
    }

    @Override
    public String getId() {
        return payload.getId();
    }

    @Override
    public void setId(String id) {
        this.payload.setId(id);
    }

    T getPayload() {
        return payload;
    }

    void setPayload(T payload) {
        this.payload = payload;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

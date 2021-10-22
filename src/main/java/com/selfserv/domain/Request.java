package com.selfserv.domain;

import com.selfserv.domain.enumeration.RequestStatus;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Request.
 */
@Entity
@Table(name = "request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id", unique = true)
    private Long requestID;

    @Column(name = "request_type")
    private String requestType;

    @NotNull
    @Column(name = "project_info", nullable = false)
    private String projectInfo;

    @NotNull
    @Column(name = "cost_code", nullable = false)
    private String costCode;

    @NotNull
    @Min(value = 1)
    @Column(name = "environments", nullable = false)
    private Integer environments;

    @Lob
    @Column(name = "config_input", nullable = false)
    private String configInput;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Request id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestID() {
        return this.requestID;
    }

    public Request requestID(Long requestID) {
        this.setRequestID(requestID);
        return this;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public Request requestType(String requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getProjectInfo() {
        return this.projectInfo;
    }

    public Request projectInfo(String projectInfo) {
        this.setProjectInfo(projectInfo);
        return this;
    }

    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }

    public String getCostCode() {
        return this.costCode;
    }

    public Request costCode(String costCode) {
        this.setCostCode(costCode);
        return this;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public Integer getEnvironments() {
        return this.environments;
    }

    public Request environments(Integer environments) {
        this.setEnvironments(environments);
        return this;
    }

    public void setEnvironments(Integer environments) {
        this.environments = environments;
    }

    public String getConfigInput() {
        return this.configInput;
    }

    public Request configInput(String configInput) {
        this.setConfigInput(configInput);
        return this;
    }

    public void setConfigInput(String configInput) {
        this.configInput = configInput;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public Request status(RequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }
        return id != null && id.equals(((Request) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Request{" +
            "id=" + getId() +
            ", requestID=" + getRequestID() +
            ", requestType='" + getRequestType() + "'" +
            ", projectInfo='" + getProjectInfo() + "'" +
            ", costCode='" + getCostCode() + "'" +
            ", environments=" + getEnvironments() +
            ", configInput='" + getConfigInput() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

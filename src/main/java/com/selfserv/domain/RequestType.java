package com.selfserv.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A RequestType.
 */
@Entity
@Table(name = "request_type")
public class RequestType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "request_type_id", unique = true)
    private String requestTypeID;

    @Column(name = "request_type_name")
    private String requestTypeName;

    @Lob
    @Column(name = "request_type_input", nullable = false)
    private String requestTypeInput;

    @NotNull
    @Column(name = "approval_process", nullable = false)
    private String approvalProcess;

    @NotNull
    @Column(name = "automation_process", nullable = false)
    private String automationProcess;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RequestType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestTypeID() {
        return this.requestTypeID;
    }

    public RequestType requestTypeID(String requestTypeID) {
        this.setRequestTypeID(requestTypeID);
        return this;
    }

    public void setRequestTypeID(String requestTypeID) {
        this.requestTypeID = requestTypeID;
    }

    public String getRequestTypeName() {
        return this.requestTypeName;
    }

    public RequestType requestTypeName(String requestTypeName) {
        this.setRequestTypeName(requestTypeName);
        return this;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getRequestTypeInput() {
        return this.requestTypeInput;
    }

    public RequestType requestTypeInput(String requestTypeInput) {
        this.setRequestTypeInput(requestTypeInput);
        return this;
    }

    public void setRequestTypeInput(String requestTypeInput) {
        this.requestTypeInput = requestTypeInput;
    }

    public String getApprovalProcess() {
        return this.approvalProcess;
    }

    public RequestType approvalProcess(String approvalProcess) {
        this.setApprovalProcess(approvalProcess);
        return this;
    }

    public void setApprovalProcess(String approvalProcess) {
        this.approvalProcess = approvalProcess;
    }

    public String getAutomationProcess() {
        return this.automationProcess;
    }

    public RequestType automationProcess(String automationProcess) {
        this.setAutomationProcess(automationProcess);
        return this;
    }

    public void setAutomationProcess(String automationProcess) {
        this.automationProcess = automationProcess;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestType)) {
            return false;
        }
        return id != null && id.equals(((RequestType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestType{" +
            "id=" + getId() +
            ", requestTypeID='" + getRequestTypeID() + "'" +
            ", requestTypeName='" + getRequestTypeName() + "'" +
            ", requestTypeInput='" + getRequestTypeInput() + "'" +
            ", approvalProcess='" + getApprovalProcess() + "'" +
            ", automationProcess='" + getAutomationProcess() + "'" +
            "}";
    }
}

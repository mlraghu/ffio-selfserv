package com.selfserv.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.selfserv.IntegrationTest;
import com.selfserv.domain.RequestType;
import com.selfserv.repository.RequestTypeRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RequestTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestTypeResourceIT {

    private static final String DEFAULT_REQUEST_TYPE_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_TYPE_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE_INPUT = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVAL_PROCESS = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_PROCESS = "BBBBBBBBBB";

    private static final String DEFAULT_AUTOMATION_PROCESS = "AAAAAAAAAA";
    private static final String UPDATED_AUTOMATION_PROCESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/request-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequestTypeRepository requestTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestTypeMockMvc;

    private RequestType requestType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestType createEntity(EntityManager em) {
        RequestType requestType = new RequestType()
            .requestTypeID(DEFAULT_REQUEST_TYPE_ID)
            .requestTypeName(DEFAULT_REQUEST_TYPE_NAME)
            .requestTypeInput(DEFAULT_REQUEST_TYPE_INPUT)
            .approvalProcess(DEFAULT_APPROVAL_PROCESS)
            .automationProcess(DEFAULT_AUTOMATION_PROCESS);
        return requestType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestType createUpdatedEntity(EntityManager em) {
        RequestType requestType = new RequestType()
            .requestTypeID(UPDATED_REQUEST_TYPE_ID)
            .requestTypeName(UPDATED_REQUEST_TYPE_NAME)
            .requestTypeInput(UPDATED_REQUEST_TYPE_INPUT)
            .approvalProcess(UPDATED_APPROVAL_PROCESS)
            .automationProcess(UPDATED_AUTOMATION_PROCESS);
        return requestType;
    }

    @BeforeEach
    public void initTest() {
        requestType = createEntity(em);
    }

    @Test
    @Transactional
    void createRequestType() throws Exception {
        int databaseSizeBeforeCreate = requestTypeRepository.findAll().size();
        // Create the RequestType
        restRequestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isCreated());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getRequestTypeID()).isEqualTo(DEFAULT_REQUEST_TYPE_ID);
        assertThat(testRequestType.getRequestTypeName()).isEqualTo(DEFAULT_REQUEST_TYPE_NAME);
        assertThat(testRequestType.getRequestTypeInput()).isEqualTo(DEFAULT_REQUEST_TYPE_INPUT);
        assertThat(testRequestType.getApprovalProcess()).isEqualTo(DEFAULT_APPROVAL_PROCESS);
        assertThat(testRequestType.getAutomationProcess()).isEqualTo(DEFAULT_AUTOMATION_PROCESS);
    }

    @Test
    @Transactional
    void createRequestTypeWithExistingId() throws Exception {
        // Create the RequestType with an existing ID
        requestType.setId(1L);

        int databaseSizeBeforeCreate = requestTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isBadRequest());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApprovalProcessIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestTypeRepository.findAll().size();
        // set the field null
        requestType.setApprovalProcess(null);

        // Create the RequestType, which fails.

        restRequestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isBadRequest());

        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAutomationProcessIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestTypeRepository.findAll().size();
        // set the field null
        requestType.setAutomationProcess(null);

        // Create the RequestType, which fails.

        restRequestTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isBadRequest());

        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequestTypes() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        // Get all the requestTypeList
        restRequestTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestType.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestTypeID").value(hasItem(DEFAULT_REQUEST_TYPE_ID)))
            .andExpect(jsonPath("$.[*].requestTypeName").value(hasItem(DEFAULT_REQUEST_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].requestTypeInput").value(hasItem(DEFAULT_REQUEST_TYPE_INPUT.toString())))
            .andExpect(jsonPath("$.[*].approvalProcess").value(hasItem(DEFAULT_APPROVAL_PROCESS)))
            .andExpect(jsonPath("$.[*].automationProcess").value(hasItem(DEFAULT_AUTOMATION_PROCESS)));
    }

    @Test
    @Transactional
    void getRequestType() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        // Get the requestType
        restRequestTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, requestType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestType.getId().intValue()))
            .andExpect(jsonPath("$.requestTypeID").value(DEFAULT_REQUEST_TYPE_ID))
            .andExpect(jsonPath("$.requestTypeName").value(DEFAULT_REQUEST_TYPE_NAME))
            .andExpect(jsonPath("$.requestTypeInput").value(DEFAULT_REQUEST_TYPE_INPUT.toString()))
            .andExpect(jsonPath("$.approvalProcess").value(DEFAULT_APPROVAL_PROCESS))
            .andExpect(jsonPath("$.automationProcess").value(DEFAULT_AUTOMATION_PROCESS));
    }

    @Test
    @Transactional
    void getNonExistingRequestType() throws Exception {
        // Get the requestType
        restRequestTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRequestType() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();

        // Update the requestType
        RequestType updatedRequestType = requestTypeRepository.findById(requestType.getId()).get();
        // Disconnect from session so that the updates on updatedRequestType are not directly saved in db
        em.detach(updatedRequestType);
        updatedRequestType
            .requestTypeID(UPDATED_REQUEST_TYPE_ID)
            .requestTypeName(UPDATED_REQUEST_TYPE_NAME)
            .requestTypeInput(UPDATED_REQUEST_TYPE_INPUT)
            .approvalProcess(UPDATED_APPROVAL_PROCESS)
            .automationProcess(UPDATED_AUTOMATION_PROCESS);

        restRequestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequestType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequestType))
            )
            .andExpect(status().isOk());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getRequestTypeID()).isEqualTo(UPDATED_REQUEST_TYPE_ID);
        assertThat(testRequestType.getRequestTypeName()).isEqualTo(UPDATED_REQUEST_TYPE_NAME);
        assertThat(testRequestType.getRequestTypeInput()).isEqualTo(UPDATED_REQUEST_TYPE_INPUT);
        assertThat(testRequestType.getApprovalProcess()).isEqualTo(UPDATED_APPROVAL_PROCESS);
        assertThat(testRequestType.getAutomationProcess()).isEqualTo(UPDATED_AUTOMATION_PROCESS);
    }

    @Test
    @Transactional
    void putNonExistingRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();
        requestType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();
        requestType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();
        requestType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requestType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequestTypeWithPatch() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();

        // Update the requestType using partial update
        RequestType partialUpdatedRequestType = new RequestType();
        partialUpdatedRequestType.setId(requestType.getId());

        partialUpdatedRequestType
            .requestTypeID(UPDATED_REQUEST_TYPE_ID)
            .approvalProcess(UPDATED_APPROVAL_PROCESS)
            .automationProcess(UPDATED_AUTOMATION_PROCESS);

        restRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestType))
            )
            .andExpect(status().isOk());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getRequestTypeID()).isEqualTo(UPDATED_REQUEST_TYPE_ID);
        assertThat(testRequestType.getRequestTypeName()).isEqualTo(DEFAULT_REQUEST_TYPE_NAME);
        assertThat(testRequestType.getRequestTypeInput()).isEqualTo(DEFAULT_REQUEST_TYPE_INPUT);
        assertThat(testRequestType.getApprovalProcess()).isEqualTo(UPDATED_APPROVAL_PROCESS);
        assertThat(testRequestType.getAutomationProcess()).isEqualTo(UPDATED_AUTOMATION_PROCESS);
    }

    @Test
    @Transactional
    void fullUpdateRequestTypeWithPatch() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();

        // Update the requestType using partial update
        RequestType partialUpdatedRequestType = new RequestType();
        partialUpdatedRequestType.setId(requestType.getId());

        partialUpdatedRequestType
            .requestTypeID(UPDATED_REQUEST_TYPE_ID)
            .requestTypeName(UPDATED_REQUEST_TYPE_NAME)
            .requestTypeInput(UPDATED_REQUEST_TYPE_INPUT)
            .approvalProcess(UPDATED_APPROVAL_PROCESS)
            .automationProcess(UPDATED_AUTOMATION_PROCESS);

        restRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestType))
            )
            .andExpect(status().isOk());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
        RequestType testRequestType = requestTypeList.get(requestTypeList.size() - 1);
        assertThat(testRequestType.getRequestTypeID()).isEqualTo(UPDATED_REQUEST_TYPE_ID);
        assertThat(testRequestType.getRequestTypeName()).isEqualTo(UPDATED_REQUEST_TYPE_NAME);
        assertThat(testRequestType.getRequestTypeInput()).isEqualTo(UPDATED_REQUEST_TYPE_INPUT);
        assertThat(testRequestType.getApprovalProcess()).isEqualTo(UPDATED_APPROVAL_PROCESS);
        assertThat(testRequestType.getAutomationProcess()).isEqualTo(UPDATED_AUTOMATION_PROCESS);
    }

    @Test
    @Transactional
    void patchNonExistingRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();
        requestType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requestType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();
        requestType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequestType() throws Exception {
        int databaseSizeBeforeUpdate = requestTypeRepository.findAll().size();
        requestType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(requestType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestType in the database
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequestType() throws Exception {
        // Initialize the database
        requestTypeRepository.saveAndFlush(requestType);

        int databaseSizeBeforeDelete = requestTypeRepository.findAll().size();

        // Delete the requestType
        restRequestTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, requestType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequestType> requestTypeList = requestTypeRepository.findAll();
        assertThat(requestTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

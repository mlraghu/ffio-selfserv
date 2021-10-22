package com.selfserv.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.selfserv.IntegrationTest;
import com.selfserv.domain.Request;
import com.selfserv.domain.enumeration.RequestStatus;
import com.selfserv.repository.RequestRepository;
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
 * Integration tests for the {@link RequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestResourceIT {

    private static final Long DEFAULT_REQUEST_ID = 1L;
    private static final Long UPDATED_REQUEST_ID = 2L;

    private static final String DEFAULT_REQUEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_COST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COST_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENVIRONMENTS = 1;
    private static final Integer UPDATED_ENVIRONMENTS = 2;

    private static final String DEFAULT_CONFIG_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_INPUT = "BBBBBBBBBB";

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.DRAFT;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.PENDING;

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestMockMvc;

    private Request request;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity(EntityManager em) {
        Request request = new Request()
            .requestID(DEFAULT_REQUEST_ID)
            .requestType(DEFAULT_REQUEST_TYPE)
            .projectInfo(DEFAULT_PROJECT_INFO)
            .costCode(DEFAULT_COST_CODE)
            .environments(DEFAULT_ENVIRONMENTS)
            .configInput(DEFAULT_CONFIG_INPUT)
            .status(DEFAULT_STATUS);
        return request;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createUpdatedEntity(EntityManager em) {
        Request request = new Request()
            .requestID(UPDATED_REQUEST_ID)
            .requestType(UPDATED_REQUEST_TYPE)
            .projectInfo(UPDATED_PROJECT_INFO)
            .costCode(UPDATED_COST_CODE)
            .environments(UPDATED_ENVIRONMENTS)
            .configInput(UPDATED_CONFIG_INPUT)
            .status(UPDATED_STATUS);
        return request;
    }

    @BeforeEach
    public void initTest() {
        request = createEntity(em);
    }

    @Test
    @Transactional
    void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();
        // Create the Request
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestID()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testRequest.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testRequest.getProjectInfo()).isEqualTo(DEFAULT_PROJECT_INFO);
        assertThat(testRequest.getCostCode()).isEqualTo(DEFAULT_COST_CODE);
        assertThat(testRequest.getEnvironments()).isEqualTo(DEFAULT_ENVIRONMENTS);
        assertThat(testRequest.getConfigInput()).isEqualTo(DEFAULT_CONFIG_INPUT);
        assertThat(testRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createRequestWithExistingId() throws Exception {
        // Create the Request with an existing ID
        request.setId(1L);

        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProjectInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setProjectInfo(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setCostCode(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnvironmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setEnvironments(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setStatus(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestID").value(hasItem(DEFAULT_REQUEST_ID.intValue())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].projectInfo").value(hasItem(DEFAULT_PROJECT_INFO)))
            .andExpect(jsonPath("$.[*].costCode").value(hasItem(DEFAULT_COST_CODE)))
            .andExpect(jsonPath("$.[*].environments").value(hasItem(DEFAULT_ENVIRONMENTS)))
            .andExpect(jsonPath("$.[*].configInput").value(hasItem(DEFAULT_CONFIG_INPUT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get the request
        restRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId().intValue()))
            .andExpect(jsonPath("$.requestID").value(DEFAULT_REQUEST_ID.intValue()))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE))
            .andExpect(jsonPath("$.projectInfo").value(DEFAULT_PROJECT_INFO))
            .andExpect(jsonPath("$.costCode").value(DEFAULT_COST_CODE))
            .andExpect(jsonPath("$.environments").value(DEFAULT_ENVIRONMENTS))
            .andExpect(jsonPath("$.configInput").value(DEFAULT_CONFIG_INPUT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        Request updatedRequest = requestRepository.findById(request.getId()).get();
        // Disconnect from session so that the updates on updatedRequest are not directly saved in db
        em.detach(updatedRequest);
        updatedRequest
            .requestID(UPDATED_REQUEST_ID)
            .requestType(UPDATED_REQUEST_TYPE)
            .projectInfo(UPDATED_PROJECT_INFO)
            .costCode(UPDATED_COST_CODE)
            .environments(UPDATED_ENVIRONMENTS)
            .configInput(UPDATED_CONFIG_INPUT)
            .status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestID()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequest.getProjectInfo()).isEqualTo(UPDATED_PROJECT_INFO);
        assertThat(testRequest.getCostCode()).isEqualTo(UPDATED_COST_CODE);
        assertThat(testRequest.getEnvironments()).isEqualTo(UPDATED_ENVIRONMENTS);
        assertThat(testRequest.getConfigInput()).isEqualTo(UPDATED_CONFIG_INPUT);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, request.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest
            .requestID(UPDATED_REQUEST_ID)
            .requestType(UPDATED_REQUEST_TYPE)
            .costCode(UPDATED_COST_CODE)
            .environments(UPDATED_ENVIRONMENTS)
            .configInput(UPDATED_CONFIG_INPUT);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestID()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequest.getProjectInfo()).isEqualTo(DEFAULT_PROJECT_INFO);
        assertThat(testRequest.getCostCode()).isEqualTo(UPDATED_COST_CODE);
        assertThat(testRequest.getEnvironments()).isEqualTo(UPDATED_ENVIRONMENTS);
        assertThat(testRequest.getConfigInput()).isEqualTo(UPDATED_CONFIG_INPUT);
        assertThat(testRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest
            .requestID(UPDATED_REQUEST_ID)
            .requestType(UPDATED_REQUEST_TYPE)
            .projectInfo(UPDATED_PROJECT_INFO)
            .costCode(UPDATED_COST_CODE)
            .environments(UPDATED_ENVIRONMENTS)
            .configInput(UPDATED_CONFIG_INPUT)
            .status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestID()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequest.getProjectInfo()).isEqualTo(UPDATED_PROJECT_INFO);
        assertThat(testRequest.getCostCode()).isEqualTo(UPDATED_COST_CODE);
        assertThat(testRequest.getEnvironments()).isEqualTo(UPDATED_ENVIRONMENTS);
        assertThat(testRequest.getConfigInput()).isEqualTo(UPDATED_CONFIG_INPUT);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, request.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Delete the request
        restRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, request.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

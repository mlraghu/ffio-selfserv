package com.selfserv.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.selfserv.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestType.class);
        RequestType requestType1 = new RequestType();
        requestType1.setId(1L);
        RequestType requestType2 = new RequestType();
        requestType2.setId(requestType1.getId());
        assertThat(requestType1).isEqualTo(requestType2);
        requestType2.setId(2L);
        assertThat(requestType1).isNotEqualTo(requestType2);
        requestType1.setId(null);
        assertThat(requestType1).isNotEqualTo(requestType2);
    }
}

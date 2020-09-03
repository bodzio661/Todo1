package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class FakturaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Faktura.class);
        Faktura faktura1 = new Faktura();
        faktura1.setId(1L);
        Faktura faktura2 = new Faktura();
        faktura2.setId(faktura1.getId());
        assertThat(faktura1).isEqualTo(faktura2);
        faktura2.setId(2L);
        assertThat(faktura1).isNotEqualTo(faktura2);
        faktura1.setId(null);
        assertThat(faktura1).isNotEqualTo(faktura2);
    }
}

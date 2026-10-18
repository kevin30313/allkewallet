package com.kevin30313.alkewallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"JWT_SECRET=testSecretKeyForJwtTokenValidation12345678901234567890"})
@ActiveProfiles("test")
class AlkewalletApplicationTests {

    @Test
    void contextLoads() {
    }

}

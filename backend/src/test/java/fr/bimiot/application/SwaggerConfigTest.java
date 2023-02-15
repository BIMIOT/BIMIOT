package fr.bimiot.application;

import org.junit.jupiter.api.Test;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {
    SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void api_shouldReturnDocketConfigurationForSwagger2(){
        Docket docket = swaggerConfig.api();
        assertThat(docket.getDocumentationType()).isEqualTo(DocumentationType.SWAGGER_2);
    }
}
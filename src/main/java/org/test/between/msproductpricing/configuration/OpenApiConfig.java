package org.test.between.msproductpricing.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfig class: Configuration class for OpenAPI description fields.
 */
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "${api.doc.description}",
        version = "${api.doc.version}",
        description = "${api.doc.name}"
))
public class OpenApiConfig {

}

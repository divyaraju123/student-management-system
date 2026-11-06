
package javaspringboot.Student.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentManagementAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Student Management System API")
                        .description("REST API documentation for the Student Management System. Supports student creation, retrieval, updating, deletion, pagination, sorting, validation, and searching.")
                        .version("1.0.0"));
    }
}

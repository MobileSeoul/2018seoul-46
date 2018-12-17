package me.quiz_together.root.config;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import me.quiz_together.root.support.ApiVersion;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@SwaggerDefinition(
        info = @Info(
                description = "Start Hackathon",
                version = "V1.0.0",
                title = "Winner",
                termsOfService = "http://theweatherapi.io/terms.html",
                contact = @Contact(
                        name = "WooJin Kang",
                        email = "shon9811@naver.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        consumes = { "application/json", "application/xml" },
        produces = { "application/json", "application/xml" },
        schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }
//        tags = {
//                @Tag(name = "Private", description = "Tag used to denote operations as private")
//        },
//        externalDocs = @ExternalDocs(value = "Meteorology", url = "http://theweatherapi.io/meteorology.html")
)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getGlobalParams());

    }

    @NotNull
    private ArrayList<Parameter> getGlobalParams() {
        return Lists.newArrayList(new ParameterBuilder()
                                          .name("apiVersion")
                                          .description("api version")
                                          .modelRef(new ModelRef("string"))
                                          .parameterType("path")
                                          .defaultValue(
                                                  'v' + String.valueOf(ApiVersion.LATEST_API_VER.getVersion()))
                                          .required(true)
                                          .build());
    }
}

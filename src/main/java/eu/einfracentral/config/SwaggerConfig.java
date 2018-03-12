package eu.einfracentral.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.Api;
import java.net.URL;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by pgl on 08/12/17.
 */
@Configuration
@EnableSwagger2
@PropertySource({"classpath:application.properties"})
public class SwaggerConfig {
    @Autowired
    private TypeResolver typeResolver;
    @Value("${platform.root:}")
    private String platform;

    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(URL.class, String.class)
                .directModelSubstitute(XMLGregorianCalendar.class, String.class)
//                .alternateTypeRules(newRule(typeResolver.arrayType(URL.class), typeResolver.arrayType(String.class)))
//                .alternateTypeRules(newRule(typeResolver.arrayType(XMLGregorianCalendar.class), typeResolver.arrayType(String.class)))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("eInfraCentral")
                .description("External APIs for the eInfraCentral registry")
                .version("1")
                .termsOfServiceUrl(String.format("%s/tos", platform))
//                .license("NAME")
                .licenseUrl(String.format("%s/license", platform))
                .build();
    }
}

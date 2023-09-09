package telran.functionality.com.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Bank application api",
                description = "Bank application", version = "1.0.0",
                contact = @Contact(
                        name = "Olena Averchenko",
                        email = "lena@averchenko.com",
                        url = "http://lena.com"
                )
        )
)
@SecurityScheme(
        name = "basicauth",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenApiConfig {
//
}
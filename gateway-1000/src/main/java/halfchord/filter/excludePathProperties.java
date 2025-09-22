package halfchord.filter;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@PropertySource("classpath:exclundePath.properties")
@ConfigurationProperties(prefix = "exclude")
public class excludePathProperties {
    private List<String> urls;
}

package task1.soft.api.appconfig;

import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Data
@Configuration
public class ProfileMvcConfig extends WebMvcConfigurerAdapter {

    private MessageSource messageSource;

    /**
     * This method is overridden due to use the {@link MessageSource message source} in bean validation.
     * @return  A Validator using the {@link MessageSource message source} in bean validation.
     */
    @Override
    public Validator getValidator() {

        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);

        return factory;
    }
}

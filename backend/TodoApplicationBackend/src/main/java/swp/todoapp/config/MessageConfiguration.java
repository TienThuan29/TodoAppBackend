package swp.todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:message.properties")
public class MessageConfiguration {

    public final String ERROR_USERNAME_NOTFOUND;
    public final String ERROR_USERNAME_PASSWORD_INVALID;
    public final String ERROR_ROLE_NOTFOUND;
    public final String ERROR_JWT_INVALID_TOKEN;
    public final String ERROR_USER_NOT_FOUND;

    // Inform message
    public final String INFORM_UPDATE_PROFILE_IMAGE_SUCCESS;
    public final String INFORM_UPDATE_PASS_SUCCESS;

    public MessageConfiguration(
            @Value("${message.error.username-pass-incorrect}") String err_username_password_invalid,
            @Value("${message.error.role.notfound}") String err_role_notfound,
            @Value("${message.error.username.notfound}") String err_username_notfound,
            @Value("${message.error.jwt.invalid-token}") String jwt_invalid_token,
            @Value("${message.error.user.not-found}") String err_user_not_found,
            @Value("${message.inform.update-image-success}") String inform_update_image_success,
            @Value("${message.inform.update-pass-success}") String inform_update_pass_success
    ) {
        this.ERROR_USERNAME_PASSWORD_INVALID = err_username_password_invalid;
        this.ERROR_ROLE_NOTFOUND = err_role_notfound;
        this.ERROR_USERNAME_NOTFOUND = err_username_notfound;
        this.ERROR_JWT_INVALID_TOKEN = jwt_invalid_token;
        this.ERROR_USER_NOT_FOUND = err_user_not_found;
        this.INFORM_UPDATE_PROFILE_IMAGE_SUCCESS = inform_update_image_success;
        this.INFORM_UPDATE_PASS_SUCCESS = inform_update_pass_success;
    }

}

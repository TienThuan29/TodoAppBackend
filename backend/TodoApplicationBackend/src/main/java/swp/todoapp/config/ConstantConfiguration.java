package swp.todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:security.properties")
public class ConstantConfiguration {

    // Authorization properties
    public final String HTTP_HEADER_AUTHORIZATION;
    public final String HTTP_HEADER_AUTHORIZATION_BEARER;

    // Jwt properties
    public final Long JWT_TOKEN_EXPIRATION;
    public final Long REFRESH_TOKEN_EXPIRATION;
    public final String SECRET_KEY;
    public final Boolean JWT_REVOKED_DISABLE;
    public final Boolean JWT_REVOKED_ENABLE;
    public final Boolean JWT_EXPIRED_DISABLE;
    public final Boolean JWT_EXPIRED_ENABLE;

    // User interface URL properties
    public final String URL_UI;
    public final String UI_METHOD_GET;
    public final String UI_METHOD_POST;
    public final String UI_METHOD_PUT;
    public final String UI_METHOD_DELETE;
    public final String UI_LOGIN_PAGE;
    public final String UI_DEFAULT_LOGIN_SUCCESS;

    // Cors properties
    public final String CORS_ALLOWED_HEADER;
    public final Long CORS_MAX_AGE;
    public final String CORS_PATTERN;
    public final String API_ALL_AUTHEN;
    public final String LOGOUT_HANDLER_URL;

    public ConstantConfiguration(
        @Value("${jwt.expiration}") Long jwt_token_expiration,
        @Value("${jwt.refresh-token.expiration}") Long refresh_token_expiration,
        @Value("${jwt.secret-key}") String secret_key,
        @Value("${jwt.expired.disable}") Boolean jwt_expired_disable,
        @Value("${jwt.expired.enable}") Boolean jwt_expired_enable,
        @Value("${jwt.revoked.disable}") Boolean jwt_revoked_disable,
        @Value("${jwt.revoked.enable}") Boolean jwt_revoked_enable,
        @Value("${auth.header}") String http_header_authorization,
        @Value("${auth.header.bearer}") String http_header_authorization_bearer,
        @Value("${url.ui.method.get}") String ui_method_get,
        @Value("${url.ui.method.post}") String ui_method_post,
        @Value("${url.ui.method.put}") String ui_method_put,
        @Value("${url.ui.method.delete}") String ui_method_delete,
        @Value("${url.cors.allowed-header}") String allowed_header,
        @Value("${url.cors.age}") Long max_age,
        @Value("${url.cors.pattern}") String cors_pattern,
        @Value("${url.ui}") String url_ui,
        @Value("${api.url.all.auth}") String api_url_all_auth,
        @Value("${api.url.logout}") String api_url_logout,
        @Value("${url.ui.login-page}") String ui_login_page,
        @Value("${url.ui.oauth2.default-success-login}") String ui_default_page_after_login_success
    ) {
        this.JWT_TOKEN_EXPIRATION = jwt_token_expiration;
        this.REFRESH_TOKEN_EXPIRATION = refresh_token_expiration;
        this.SECRET_KEY = secret_key;
        this.JWT_REVOKED_DISABLE = jwt_revoked_disable;
        this.JWT_REVOKED_ENABLE = jwt_revoked_enable;
        this.JWT_EXPIRED_DISABLE = jwt_expired_disable;
        this.JWT_EXPIRED_ENABLE = jwt_expired_enable;
        this.HTTP_HEADER_AUTHORIZATION = http_header_authorization;
        this.HTTP_HEADER_AUTHORIZATION_BEARER = http_header_authorization_bearer;
        this.UI_METHOD_GET = ui_method_get;
        this.UI_METHOD_POST = ui_method_post;
        this.UI_METHOD_PUT = ui_method_put;
        this.UI_METHOD_DELETE = ui_method_delete;
        this.CORS_ALLOWED_HEADER = allowed_header;
        this.CORS_MAX_AGE = max_age;
        this.CORS_PATTERN = cors_pattern;
        this.URL_UI = url_ui;
        this.API_ALL_AUTHEN = api_url_all_auth;
        this.LOGOUT_HANDLER_URL = api_url_logout;
        this.UI_LOGIN_PAGE = ui_login_page;
        this.UI_DEFAULT_LOGIN_SUCCESS = ui_default_page_after_login_success;
    }

}

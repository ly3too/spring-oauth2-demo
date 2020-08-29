package cn.ly3too.oauthdemo.resourceserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * use opaque token introspection
 */
@EnableWebSecurity
@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {
    private static Logger log = LoggerFactory.getLogger(WebSecurityConfigurerAdapter.class);

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}") String introspectionUri;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}") String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}") String clientSecret;

    /**
     * config the resource authorities;
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .antMatchers(HttpMethod.GET, "/message/**").hasAnyAuthority("admin", "user")
                    .antMatchers(HttpMethod.POST, "/message/**").hasAuthority("admin")
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
    }

    @Bean
    public OpaqueTokenIntrospector introspector() {
        return new CustomAuthoritiesOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    /**
     * custom token introspector. since a non jwt token used
     */
    public static class CustomAuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
        private OpaqueTokenIntrospector delegate;
        public CustomAuthoritiesOpaqueTokenIntrospector(String introspectUri, String clientId, String secret) {
            delegate = new NimbusOpaqueTokenIntrospector(introspectUri, clientId, secret);
        }

        public OAuth2AuthenticatedPrincipal introspect(String token) {
            OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
            log.debug("original principle: " + principal);
            return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getAttribute("user_name"), principal.getAttributes(),
                extractAuthorities(principal));
        }

        /**
         * get authorities from attributes
         * @param principal
         * @return
         */
        private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
            List<String> scopes = principal.getAttribute("authorities");
            return scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
    }
}

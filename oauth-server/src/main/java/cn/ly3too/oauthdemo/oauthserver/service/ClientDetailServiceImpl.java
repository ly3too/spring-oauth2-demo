package cn.ly3too.oauthdemo.oauthserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailServiceImpl implements ClientDetailsService {
    private Logger log = LoggerFactory.getLogger(ClientDetailsService.class);

    @Value("${client.defaultRedirectUri:localhost:8082}")
    private String defaultRediectUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * get client from client id
     * @param s
     * @return
     * @throws ClientRegistrationException
     */
    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        log.info("== querying client: " + s);
        BaseClientDetails client = new BaseClientDetails(s, "", s, "password,refresh_token,authorization_code,implicit", s, defaultRediectUri);
        client.setClientSecret(passwordEncoder.encode(s));
        return client;
    }
}

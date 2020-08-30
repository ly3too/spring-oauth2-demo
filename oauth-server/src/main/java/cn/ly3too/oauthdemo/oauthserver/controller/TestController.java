package cn.ly3too.oauthdemo.oauthserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {
    private static Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/user")
    public Object userInfo(Principal user, Authentication authentication) {
        log.info("== principal: " + user);
        log.info("== auth: " + authentication.getPrincipal());
        return authentication.getPrincipal();
    }
}

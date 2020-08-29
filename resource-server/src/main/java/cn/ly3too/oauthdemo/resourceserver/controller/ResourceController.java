package cn.ly3too.oauthdemo.resourceserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    private static Logger log = LoggerFactory.getLogger(RestController.class);

    @GetMapping("/")
    public OAuth2AuthenticatedPrincipal index(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        log.info("== authorities" + principal.getAuthorities());
        return principal;
    }

    @GetMapping("/message")
    public String message() {
        return "secret message. need user or admin authorities";
    }

    @PostMapping("/message")
    public String createMessage(@RequestBody String message) {
        return String.format("Message was created. Content: %s. need admin authority", message);
    }
}

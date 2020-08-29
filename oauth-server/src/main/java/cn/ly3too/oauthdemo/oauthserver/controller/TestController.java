package cn.ly3too.oauthdemo.oauthserver.controller;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/user")
    public AuthenticatedPrincipal userInfo(@AuthenticationPrincipal AuthenticatedPrincipal principal) {
        return principal;
    }
}

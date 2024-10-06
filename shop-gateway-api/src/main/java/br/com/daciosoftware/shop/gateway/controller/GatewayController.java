package br.com.daciosoftware.shop.gateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @GetMapping("/healthcheck")
    @PreAuthorize("permitAll()")
    public String healthcheck () {
        return "ok";
    }
}

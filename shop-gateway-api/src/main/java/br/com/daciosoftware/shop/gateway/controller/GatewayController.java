package br.com.daciosoftware.shop.gateway.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @GetMapping("/health")
    public ResponseEntity<Object> healthcheck() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"service\" : \"gateway_service\", \"status\" : \"OK\" }");
    }
}

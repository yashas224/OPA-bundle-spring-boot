package com.consumer.bundle.opa.controller;

import com.consumer.bundle.opa.dto.UserRequest;
import com.consumer.bundle.opa.restclient.OpaClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;

@RestController
@RequestMapping("/opa-bundle-consumer")
public class BundleConsumerController {

  @Autowired
  OpaClient opaClient;

  @PostMapping("/check-access/{access}")
  public String checkAccess(@RequestBody UserRequest userRequest, @PathVariable(name = "access") String access) {
    boolean allowAccess = opaClient.allowAccess("user_is_".concat(access), userRequest);
    return allowAccess ? "Access Allowed " : "Access Not allowed";
  }

  @SneakyThrows
  @GetMapping("/hello")
  public String hello() {

    InetAddress ip = InetAddress.getLocalHost();
    String hostname = ip.getHostName();
    return "opa-bundle-consumer up and running in ".concat(hostname).concat(" !!");
  }
}

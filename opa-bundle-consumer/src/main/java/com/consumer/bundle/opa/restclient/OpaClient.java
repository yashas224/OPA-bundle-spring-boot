package com.consumer.bundle.opa.restclient;

import com.consumer.bundle.opa.dto.OpaRequest;
import com.consumer.bundle.opa.dto.OpaResponse;
import com.consumer.bundle.opa.dto.OpaUser;
import com.consumer.bundle.opa.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpaClient {

  @Autowired
  RestTemplate restTemplate;

  public boolean allowAccess(String accessKey, UserRequest userRequest) {
    boolean allowAccess = false;
    OpaRequest opaRequest = OpaRequest.builder().input(OpaUser.builder().user(userRequest.getUserName()).build()).build();
    HttpEntity<OpaRequest> request = new HttpEntity<>(opaRequest);
    // using sidecar container port can communicate directly
//    https://www.containiq.com/post/kubernetes-sidecar-container
    ResponseEntity<OpaResponse> response = restTemplate.postForEntity("http://localhost:8181/v1/data/play", request, OpaResponse.class);
    if(response.getStatusCode() == HttpStatus.OK) {
      OpaResponse opaResponse = response.getBody();
      allowAccess = opaResponse.getResult().containsKey(accessKey) ? true : false;
    } else {
      allowAccess = false;
    }
    return allowAccess;
  }
}

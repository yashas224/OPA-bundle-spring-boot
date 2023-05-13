package com.provider.bundle.opa.controller;

import com.provider.bundle.opa.service.BundleProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/bundle-provider")
public class BundleProviderController {

  @Autowired
  @Qualifier("linux-command-generator")
  BundleProviderService bundleProviderService;

  @Value("${bundle-name}")
  private String bundleName;

  @GetMapping("/hello")
  public String hello() throws UnknownHostException {
    InetAddress ip = InetAddress.getLocalHost();
    String hostname = ip.getHostName();
    return "opa-bundle-provider service is up in ".concat(hostname);
  }

  Logger logger = Logger.getLogger(BundleProviderController.class.getName());

  @GetMapping("/download/{bundleName}")
  public ResponseEntity getBundle(@PathVariable(name = "bundleName") String passedBundleName, @RequestHeader HttpHeaders requestHeaders) {

    logger.info("invoking bundle-provider download end point !!! ");
    logger.info("Request Headers ".concat(requestHeaders.toString()));
    if(!passedBundleName.equals(bundleName)) {
      return ResponseEntity.badRequest().body("Bundle not found !!");
    }
    deleteExistingPolicytarFile();

    File file = bundleProviderService.generateTarGzipFile();
    FileSystemResource fileSystemResource = new FileSystemResource(file);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(ContentDisposition.attachment().filename("policies.tar.gz").build());
    headers.setContentType(MediaType.valueOf("application/gzip"));
    ResponseEntity response = new ResponseEntity(fileSystemResource, headers, HttpStatus.OK);

    return response;
  }

  private void deleteExistingPolicytarFile() {

    String dir = System.getProperty("user.dir");
    var file = new File(dir.concat("/policies.tar.gz"));
    if(file.exists()) {
      logger.info("Deleting existing policies.tar.gz   !!! ");
      file.delete();
    }else{
      logger.info(" policies.tar.gz Does not  exist !!! ");

    }
  }
}

package com.provider.bundle.opa.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.logging.Logger;

@Service("linux-command-generator")

public class BundleProviderLinuxCommandServiceImpl implements BundleProviderService {

  Logger logger = Logger.getLogger(BundleProviderLinuxCommandServiceImpl.class.getName());

  @SneakyThrows
  @Override
  public File generateTarGzipFile() {
    logger.info("Generating .tar.gz file using linux command .");
    //    tar --disable-copyfile --exclude='.DS_Store' -cvzf file.tar.gz policies
    Process process = Runtime.getRuntime().exec("tar  --exclude='.DS_Store' -cvzf policies.tar.gz policies");
    while(process.isAlive()) {

    }
    String dir = System.getProperty("user.dir");
    return new File(dir.concat("/policies.tar.gz"));
  }
}

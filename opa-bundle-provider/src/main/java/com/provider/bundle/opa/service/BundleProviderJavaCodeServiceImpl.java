package com.provider.bundle.opa.service;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

@Service("code-generator")

public class BundleProviderJavaCodeServiceImpl implements BundleProviderService {

  Logger logger = Logger.getLogger(BundleProviderJavaCodeServiceImpl.class.getName());

  public File generateTarGzipFile() {
    logger.info("start");

    File file = null;
    try {
      // tar.gz a folder
      String dir = System.getProperty("user.dir");
      logger.info("path to ploicy is ".concat(dir.concat("/sample-policy-testing/policies")));
      Path source = Paths.get(dir.concat("/sample-policy-testing/policies"));
      createTarGzipFolder(source);
      file = new File(dir.concat("/policies.tar.gz"));
    } catch(IOException e) {
      e.printStackTrace();
    }
    return file;
  }

  // generate .tar.gz file at the current working directory
  // tar.gz a folder
  public static void createTarGzipFolder(Path source) throws IOException {

    if(!Files.isDirectory(source)) {
      throw new IOException("Please provide a directory.");
    }

    // get folder name as zip file name
    String tarFileName = source.getFileName().toString() + ".tar.gz";

    try(OutputStream fOut = Files.newOutputStream(Paths.get(tarFileName));
        BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
        GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

      Files.walkFileTree(source, new SimpleFileVisitor<>() {

        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attributes) {

          // only copy files, no symbolic links
          if(attributes.isSymbolicLink()) {
            return FileVisitResult.CONTINUE;
          }

          // get filename
          Path targetFile = source.relativize(file);

          try {
            TarArchiveEntry tarEntry = new TarArchiveEntry(
               file.toFile(), targetFile.toString());

            tOut.putArchiveEntry(tarEntry);

            Files.copy(file, tOut);

            tOut.closeArchiveEntry();

            System.out.printf("file : %s%n", file);
          } catch(IOException e) {
            System.err.printf("Unable to tar.gz : %s%n%s%n", file, e);
          }

          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
          System.err.printf("Unable to tar.gz : %s%n%s%n", file, exc);
          return FileVisitResult.CONTINUE;
        }
      });

      tOut.finish();
    }
  }
}


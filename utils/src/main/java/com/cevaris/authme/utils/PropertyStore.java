package com.cevaris.authme.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;

import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;

public class PropertyStore {

  private final AWSKMSClient awskmsClient;
  private final Properties properties;
  private static final Logger logger = Logger.getLogger(PropertyStore.class.getName());
  public static final String PROPERTY_RESOURCE_PATH = "PROPERTY_RESOURCE_PATH";
  private final String resourcePath;

  @Inject
  public PropertyStore(AWSKMSClient awskmsClient, @Named(PROPERTY_RESOURCE_PATH) String resourcePath) {
    this.resourcePath = resourcePath;
    this.properties = new Properties();
    this.awskmsClient = awskmsClient;
  }

  public int getInt(String value) {
    return Integer.parseInt(get(value));
  }

  public long getLong(String value) {
    return Long.parseLong(get(value));
  }

  public String get(String value) {
    return (String) getProperty(value);
  }

  public PropertyStore loadResource() {
    loadResource(resourcePath);
    return this;
  }

  private PropertyStore loadResource(String resource) {
    try {
      InputStream encInputStream = getResourceInputStream(resource);
      InputStream plainInputStream = decryptInputStream(encInputStream);
      loadProperties(plainInputStream);
    } catch (IOException e) {
      logger.log(Level.SEVERE, String.format("failed to load %s", resource), e);
    }

    return this;
  }

  private Object getProperty(String value) {
    Object result = this.properties.get(value);
    if (result == null) {
      logger.log(Level.WARNING, String.format("%s property not found", value));
    }
    return result;
  }

  private void loadProperties(InputStream inputStream) throws IOException {
    properties.load(inputStream);
  }

  private InputStream decryptInputStream(InputStream stream) throws IOException {
    ByteBuffer buffer = ByteBuffer.wrap(ByteStreams.toByteArray(stream));

    DecryptRequest req = new DecryptRequest()
        .withCiphertextBlob(buffer);

    ByteBuffer plainBytes = awskmsClient
        .decrypt(req)
        .getPlaintext();

    return new ByteArrayInputStream(plainBytes.array());
  }


  private InputStream getResourceInputStream(String resource) {
    Preconditions.checkNotNull(resource);
    InputStream stream = getClass().getResourceAsStream(resource);
    Preconditions.checkNotNull(stream, String.format("resource %s not found", resource));
    return stream;
  }

}

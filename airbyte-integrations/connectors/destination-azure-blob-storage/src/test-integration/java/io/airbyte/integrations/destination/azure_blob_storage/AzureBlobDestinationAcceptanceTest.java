package io.airbyte.integrations.destination.azure_blob_storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import io.airbyte.commons.io.IOs;
import io.airbyte.commons.json.Jsons;
import io.airbyte.protocol.models.AirbyteConnectionStatus;
import io.airbyte.protocol.models.AirbyteConnectionStatus.Status;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AzureBlobDestinationAcceptanceTest {

  protected final String secretFilePath = "secrets/config.json";
  private JsonNode config;

  @BeforeEach
  public void beforeAll(){
    config = Jsons.deserialize(IOs.readFile(Path.of(secretFilePath)));
  }


//  // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  //  refactor test and remove hardcoded creds from secrets file
  @Test
  public void testCheck() {
    final AzureBlobStorageDestination azureBlobStorageDestination = new AzureBlobStorageDestination();
    final AirbyteConnectionStatus checkResult = azureBlobStorageDestination.check(config);

    assertEquals(Status.SUCCEEDED, checkResult.getStatus());
  }


  @Test
  public void testParallelSWrite() {
    final AzureBlobStorageDestination azureBlobStorageDestination = new AzureBlobStorageDestination();
    final AirbyteConnectionStatus checkResult = azureBlobStorageDestination.check(config);

    assertEquals(Status.SUCCEEDED, checkResult.getStatus());
  }

  @Test
  public void testCheckInvalidAccountName() {
    final JsonNode invalidConfig = Jsons.jsonNode(ImmutableMap.builder()
        .put("azure_blob_storage_account_name", "someInvalidName")
        .put("azure_blob_storage_account_key", config.get("azure_blob_storage_account_key"))
        .build());

    final AzureBlobStorageDestination azureBlobStorageDestination = new AzureBlobStorageDestination();
    final AirbyteConnectionStatus checkResult = azureBlobStorageDestination.check(invalidConfig);

    assertEquals(Status.FAILED, checkResult.getStatus());
  }

  @Test
  public void testCheckInvalidKey() {
    final JsonNode invalidConfig = Jsons.jsonNode(ImmutableMap.builder()
        .put("azure_blob_storage_account_name", config.get("azure_blob_storage_account_name"))
        .put("azure_blob_storage_account_key", "someInvalidKey")
        .build());
    final AzureBlobStorageDestination azureBlobStorageDestination = new AzureBlobStorageDestination();
    final AirbyteConnectionStatus checkResult = azureBlobStorageDestination.check(invalidConfig);

    assertEquals(Status.FAILED, checkResult.getStatus());
  }


  @Test
  public void testCheckInvaliDomainName() {
    final JsonNode invalidConfig = Jsons.jsonNode(ImmutableMap.builder()
        .put("azure_blob_storage_account_name", config.get("azure_blob_storage_account_name"))
        .put("azure_blob_storage_account_key", config.get("azure_blob_storage_account_key"))
        .put("azure_blob_storage_endpoint_domain_name", "invalidDomain.com.invalid123")
        .build());
    final AzureBlobStorageDestination azureBlobStorageDestination = new AzureBlobStorageDestination();
    final AirbyteConnectionStatus checkResult = azureBlobStorageDestination.check(invalidConfig);

    assertEquals(Status.FAILED, checkResult.getStatus());
  }
}

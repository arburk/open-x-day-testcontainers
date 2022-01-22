/*
 * Copyright 2022 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baloise.open.xday.testcontainers.infrastructure.db.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {DemoRepositoryTest.Initializer.class})
class DemoRepositoryTest {

  @Autowired DemoRepository repository;

  @Container
  public static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:12")
      .withDatabaseName("openx")
      .withUsername("postgres")
      .withPassword("postgres");

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
          "spring.datasource.url=" + postgres.getJdbcUrl(),
          "spring.datasource.username=" + postgres.getUsername(),
          "spring.datasource.password=" + postgres.getPassword()
      ).applyTo(configurableApplicationContext.getEnvironment());
      System.out.println(postgres.getJdbcUrl());
    }
  }

  @Test
  void allPropertiesSetWhenPersisted() {
    // get timestamp before record is added for comparison
    final LocalDateTime beginOfTest = LocalDateTime.now();

    // create record to test with name only
    DemoEntity testRecord = DemoEntity.builder().name("MyTestRecords").build();
    assertNull(testRecord.getId()); // ID will be set by DB during persistence
    assertNull(testRecord.getRecorded()); // Timestamp is added by PrePersist lifecycle hook

    testRecord = repository.save(testRecord);

    // get timestamp after record has been added for comparison
    final LocalDateTime afterEntityPeristed = LocalDateTime.now();

    assertEquals("MyTestRecords", testRecord.getName()); // name should not be modified
    assertNotNull(testRecord.getId()); // ID should be set now by Database

    final LocalDateTime recorded = testRecord.getRecorded();
    assertNotNull(recorded); // timestamp should be set now by Database
    assertTrue(recorded.isAfter(beginOfTest));
    assertTrue(recorded.isBefore(afterEntityPeristed));
  }
}

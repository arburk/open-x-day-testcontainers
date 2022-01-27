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
package com.baloise.open.xday.testcontainers.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.OracleContainer;

public abstract class AbstractOracleTest {

  private static final String PERSISTENCE_UNIT_NAME = "PersistenceUnit";
  protected static EntityManager entityManager;


  static {
    final OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe")
        .withUsername("APP") // creates user/schema
        .withPassword("APP")
        .withInitScript("init_oracle.sql");
    oracle.start();

    final Map<String, String> properties = new HashMap<>();
    properties.put("javax.persistence.jdbc.url", oracle.getJdbcUrl());
    properties.put("javax.persistence.jdbc.user", oracle.getUsername());
    properties.put("javax.persistence.jdbc.password", oracle.getPassword());

    entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties).createEntityManager();
  }

  @BeforeEach
  public void beforeTest() {
    if (!entityManager.getTransaction().isActive()) {
      entityManager.getTransaction().begin();
    }
  }

  @AfterEach
  public void afterTest() {
    if (entityManager.getTransaction().isActive()) {
      entityManager.getTransaction().rollback();
    }
  }

}

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
package com.baloise.open.xday.testcontainers.jpa.derby;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractDerbyTest {

  private static final String DERBY_PERSISTENCE_UNIT_NAME = "DerbyTestPersistenceUnit";
  protected static EntityManager entityManager;

  @BeforeAll
  public static void beforeClass() {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(DERBY_PERSISTENCE_UNIT_NAME);
    entityManager = entityManagerFactory.createEntityManager();
  }

  @AfterAll
  public static void afterClass() {
    entityManager.close();
  }

  @BeforeEach
  public void beforeTest() {
    entityManager.getTransaction().begin();
  }

  @AfterEach
  public void afterTest() {
    entityManager.getTransaction().rollback();
  }

}

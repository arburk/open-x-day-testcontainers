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
package com.baloise.open.xday.testcontainers.jpa.demo;

import lombok.*;

import java.util.Objects;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DEMOPARTS")
public class DemoParts {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DemoPartSeq")
  @SequenceGenerator(name = "DemoPartSeq", sequenceName = "DEMO_SEQ", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "FK")
  private DemoEntity demoEntity;

  @Column(name = "NAME", nullable = false)
  private String PartName;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DemoParts that = (DemoParts) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "DemoParts{" +
               "id=" + id +
               ", foreignId=" + (demoEntity != null ? demoEntity.getId() : null) +
               ", PartName='" + PartName + '\'' +
               '}';
  }
}

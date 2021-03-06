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

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.derby.agg.Aggregator;

public class Listagg<V extends String> implements Aggregator<String, String, Listagg<V>> {

  private ArrayList<String> list = null;

  public Listagg() { }

  @Override
  public void init() {
    list = new ArrayList<>();
  }

  @Override
  public void accumulate(String content) {
    list.add(content);
  }

  @Override
  public void merge(Listagg<V> vListagg) {
    list.addAll(vListagg.list);
  }

  @Override
  public String terminate() {
    return StringUtils.join(list, ",");
  }
}

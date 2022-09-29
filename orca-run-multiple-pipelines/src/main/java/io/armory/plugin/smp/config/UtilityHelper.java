/*
 * Copyright 2020 Armory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.armory.plugin.smp.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.armory.plugin.smp.parseyml.App;
import io.armory.plugin.smp.parseyml.Apps;
import io.armory.plugin.smp.parseyml.BundleWeb;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class UtilityHelper {

    public Apps getApps(RunMultiplePipelinesContext context, Gson gson, ObjectMapper mapper) throws JsonProcessingException {
        String json = gson.toJson(context.getYamlConfig().get(0));
        BundleWeb bundleWeb = mapper.readValue(json, BundleWeb.class);

        String jsonApps = gson.toJson(bundleWeb.getBundleWeb());
        Apps apps = mapper.readValue(jsonApps, Apps.class);
        return apps;
    }

    public Map<String, Stack<App>> tryWithStack(Apps apps, ObjectMapper mapper, Gson gson) throws JsonProcessingException {
        Map<String, Stack<App>> result = new HashMap<>();

        //Push the "app" to the stack
        for (Map.Entry<String, Object> entry : apps.getApps().entrySet()) {
            Map<String, Object> mapApp = Map.ofEntries(entry);
            String jsonApp = gson.toJson(mapApp.get(entry.getKey()));
            App app = mapper.readValue(jsonApp, App.class);
            result.put(entry.getKey(), new Stack<>());
            result.get(entry.getKey()).push(app);
        }

        return result;
    }

}

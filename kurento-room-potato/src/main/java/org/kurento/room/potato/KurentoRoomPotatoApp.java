/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
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
package org.kurento.room.potato;

import static org.kurento.commons.PropertiesManager.getPropertyJson;

import java.util.List;

import org.kurento.commons.ConfigFileManager;
import org.kurento.commons.PropertiesManager;
import org.kurento.jsonrpc.JsonUtils;
import org.kurento.room.KurentoRoomServerApp;
import org.kurento.room.kms.KmsManager;
import org.kurento.room.rpc.JsonRpcUserControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Potato application for Kurento Room, extends the Room Server application class. Uses the Room
 * Client JS library for the web client, which is built with AngularJS and lumx.
 *
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @author Radu Tom Vlad (rvlad@naevatec.com)
 * @since 5.0.0
 */
public class KurentoRoomPotatoApp extends KurentoRoomServerApp {

  private static final Logger log = LoggerFactory.getLogger(KurentoRoomPotatoApp.class);

  public final static String KROOMPOTATO_CFG_FILENAME = "kurento-room-potato.conf.json";

  private static JsonObject DEFAULT_HAT_COORDS = new JsonObject();

  static {
    ConfigFileManager.loadConfigFile(KROOMPOTATO_CFG_FILENAME);
    DEFAULT_HAT_COORDS.addProperty("offsetXPercent", -0.35F);
    DEFAULT_HAT_COORDS.addProperty("offsetYPercent", -1.2F);
    DEFAULT_HAT_COORDS.addProperty("widthPercent", 1.6F);
		DEFAULT_HAT_COORDS.addProperty("heightPercent", 1.6F);
  }

  private static final String IMG_FOLDER = "img/";

  private final String DEFAULT_APP_SERVER_URL = PropertiesManager.getProperty("app.uri",
      "https://localhost:8443");

  private final Integer POTATO_KMS_NODE_LIMIT = PropertiesManager.getProperty("potato.kmsLimit", 1000);
  private final String POTATO_AUTH_REGEX = PropertiesManager.getProperty("potato.authRegex");
  private final String POTATO_HAT_URL = PropertiesManager.getProperty("potato.hatUrl",
      "mario-wings.png");

  private final JsonObject POTATO_HAT_COORDS = PropertiesManager.getPropertyJson("potato.hatCoords",
      DEFAULT_HAT_COORDS.toString(), JsonObject.class);

  @Override
  public KmsManager kmsManager() {
    JsonArray kmsUris = getPropertyJson(KurentoRoomServerApp.KMSS_URIS_PROPERTY,
        KurentoRoomServerApp.KMSS_URIS_DEFAULT, JsonArray.class);
    List<String> kmsWsUris = JsonUtils.toStringList(kmsUris);

    log.info("Configuring Kurento Room Server to use the following kmss: " + kmsWsUris);

    FixedNKmsManager fixedKmsManager = new FixedNKmsManager(kmsWsUris, POTATO_KMS_NODE_LIMIT);
    fixedKmsManager.setAuthRegex(POTATO_AUTH_REGEX);
    log.debug("Authorization regex for new rooms: {}", POTATO_AUTH_REGEX);
    return fixedKmsManager;
  }

  @Override
  public JsonRpcUserControl userControl() {
    PotatoJsonRpcUserControl uc = new PotatoJsonRpcUserControl(roomManager());
    String appServerUrl = System.getProperty("app.server.url", DEFAULT_APP_SERVER_URL);
    String hatUrl;
    if (appServerUrl.endsWith("/")) {
      hatUrl = appServerUrl + IMG_FOLDER + POTATO_HAT_URL;
    } else {
      hatUrl = appServerUrl + "/" + IMG_FOLDER + POTATO_HAT_URL;
    }
    uc.setHatUrl(hatUrl);
    uc.setHatCoords(POTATO_HAT_COORDS);
    return uc;
  }

  public static void main(String[] args) throws Exception {
    log.info("Using /dev/urandom for secure random generation");
    System.setProperty("java.security.egd", "file:/dev/./urandom");
    SpringApplication.run(KurentoRoomPotatoApp.class, args);
  }
}

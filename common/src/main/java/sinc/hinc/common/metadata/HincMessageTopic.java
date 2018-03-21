/*
 * Copyright (c) 2013 Technische Universitat Wien (TUW), Distributed Systems Group. http://dsg.tuwien.ac.at
 *
 * This work was partially supported by the European Commission in terms of the CELAR FP7 project (FP7-ICT-2011-8 #317790), http://www.celarcloud.eu/
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package sinc.hinc.common.metadata;

import java.util.UUID;

/**
 *
 * @author Duc-Hung Le
 */
//TODO check which topics are truly necessary
public class HincMessageTopic {

    private static final String PREFIX = "sinc.hinc.";
    // From CENTER to PIONEER: send request
    public static final String CLIENT_REQUEST_HINC = PREFIX + "request";
    // From Pioneer to Center: heartbeat, register
    public static final String REGISTER_AND_HEARBEAT = PREFIX + "sync";

    public static final String CLIENT_WAIT_FOR_INFORMATION = PREFIX + "client";

    public static String getHINCPrivateTopic(String hincID) {
        return CLIENT_REQUEST_HINC + "." + hincID.trim();
    }

    public static String getBroadCastTopic(String groupName) {
        return CLIENT_REQUEST_HINC + ".HINC_BROADCAST." + groupName.trim();
    }

    public static String getTemporaryTopic() {
        return PREFIX + "temp." + UUID.randomUUID().toString();
    }

}

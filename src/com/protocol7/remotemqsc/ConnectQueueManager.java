/*
 Copyright 2004 Niklas Gustavsson (niklas@protocol7.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.protocol7.remotemqsc;

import java.util.Hashtable;
import java.util.Map;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;

/**
 * Utility class for connecting to a queue manager.
 * 
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *  
 */
public class ConnectQueueManager {

    /**
     * Connect to a queue manager with the configuration provided in the Map.
     * 
     * @param config
     *            The configuration as a Map. Keys are those documented in the
     *            MQ documentation.
     * @return The connected queue manager
     * @throws MQException
     */
    public MQQueueManager clientConnection(Map config) throws MQException {

        MQQueueManager qm;

        MQEnvironment.disableTracing();
        MQException.log = null;

        if (!config.containsKey("channel")) {
            config.put("channel", "SYSTEM.ADMIN.SVRCONN");
        }

        if (config.containsKey("sslciphersuite")) {
            config.put("SSL Cipher Suite", config.get("sslciphersuite"));
        }

        if (!config.containsKey("port")) {
            config.put("port", new Integer(1414));
        }

        qm = new MQQueueManager("", new Hashtable(config));
        return qm;
    }
}
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.pcf.CMQCFC;
import com.ibm.mq.pcf.MQCFH;
import com.ibm.mq.pcf.MQCFIN;
import com.ibm.mq.pcf.MQCFST;
import com.ibm.mq.pcf.PCFParameter;

/**
 * Class for sending and receiving PCF commandos to a queue manager. Handles
 * formating, sending and receiving of messages and uses callbacks to listeners
 * to notify of received messages and PCF parameters.
 * 
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *  
 */
public class MqscRunner {

    private static final String DEFAULT_MODEL_QUEUE = "SYSTEM.DEFAULT.MODEL.QUEUE";

    private static final String REPLYQUEUE = "REMOTE.MQSC.*";

    private List listeners = new ArrayList();

    /**
     * Sends a MQSC command to the provided queue manager
     * 
     * @param queueManager
     *            Queue manager to send the command to
     * @param command
     *            The MQSC command
     * @throws MQException
     * @throws IOException
     */
    public void runCommand(MQQueueManager queueManager, String command)
            throws MQException, IOException {
        MQQueue adminQueue = null;
        MQQueue replyQueue = null;
        MQPutMessageOptions pmo = new MQPutMessageOptions();
        MQGetMessageOptions gmo = new MQGetMessageOptions();

        PCFParameter pcfParameter;
        MQMessage msg = new MQMessage();
        MQCFH mqcfh;

        try {
            // Disable all tracing
            MQEnvironment.disableTracing();
            MQException.log = null;

            // Access queues
            adminQueue = queueManager.accessQueue(queueManager
                    .getCommandInputQueueName(), MQC.MQOO_OUTPUT);
            replyQueue = queueManager.accessQueue(DEFAULT_MODEL_QUEUE,
                    MQC.MQOO_INPUT_EXCLUSIVE, null, REPLYQUEUE, null);

            msg.messageType = MQC.MQMT_REQUEST;
            msg.expiry = 100;
            msg.feedback = MQC.MQFB_NONE;
            msg.format = MQC.MQFMT_ADMIN;
            msg.replyToQueueName = replyQueue.name;

            MQCFH.write(msg, CMQCFC.MQCMD_ESCAPE, 2);

            MQCFIN.write(msg, CMQCFC.MQIACF_ESCAPE_TYPE, CMQCFC.MQET_MQSC);
            MQCFST.write(msg, CMQCFC.MQCACF_ESCAPE_TEXT, command);

            // Put PCF message
            adminQueue.put(msg, pmo);

            // Wait for response
            gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_CONVERT;
            gmo.waitInterval = 10000;

            // Read the response messages
            do {
                msg.messageId = MQC.MQMI_NONE;
                replyQueue.get(msg, gmo);
                mqcfh = new MQCFH(msg);

                for (Iterator iter = listeners.iterator(); iter.hasNext();) {
                    PcfResponseListener listener = (PcfResponseListener) iter
                            .next();
                    listener.onMessage(mqcfh);
                }

                for (int i = 0; i < mqcfh.parameterCount; i++) {
                    pcfParameter = PCFParameter.nextParameter(msg);

                    for (Iterator iter = listeners.iterator(); iter.hasNext();) {
                        PcfResponseListener listener = (PcfResponseListener) iter
                                .next();
                        listener.onParameter(pcfParameter);
                    }
                }
            } while (mqcfh.control != CMQCFC.MQCFC_LAST);
        } finally {
            try {
                if (replyQueue != null) {
                    replyQueue.close();
                }
                if (adminQueue != null) {
                    adminQueue.close();
                }
            } catch (MQException e1) {
                // ignore
            }
        }
    }

    /**
     * Add a listener that will receive callbacks when a message or PCF
     * parameter is returned from the queue manager. If the listener is already
     * registered the method call will be ignored.
     * 
     * @param listener
     *            The listener to add
     */
    public void addPcfResponseListener(PcfResponseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener can not be null");
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener that will no longer receive events. If the listener is
     * not registered the method call will be ignored.
     * 
     * @param listener
     *            The listener to remove
     */
    public void removePcfResponseListener(PcfResponseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener can not be null");
        }

        listeners.remove(listener);
    }
}
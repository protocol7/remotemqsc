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

import com.ibm.mq.pcf.MQCFH;
import com.ibm.mq.pcf.PCFParameter;

/**
 * Interface to implement if you want to receive notifications on when a new PCF
 * message or parameter is received in
 * {@link com.protocol7.remotemqsc.MqscRunner}.
 * 
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *  
 */
public interface PcfResponseListener {
    /**
     * Called when a PCF message is received
     * 
     * @param msg
     *            The message received
     */
    public void onMessage(MQCFH msg);

    /**
     * Called once for each PCF parameter returned
     * 
     * @param param
     *            The parameter
     */
    public void onParameter(PCFParameter param);
}
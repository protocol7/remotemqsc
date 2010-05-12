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

/**
 * Buffer of MQSC commands that removes comments (lines starting with *) and
 * handles newlines (+).
 * 
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *  
 */
public class MqscCommandBuffer {

    StringBuffer sb = new StringBuffer();

    /**
     * Append a new line with a full or partial MQSC command
     * 
     * @param str
     *            Some MQSC text
     * @return true if the command is complete, false otherwise
     */
    public boolean append(String str) {
        str = str.trim();

        boolean result = false;

        if (str.length() > 0) {
            if (str.startsWith("*")) {
                // ignore comment
            } else if (str.endsWith("+")) {
                sb.append(str.substring(0, str.length() - 1));
            } else {
                result = true;
                sb.append(str);
            }

        }

        return result;
    }

    /**
     * Returns to complete command without any comments and as a single line.
     */
    public String toString() {
        return sb.toString();
    }
}
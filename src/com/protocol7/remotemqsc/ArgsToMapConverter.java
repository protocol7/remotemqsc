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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing a command line argument array into a Map.<
 * 
 * @author Niklas Gustavsson (niklas@protocol7.com)
 *  
 */
public class ArgsToMapConverter {
    /**
     * Converts a command line array of pairs (-name value) into a MAP
     * 
     * @param args
     *            The command line array
     * @return A Map with names as keys and values as values
     */
    public static Map convert(String[] args) {
        Map argsMap = new HashMap();

        for (int i = 1; i < args.length; i = i + 2) {
            String key = args[i - 1].substring(1);
            String value = args[i];
            argsMap.put(key, value);
        }

        return argsMap;
    }

}
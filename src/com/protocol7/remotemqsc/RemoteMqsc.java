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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import com.ibm.mq.MQQueueManager;
import com.ibm.mq.pcf.MQCFH;
import com.ibm.mq.pcf.PCFParameter;

/**
 * Main class for running command line remote MQSC scripts. Scripts can be run
 * interactivly or by piping in a file.
 * 
 * The class will try to mimic the command line tool runmqsc as closely as
 * possible.
 *  *
 * @author Niklas Gustavsson (niklas@protocol7.com)
 */
public class RemoteMqsc implements PcfResponseListener {
    private int noOfCommands = 0;

    private int noOfSyntaxErrors = 0;

    private int noOfValidErrors = 0;

    /**
     * Shows the command line syntax
     *  
     */
    private static void showUsage() {
        StringBuffer sb = new StringBuffer();
        sb.append("usage: ").append(RemoteMqsc.class.getName()).append("\n")
                .append("\t-hostname <ip>\n").append("\t[-port <port>]\n")
                .append("\t[-channel <channel>]\n").append(
                        "\t[-sslciphersuite <sslciphersuite>]\n").append(
                        "\t[-userID <ip>]\n").append("\t[-password <ip>]\n")
                .append("\t[-CCSID <ip>]\n");

        System.out.println(sb.toString());
    }

    /**
     * Main method used to run the application
     * 
     * @param args
     *            {@see RemoteMqsc#showUsage()}
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Map argsMap = ArgsToMapConverter.convert(args);

        if (argsMap.containsKey("?")) {
            showUsage();
            System.exit(0);
        } else if (!argsMap.containsKey("hostname")) {
            showUsage();
            System.exit(-1);
        }

        if (argsMap.containsKey("port")) {
            argsMap.put("port", Integer.valueOf((String) argsMap.get("port")));
        }

        RemoteMqsc remoteMqsc = new RemoteMqsc();

        remoteMqsc.run(argsMap);
    }

    /**
     * Run the command line tool
     * 
     * @param config
     *            Configuration for connecting to the queue manager
     * @throws Exception
     */
    public void run(Map config) throws Exception {
        ConnectQueueManager connQM = new ConnectQueueManager();

        MQQueueManager qm = null;
        BufferedReader br = null;

        try {
            qm = connQM.clientConnection(config);

            System.out
                    .println("remoteMQSC by Niklas Gustavsson (niklas@protocol7.com)");
            System.out.println("Starting MQSC for queue manager at "
                    + config.get("hostname") + "(" + config.get("port")
                    + ").\n");

            MqscRunner runner = new MqscRunner();
            runner.addPcfResponseListener(this);

            br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                MqscCommandBuffer cb = new MqscCommandBuffer();

                while (!cb.append(br.readLine()))
                    ;

                if (cb.toString().equalsIgnoreCase("end")) {
                    System.out.println(noOfCommands + " MQSC commands read.");
                    if (noOfValidErrors == 0) {
                        System.out.println("No commands have a syntax error.");
                    } else {
                        System.out.println(noOfValidErrors
                                + " commands have a syntax error.");
                    }
                    if (noOfValidErrors == 0) {
                        System.out
                                .println("All valid MQSC commands were processed.");
                    } else {
                        System.out
                                .println(noOfValidErrors
                                        + " valid MQSC command could not be processed.");
                    }
                    break;
                }

                noOfCommands++;
                runner.runCommand(qm, cb.toString());
            }
        } finally {
            if (br != null)
                br.close();
            if (qm != null)
                qm.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.zystems.runmqsc.PcfResponseListener#onMessage(com.ibm.mq.pcf.MQCFH)
     */
    public void onMessage(MQCFH msg) {
        if (msg.reason != 0) {
            if (msg.reason > 4000) {
                noOfValidErrors++;
            } else {
                noOfSyntaxErrors++;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.zystems.runmqsc.PcfResponseListener#onParameter(com.ibm.mq.pcf.PCFParameter)
     */
    public void onParameter(PCFParameter param) {
        if (param.getParameter() == 3014) {
            System.out.println(param.getStringValue());
        }
    }
}
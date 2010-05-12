Remote MQSC version 0.1
Author:  Niklas Gustavsson (niklas@protocol7.com)
Website: http://www.protocol7.com/code/remotemqsc

The aim of this little application is to provide a way of running 
WebSphere MQ scripts remote. WebSphere MQ comes with a command line 
tool called runmqsc that enables an administrator to inquire and
change a queue manager and other MQ objects (e.g. queues, channels).
runmqsc only works on local queue managers. remotemqsc removes this 
limitation bt enabling MQSC scripts to be run at a remote queue
manager by making a client connection.

Usage
-----
Note that Remote MQSC is distributed without the required MQ JARs 
(I'm probably not allowed to distribute them). So, your first step 
is to place the following files into the Remote MQSC library, or add 
them to the classpath with some other method:
 * com.ibm.mq.jar
 * com.ibm.mq.pcf.jar
 * connector.jar

Run the application by executing the remotemqsc.bat file or by issuing 
the following command:
java -jar java -jar runmqsc.jar

This will show the required and optional parameters.

Version history
---------------
0.1		2004-07-10	First version

License
-------
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

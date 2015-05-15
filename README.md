client-server-app
=================

This is an exercise to implement a simple nio socket server/client using JAVA NIO package.

Functions
=================
1. implement nio server/client.
2. server can handle incoming requests in parallel.
3. DB file is in xml files, when server start, those data will be loaded into memory.
4. client can send request to add/remove/list data.
5. server has an another thread to regularly save updated data from in-memory to data file.

other document
=================
1. doc/server.jpg, to show UML class diagram for server side.
2. doc/client.jpg, to show UML class diagram for client side.

build & run
=================
1. install jdk
2. install ant
3. run "ant" to build.
4. run "java -cp ./dist/classes/ app.server.startup.Server" to start server.
5. run "java -cp ./dist/classes/ app.client.startup.Client" to start client.



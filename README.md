client-server-app
=================

This is a exercise that implements a simple nio socket server/client.

Functions
=================
1. implement nio server/client.
2. server can handle incoming requests in parallel.
3. data is stored in xml file, when server start, data will be loaded into memory.
4. client can send request to add/remove/list data.
5. server has a thread to save updated data on in-memory to data file.

document
=================
1. UML class diagram of server is doc/server.jpg
2. UML class diagram of client is doc/client.jpg

build & run
=================
1. install jdk
2. install ant
3. run "ant" to build.
4. run "java -cp ./dist/classes/ app.server.startup.Server" to start server.
5. run "java -cp ./dist/classes/ app.client.startup.Client" to start client.



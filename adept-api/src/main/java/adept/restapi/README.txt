Server:
1) The server takes the port number as a run time argument.
2) Maintains server state "RUNNING" and "INITIALIZING" in response to GET requests from the client. //TODO: think about "ERROR" state.


Client:
1) The client takes the full path of the file to be sent to the server, server name and the port as runtime arguments.
2) On successful return from server, prints the serialized response content to file. 
3) The client also prints to stdout the HTTP error code returned & the time taken to return.
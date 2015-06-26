package adept.mappers.thrift.serialization;

import adept.serialization.*;
import thrift.adept.serialization.*;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import java.util.HashMap;

public class SerializationServer {

  public static XMLSerializerWrapper handler;

  public static Serializer.Processor processor;

  public static int port;

  public static void main(String [] args) {
    try {
      port = Integer.parseInt(args[0]);
      handler = new XMLSerializerWrapper(thrift.adept.serialization.SerializationType.XML);
      processor = new Serializer.Processor(handler);

      Runnable simple = new Runnable() {
        public void run() {
          simple(processor, port);
        }
      };   

      new Thread(simple).start();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public static void simple(Serializer.Processor processor, int port) {
    try {
      TServerTransport serverTransport = new TServerSocket(new java.net.ServerSocket(port));
      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

      // Use this for a multithreaded server
      // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

      System.out.println("Starting the simple server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

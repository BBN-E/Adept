/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;


public class ReadWrite {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 7800), 0);
        server.createContext("/write", new WriteHandler());
        server.createContext("/read", new ReadHandler());
        server.setExecutor(null); // creates a default executor
        System.out.println("starting server..");
        server.start();
        System.out.println("server started");
    }

    static class WriteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            try {
                String response = "tsService: Successfully saved file.";
                t.sendResponseHeaders(200, response.length());
                System.out.println("in handle");

                // write to file
                Headers headers = t.getRequestHeaders();
                for(String key : headers.keySet())
                {
                    System.out.println("Key: " + key + "  Value: " + headers.getFirst(key));
                }

                final byte[] buf = new byte[327680]; // 320k
                int bytesRead;
                InputStream contents = t.getRequestBody();
                FileOutputStream out = new FileOutputStream(new File(headers.getFirst("filepath")), false);
                while ((bytesRead = contents.read(buf)) != -1)
                      out.write(buf, 0, bytesRead);
                out.flush();
                out.close();

                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

   static class ReadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            try {
                Headers headers = t.getRequestHeaders();

                for(String key : headers.keySet())
                {
                    System.out.println("Key: " + key + "  Value: " + headers.getFirst(key));
                }

                byte[] filecontents = Files.readAllBytes(Paths.get(headers.getFirst("filepath")));
                t.sendResponseHeaders(200, filecontents.length);
                System.out.println("File contents: " + filecontents);
                OutputStream os = t.getResponseBody();
                os.write(filecontents);
                os.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

}
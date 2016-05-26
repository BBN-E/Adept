/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.mappers.thrift.io;

import com.google.common.io.Files;

import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;

import java.io.File;
import java.io.IOException;

import adept.common.HltContentContainer;
import adept.mappers.thrift.ThriftAdeptMapper;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;

/**
 * Created by jdeyoung on 11/2/15.
 */
public final class ADEPTToThrift {

  private final static XMLSerializer xmlSerializer = new XMLSerializer(SerializationType.XML);
    private final static TSerializer thriftSerializer = new TSerializer(new TCompactProtocol.Factory());
//    private final static TSerializer thriftSerializer = new TSerializer(new TBinaryProtocol.Factory());
//  private final static TSerializer thriftSerializer = new TSerializer(new TJSONProtocol.Factory());
  private final static ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();

  public static void main(String... args) {
    // this idiom exists because an exception can be thrown without setting the exit status
    try {
      trueMain(args);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void trueMain(final String[] args) throws IOException,
                                                           TException {
    final String adeptFile = args[0];
    final String thriftFile = args[1];
    adeptDocumentToThrift(adeptFile, thriftFile);
  }

  public static void adeptDocumentToThrift(final String adeptFile, final String thriftFile)
      throws IOException, TException {
    final String serialized = adept.io.Reader.getInstance().readFileIntoString(adeptFile);
    final adept.common.HltContentContainer adeptContainer =
        (adept.common.HltContentContainer) xmlSerializer
            .deserializeString(serialized, HltContentContainer.class);
    final thrift.adept.common.HltContentContainer contentContainer = mapper.convert(adeptContainer);
    final byte[] bytes = thriftSerializer.serialize(contentContainer);
    Files.asByteSink(new File(thriftFile)).write(bytes);
  }
}

package adept.io;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import adept.common.*;
import adept.utilities.DocumentMaker;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

public class RawXMLTokenizationTest extends TestCase {

    public void test() throws Exception {
        String[] inputFiles = {
                "adept/io/26693119e4e9ebea834be051aae91289.mpdf.xml",
                "adept/io/4df27b62448bbfa8d24f6212bac9c1ba.mpdf.xml",
                "adept/io/529c5c507b2d5c43339539767cbcdf5c.mpdf.xml",
                "adept/io/6ddec4333cb07ed6614a5e721a349699.mpdf.xml",
                "adept/io/81583d0406a29558650d568c7e3eb64d.mpdf.xml",
                "adept/io/8aeb706b6fcb23d934717af7be94dd54.mpdf.xml",
                "adept/io/8ec27b90cc8b6159210e63b5a9e9ddd6.mpdf.xml",
                "adept/io/95e7f1e51583ddfc6042fb8ed6eea84a.mpdf.xml",
                "adept/io/ENG_NW_001278_20130801_F00011OCI.xml",
                "adept/io/ENG_NW_001278_20131120_F00012UHG.xml",
                "adept/io/NYT_ENG_20130419.0253.xml",
                "adept/io/NYT_ENG_20130515.0038.xml",
                "adept/io/NYT_ENG_20130603.0008.xml",
                "adept/io/NYT_ENG_20130710.0249.xml",
                "adept/io/NYT_ENG_20130724.0110.xml",
                "adept/io/NYT_ENG_20130829.0126.xml",
                "adept/io/NYT_ENG_20130912.0153.xml",
                "adept/io/NYT_ENG_20131030.0294.xml",
                "adept/io/b200ba11535848d04c3abc17bf9b6fbc.mpdf.xml",
                "adept/io/dd49c9c8294a7ec67b5e2f367768e218.mpdf.xml",
                // (the following contain Chinese text)
                "adept/io/AFP_CMN_20001001.0001.sgm.xml",
                "adept/io/AFP_CMN_20001001.0002.sgm.xml",
                "adept/io/AFP_CMN_20001001.0003.sgm.xml",
                "adept/io/AFP_CMN_20001001.0004.sgm.xml",
                "adept/io/AFP_CMN_20001001.0005.sgm.xml",
                "adept/io/AFP_CMN_20001001.0001.mpdf.xml",
                "adept/io/AFP_CMN_20001001.0002.mpdf.xml",
                "adept/io/AFP_CMN_20001001.0003.mpdf.xml",
                "adept/io/AFP_CMN_20001001.0004.mpdf.xml",
                "adept/io/AFP_CMN_20001001.0005.mpdf.xml",
        };
        for (String inputFile : inputFiles) {
            String filePath = Reader.getAbsolutePathFromClasspathOrFileSystem(inputFile);
            System.out.println("processing " + filePath);
            HltContentContainer regularHltContentContainer = new HltContentContainer();
            Document regularTokenizationDocument = DocumentMaker.getInstance().createDocument(
                    "doc1",
                    null,
                    "TEXT",
                    filePath,
                    "English",
                    filePath,
                    regularHltContentContainer,
                    TokenizerType.STANFORD_CORENLP,
                    DocumentMaker.XMLReadMode.DEFAULT
            );
            HltContentContainer rawFileHltContentContainer = new HltContentContainer();
            Document rawFileTokenizationDocument = DocumentMaker.getInstance().createDocument(
                    "doc1",
                    null,
                    "TEXT",
                    filePath,
                    "English",
                    filePath,
                    rawFileHltContentContainer,
                    TokenizerType.STANFORD_CORENLP,
                    DocumentMaker.XMLReadMode.RAW_XML
            );
            assertEquals(rawFileTokenizationDocument.getValue().length(), Reader.readRawFile(filePath).length());

            TokenStream regularTokenizationTokenStream = regularTokenizationDocument.getDefaultTokenStream();
            TokenStream rawFileTokenizationTokenStream = rawFileTokenizationDocument.getDefaultTokenStream();
            assertEquals(regularTokenizationDocument.getValue(), regularTokenizationTokenStream.getTextValue());
            assertEquals(rawFileTokenizationDocument.getValue(), rawFileTokenizationTokenStream.getTextValue());

            assertEquals(regularTokenizationTokenStream.size(), rawFileTokenizationTokenStream.size());
            int tokenStreamLength = regularTokenizationTokenStream.size();
            for (int i = 0; i < tokenStreamLength; i++) {
                Token regularToken = regularTokenizationTokenStream.get(i);
                Token rawFileToken = rawFileTokenizationTokenStream.get(i);

                assertEquals(i, regularToken.getSequenceId());
                assertEquals(i, rawFileToken.getSequenceId());

                assertEquals(regularToken.getValue(), rawFileToken.getValue());
                String rawFileTokenValueFromText = rawFileTokenizationTokenStream.getTextValue().substring(rawFileToken.getCharOffset().getBegin(), rawFileToken.getCharOffset().getEnd());
                // should not have any surrounding whitespace
                assertEquals(rawFileTokenValueFromText.trim(), rawFileTokenValueFromText);
                String regularTokenValueFromText = regularTokenizationTokenStream.getTextValue().substring(regularToken.getCharOffset().getBegin(), regularToken.getCharOffset().getEnd());
                int lengthDifferenceDueToXmlEscapeCharacters = rawFileTokenValueFromText.length() - regularTokenValueFromText.length();
                assertEquals(
                        regularToken.getCharOffset().getEnd() - regularToken.getCharOffset().getBegin(),
                        rawFileToken.getCharOffset().getEnd() - rawFileToken.getCharOffset().getBegin() - lengthDifferenceDueToXmlEscapeCharacters
                );
                assertTrue(rawFileToken.getCharOffset().getBegin() > regularToken.getCharOffset().getBegin());
                assertTrue(rawFileToken.getCharOffset().getEnd() > regularToken.getCharOffset().getEnd());
                assertEquals(
                        // (one or both may contain whitespace)
                        StringEscapeUtils.unescapeXml(regularTokenValueFromText).replaceAll("\\s", ""),
                        // Manually escape any "&amp;" that is present in the raw-file-token-value-from-text.
                        // This is necessary in case we encounter XML/HTML compound escapes, such as "&amp;#39;".
                        StringEscapeUtils.unescapeXml(rawFileTokenValueFromText.replace("&amp;", "&")).replaceAll("\\s", "")
                );
            }
            List<ConversationElement> regularHltContentContainerConversationElements = regularHltContentContainer.getConversationElements();
            if (regularHltContentContainerConversationElements != null && !regularHltContentContainerConversationElements.isEmpty()) {
                List<ConversationElement> rawFileHltContentContainerConversationElements = rawFileHltContentContainer.getConversationElements();
                assertEquals(regularHltContentContainerConversationElements.size(), rawFileHltContentContainerConversationElements.size());
                int numConversationElements = regularHltContentContainerConversationElements.size();
                for (int i = 0; i < numConversationElements; i++) {
                    assertEquals(
                            regularHltContentContainerConversationElements.get(i).getMessageChunk().getTokenOffset(),
                            rawFileHltContentContainerConversationElements.get(i).getMessageChunk().getTokenOffset()
                    );
                    assertEquals(
                            regularHltContentContainerConversationElements.get(i).getAuthoredTime(),
                            rawFileHltContentContainerConversationElements.get(i).getAuthoredTime()
                    );
                    assertEquals(
                            regularHltContentContainerConversationElements.get(i).getAuthorId(),
                            rawFileHltContentContainerConversationElements.get(i).getAuthorId()
                    );
                }
            }

            List<Passage> regularHltContentContainerPassages = regularHltContentContainer.getPassages();
            if (regularHltContentContainerPassages != null && !regularHltContentContainerPassages.isEmpty()) {
                List<Passage> rawFileHltContentContainerPassages = rawFileHltContentContainer.getPassages();
                assertEquals(regularHltContentContainerPassages.size(), rawFileHltContentContainerPassages.size());
                int numPassages = regularHltContentContainerPassages.size();
                for (int i = 0; i < numPassages; i++) {
                    assertEquals(
                            regularHltContentContainerPassages.get(i).getTokenOffset(),
                            rawFileHltContentContainerPassages.get(i).getTokenOffset()
                    );
                }
            }
        }
    }
}

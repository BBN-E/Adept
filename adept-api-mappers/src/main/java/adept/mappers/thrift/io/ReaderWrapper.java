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

package adept.mappers.thrift.io;

import adept.serialization.XMLSerializer;
import adept.serialization.AbstractSerializer;

import java.nio.ByteBuffer;

import thrift.adept.io.*;
import thrift.adept.common.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.UnsupportedEncodingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import org.dozer.DozerBeanMapper;

// TODO: Auto-generated Javadoc
/**
 * Performs XML serialization.
 */
public class ReaderWrapper implements Reader.Iface {
	// Serializer instance
	/** The xstream. */
	private static adept.io.Reader reader = adept.io.Reader.getInstance();

    private static String language = "English";

	private DozerBeanMapper mapper;

	/**
	 * Instantiates a new ReaderWrapper
	 */
	public ReaderWrapper() {
		mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
	}

	@Override
	public String readConversationFile(String path, List<Utterance> utterances, List<String> speakers, String title) {
		return null;
		//return reader.readConversationFile(path, mapper.map(utterances, Utterance.class), speakers, title);
	}

	@Override
	public EREDocument readEREFile(String path, String docId) {
		System.out.println("HELLO");
		//System.out.println(reader.readEREFile(path, docId).getFullText());
		Boolean isNull = (mapper.map(reader.readEREFile(path, docId, language), EREDocument.class) == null);
		System.out.println(isNull);
		System.out.println("HELLO2");
		System.out.println(mapper.map(reader.readEREFile(path, docId, language), EREDocument.class).getFullText());
		return mapper.map(reader.readEREFile(path, docId, language), EREDocument.class);
	}

	@Override
	public HltContentContainer EREtoHltContentContainer(String EREPath, String XMLPath){
		return mapper.map(reader.EREtoHltContentContainer(EREPath, XMLPath, language), HltContentContainer.class);
	}

	@Override
	public List<Sentence> getSentences(CoNLLDocument conllDoc){
		return null;
		//return mapper.map(reader.getSentences(mapper.map(conllDoc, CoNLLDocument.class)), Sentence.class);
	}

	@Override
	public CoNLLDocument readCoNLLFile(String path){
		return mapper.map(reader.readCoNLLFile(path), CoNLLDocument.class);
	}

	@Override
	public HltContentContainer CoNLLtoHltContentContainer(String filePath){
		return mapper.map(reader.CoNLLtoHltContentContainer(filePath), HltContentContainer.class);
	}

	@Override
	public String getAbsolutePathFromClasspathOrFileSystem(String name){
		try {
			return reader.getAbsolutePathFromClasspathOrFileSystem(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String readFileIntoString(String path){
		return reader.readFileIntoString(path);
	}

	@Override
	public String readFileIntoLines(String fileName, List<String> lines){
		return reader.readFileIntoLines(fileName, lines);
	}

	@Override
	public List<String> fileToLines(String fileName){
		return reader.fileToLines(fileName);
	}

	@Override
	public String checkSurrogates(String text){
		return reader.checkSurrogates(text);
	}

}
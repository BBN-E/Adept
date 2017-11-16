package adept.kbapi;

/*-
 * #%L
 * adept-kb
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

import com.google.common.base.Optional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import adept.common.IType;
import adept.common.OntType;
import adept.common.Type;
import adept.io.Reader;

/**
 * A simple mapping between one ontology and another.
 *
 * Converts strings for class and role names.
 *
 * Supports notation for converting some relations to events.
 *
 * Supports adding a type to a target entity based on a class and role name.
 *
 * @author dkolas
 */
public class KBOntologyMap {
	private static KBOntologyMap richEREMap;
	private static KBOntologyMap tacMap;
	private static KBOntologyMap adeptIdentityMap;


	private KBOntologyModel ontologyModel;

	private Map<String, String> ontologyMap;
	private Map<String, String> reverseOntologyMap;
	public KBOntologyMap(Map<String, String> ontologyMap, Map<String, String>
			reverseOntologyMap) {
		this.ontologyMap = ontologyMap;
		this.reverseOntologyMap = reverseOntologyMap;
		this.ontologyModel = KBOntologyModel.instance();
	}

	private static HashMap<String, String> tacInverseMap;

	public Optional<OntType> getKBTypeForType(IType type) {
		String typeToMap = type.getType().toLowerCase();
		String kbType = ontologyMap.containsKey(typeToMap) ? ontologyMap.get(typeToMap) :
				(typeToMap.contains(".")?ontologyMap.get(typeToMap.substring(0,typeToMap.indexOf("."))):null);
		if (kbType == null) {
			return Optional.absent();
		}
		OntType ontType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, kbType);
		return Optional.fromNullable(ontType);
	}

	public Optional<? extends IType> getTypeForKBType(OntType type) {
		String adeptType = reverseOntologyMap.get(type.getType().toLowerCase());
		if (adeptType == null) {
			return Optional.absent();
		}
		return Optional.fromNullable(new Type(adeptType));
	}

	public Optional<String> getTypeStringForKBType(OntType type) {
		String adeptType = reverseOntologyMap.get(type.getType().toLowerCase());
		if (adeptType == null) {
			return Optional.absent();
		}
		return Optional.fromNullable(adeptType);
	}

	/**
	 * Return a type that needs to be added when asserting a type and role name.
	 *
	 * Ex: when converting city_of_residence to Resident, assert City on the
	 * location argument.
	 *
	 * Return Optional.absent if no additional type exists.
	 *
	 * @param relationType
	 * @param role
	 * @return
	 */
	public Optional<OntType> getAdditionalTypeForRoleTarget(IType relationType, IType role) {
		String key = relationType.getType().toLowerCase() + "." + role.getType().toLowerCase()
				+ ".type";
		if (ontologyMap.containsKey(key)) {
			return Optional.of(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, ontologyMap
					.get(key)));
		}
		return Optional.absent();
	}

	/**
	 * Return true if this relationType is mapped to an Event in the target
	 * ontology.
	 *
	 * @param relationType
	 * @return
	 */
	public boolean relationConvertsToEvent(IType relationType) {
		return ontologyModel.getLeafEventTypes().contains(ontologyMap.get(relationType.getType()));
	}

	/**
	 * Return the KB role associated with the source ontology relationType and
	 * role.
	 *
	 * Return Optional.absent if no mapping exists.
	 *
	 * @param relationType
	 * @param role
	 * @return
	 */
	public Optional<OntType> getKBRoleForType(IType relationType, IType role) {
		String roleToMap = role.getType().startsWith(relationType.getType() + ".") ? role.getType()
				: relationType.getType() + "." + role.getType();
		roleToMap = roleToMap.toLowerCase();
		String kbType = ontologyMap.containsKey(roleToMap) ? ontologyMap.get(roleToMap) :
				null;
		if (kbType == null) {
			return Optional.absent();
		}
		OntType ontType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, kbType);
		return Optional.fromNullable(ontType);
	}

	/**
	 * Return arg order associated with the source ontology relationType and
	 * role.
	 *
	 * Return Optional.absent if no mapping exists.
	 *
	 * @param relationType
	 * @param role
	 * @return
	 */
	public Optional<String> getTypeforKBRole(IType relationType, IType role) {
		String roleToMap = role.getType().startsWith(relationType.getType() + ".") ? role.getType()
				: relationType.getType() + "." + role.getType();
		roleToMap = roleToMap.toLowerCase();
		String kbType = reverseOntologyMap.containsKey(roleToMap) ? reverseOntologyMap.get(roleToMap) : null;
		if (kbType == null) {
			return Optional.absent();
		}
		return Optional.fromNullable(kbType);
	}

	/**
	 * Get a reference to the mapping from RichERE-&gt; Adept. This method is deprecated.
	 * Users are encouraged to use {@link #loadOntologyMap(String, String)} with specific
	 * ontology mapping files.
	 *
	 * @return
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Deprecated
	public static KBOntologyMap getRichEREOntologyMap() throws InvalidPropertiesFormatException,
			FileNotFoundException, IOException {
		if (richEREMap == null) {
			richEREMap = loadOntologyMap("adept/kbapi/rere-to-adept.xml",
					"adept/kbapi/adept-to-rere.xml");
		}
		return richEREMap;
	}

	/**
	 * Get a reference to the mapping from TAC-&gt; Adept. This method is deprecated. Users
	 * are encouraged to use {@link #loadOntologyMap(String, String)} with specific
	 * ontology mapping files.
	 *
	 * @return
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static HashMap<String, String> getTACInverseMap() throws InvalidPropertiesFormatException,
			FileNotFoundException, IOException {
		if (tacInverseMap == null) {
			tacInverseMap = readMapResource("adept/kbapi/tac-to-inverse.xml");
		}
		return tacInverseMap;
	}

	/**
	 * Get a reference to the mapping from TAC-&gt; Adept.
	 *
	 * @return
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Deprecated
	public static KBOntologyMap getTACOntologyMap() throws InvalidPropertiesFormatException,
			FileNotFoundException, IOException {
		if (tacMap == null) {
			tacMap = loadOntologyMap("adept/kbapi/stanford-to-adept.xml",
					"adept/kbapi/adept-to-stanford.xml");
		}
		return tacMap;
	}

	/**
	 * Get a reference to the mapping from Adept-&gt; Adept (Identity mapping).
	 * This is useful in case your artifacts are already compliant with Adept type-system,
	 * and you want a type-map object to pass on to various KB InsertionBuilders
	 *
	 * @return
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static KBOntologyMap getAdeptOntologyIdentityMap() throws
							     InvalidPropertiesFormatException,
							       FileNotFoundException, IOException {
		if (adeptIdentityMap == null) {
			adeptIdentityMap = loadOntologyMap("adept/kbapi/adept-to-adept.xml",
					"adept/kbapi/adept-to-adept.xml");
		}
		return adeptIdentityMap;
	}

	/**
	 *
	 * Load a KBOntologyMap from files.
	 *
	 * @param forwardResourcePath
	 * @param reverseResourcePath
	 * @return
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static KBOntologyMap loadOntologyMap(String forwardResourcePath,
			String reverseResourcePath) throws InvalidPropertiesFormatException,
			FileNotFoundException, IOException {
		HashMap<String, String> ontologyMap = readMapResource(forwardResourcePath);
		HashMap<String, String> reverseOntologyMap = readMapResource(reverseResourcePath);

		return new KBOntologyMap(ontologyMap, reverseOntologyMap);
	}

	/**
	 * @param forwardResourcePath
	 * @return
	 * @throws IOException
	 * @throws InvalidPropertiesFormatException
	 * @throws FileNotFoundException
	 */
	private static HashMap<String, String> readMapResource(String forwardResourcePath)
			throws IOException, InvalidPropertiesFormatException, FileNotFoundException {
		Properties mappingProps = new Properties();
		HashMap<String, String> ontologyMap = new HashMap<String, String>();
		mappingProps.loadFromXML(Reader.findStreamInClasspathOrFileSystem(forwardResourcePath));
		Set<String> keys = mappingProps.stringPropertyNames();
		for (String key : keys) {
			ontologyMap.put(key.toLowerCase(), mappingProps.getProperty(key));
		}
		return ontologyMap;
	}

}

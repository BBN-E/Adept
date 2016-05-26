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

package adept.common;
import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Preconditions;
import com.hp.hpl.jena.rdf.model.Resource;


/**
 * The Class OntType, which represents one concept
 * from an ontology, where the ontology formally
 * represents knowledge as a set of concepts within
 * a domain using a shared vocabulary.
 */
public class OntType implements IType {
    
    /**
     * The ontology.
     */
    private final String namespace;

    /**
     * The ontClass name.
     */
    private final String type;

    /**
     * Constructor.
     *
     * @param namespace
     * @param type
     */
    public OntType(String namespace, String type) {
        checkArgument(namespace != null);
        checkArgument(type != null);
        this.namespace = namespace;
        this.type = type;
    }

    /**
	 * @param typeResource
	 */
	public OntType(Resource typeResource) {
		Preconditions.checkNotNull(typeResource);
		String uri = typeResource.getURI();
		this.namespace = uri.split("#")[0]+"#";
		this.type = uri.split("#")[1];
	}

	/**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the uri of the type.
     *
     * @return the uri string
     */
    public String getURI() {
        return String.format("%s%s", namespace, type);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof OntType))
            return false;
        OntType ontType = (OntType) obj;
        return (ontType.getType().equals(this.getType()));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getType().hashCode();
    }

}

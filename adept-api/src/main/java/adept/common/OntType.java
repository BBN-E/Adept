package adept.common;
/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 *
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntClass;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class OntType, which represents one concept
 * from an ontology, where the ontology formally
 * represents knowledge as a set of concepts within
 * a domain using a shared vocabulary.
 */
public class OntType implements IType {


    /**
     * The ontClass.
     */
    @XStreamOmitField
    private OntClass ontClass;
    // TODO make final
    
    /**
     * The ontology.
     */
    private final String ontology;

    /**
     * The ontClass name.
     */
    private final String ontClassName;

    /**
     * Constructor.
     *
     * @param model    the model
     * @param ontology the ontology
     * @param ontClass the ontClass
     */
    public OntType(OntModel model, String ontology, String ontClass) {
        checkArgument(model != null);
        checkArgument(ontology != null);
        checkArgument(ontClass != null);
        this.ontology = ontology;
        this.ontClass = model.getOntClass(ontClass);
        if (this.ontClass == null) {
            this.ontClass = model.getOntClass(ontClass.split("#")[0] + "#" + "OTH");
            this.ontClassName = "OTH";
        } else {
            this.ontClassName = ontClass.split("#")[1];
        }
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        if (this.ontClass == null)
            this.ontClass = ((OntType) OntTypeFactory.getInstance().getType(ontology, ontClassName)).getOntClass();
        //return null;
        return ontClass.toString();
    }

    /**
     * Gets the ont class.
     *
     * @return the ontClass
     */
    public OntClass getOntClass() {
        //getType();
        return this.ontClass;
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

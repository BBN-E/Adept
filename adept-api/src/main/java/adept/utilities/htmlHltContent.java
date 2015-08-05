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

package adept.utilities;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * The Class htmlHltContent.
 */
public class htmlHltContent
{
    
    /**
     * Transform.
     *
     * @param xml the xml
     * @param xslt the xslt
     * @param html the html
     * @throws TransformerException the transformer exception
     */
    public void transform(String xml, String xslt, String html) throws TransformerException
    {
        System.out.println("Transforming " + xml + " to " + html);
        TransformerFactory factory = TransformerFactory.newInstance("org.apache.xalan.processor.TransformerFactoryImpl", null);
        Transformer transformer = factory.newTransformer(new StreamSource(xslt));
        transformer.transform(new StreamSource(xml), new StreamResult(html));
        System.out.println("Transformation complete!");
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Incorrect arguments:\nusage: serializedHLTCC.xml HLTCC.xslt output.html");
            return;
        }

        String xml = args[0];
        String xslt = args[1];
        String html = args[2];


        try
        {
            htmlHltContent hhc = new htmlHltContent();
            hhc.transform(xml, xslt, html);
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
        }
    }
}
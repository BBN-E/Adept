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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import adept.common.Pair;


/**
 * AMR is a rooted, labeled graph to represent semantics.
 * This class has the following members:
 * <ul>
 * <li>
 * nodes: list of nodes in the graph. Its element is the name of the ith node. For example, a node
 *        name could be "a1", "b", "g2", etc.
 * <li>
 * node_values: list of node labels (values) of the graph. Its ith element is the value associated with
 *              node i in nodes list. In AMR, such value is usually a semantic concept (e.g. "boy", "want-01")
 * <li>
 * root: root node name
 * <li>
 * relations: list of edges connecting two nodes in the graph. Each entry is a link between two nodes,
 *            i.e. a triple &lt;relation name, node1 name, node 2 name&gt;. In AMR, such link denotes the
 *            relation between two semantic concepts. For example, "arg0" means that one of the
 *            concepts is the 0th argument of the other.
 * <li>
 * attributes: list of edges connecting a node to an attribute name and its value. For example, if the
 *             polarity of some node is negative, there should be an edge connecting this node and "-".
 *             A triple &lt;attribute name, node name, attribute value&gt; is used to represent such
 *             attribute. It can also be viewed as a relation.
 * </ul>
 */
public class AMR {

  private static final Log log = LogFactory.getLog(AMR.class);

  private List<String> nodes;
  private List<String> nodeValues;
  private String root;
  private List<Map<String, String>> relations;
  private List<Map<String, String>> attributes;

  /**
   *
   * @param nodeList names of nodes in AMR graph, e.g. "a11", "n"
   * @param nodeValueList values of nodes in AMR graph, e.g. "group" for a node named "g"
   * @param relationList list of relations between two nodes
   * @param attributeList list of attributes (links between one node and one constant value)
   */
  private AMR(List<String> nodeList, List<String> nodeValueList,
      List<Map<String, String>> relationList, List<Map<String, String>> attributeList) {
    // initialize the AMR graph nodes using list of nodes name
    // root, by default, is the first in var_list

    if (nodeList == null) {
      nodes = Lists.newArrayList();
      root = null;
    } else {
      nodes = nodeList;
      if (!nodeList.isEmpty()) {
        root = nodeList.get(0);
      } else {
        root = null;
      }
    }

    if (nodeValueList == null) {
      nodeValues = Lists.newArrayList();
    } else {
      nodeValues = nodeValueList;
    }

    if (relationList == null) {
      relations = Lists.newArrayList();
    } else {
      relations = relationList;
    }

    if (attributeList == null) {
      attributes = Lists.newArrayList();
    } else {
      attributes = attributeList;
    }

  }

  /**
   * Get the triples in three lists.
   * instance_triple: a triple representing an instance. E.g. instance(w, want-01)
   * attribute_triple: relation of attributes, e.g. polarity(w, -)
   * and relation triple, e.g. arg0 (w, b)
   */
  /*
  public void getTriples() {
    List<Triple> instanceTriples = Lists.newArrayList();
    List<Triple> relationTriples = Lists.newArrayList();
    List<Triple> attributeTriples = Lists.newArrayList();
    for (int i = 0; i < nodes.size(); i++) {
      instanceTriples.add(new Triple("instance", nodes.get(i), nodeValues.get(i)));
      // key is the other node this node has relation with
      // value is relation name
      for (Map.Entry<String, String> e : relations.get(i).entrySet()) {
        relationTriples.add(new Triple(e.getValue(), nodes.get(i), e.getKey()));
      }
      // key is the attribute name
      // value is the attribute value
      for (Map.Entry<String, String> e : attributes.get(i).entrySet()) {
        attributeTriples.add(new Triple(e.getKey(), nodes.get(i), e.getValue()));
      }
    }
    return instanceTriple, attributeTriple, relationTriple;
  }
  */

  public static AMR parseAMRLine(String line) {
    // Current state. It denotes the last significant symbol encountered. 1 for (, 2 for : 2 for /,
    // and 0 for start state or )
    // Last significant symbol is ( -- start processing node name
    // Last significant symbol is : -- start processing relation name
    // Last significant symbol is / -- start processing node value (concept name)
    // Last significant symbol is ) -- current node processing is complete
    // Note that if these symbols are inside parenthesis, they are not significant symbols.
    int state = 0;
    // node stack for parsing
    Stack<String> stack = new Stack<>();
    // current not-yet-reduced character sequence
    List<Character> curCharseq = Lists.newArrayList();
    // key: node name value: node value
    Map<String, String> nodeDict = Maps.newHashMap();
    // node name list (order: occurrence of the node)
    List<String> nodeNameList = Lists.newArrayList();
    // key: node name, value: list of (relation name, the other node name)
    Map<String, List<Pair<String, String>>> nodeRelationDict1 =  Maps.newHashMap();
    // key: node name, value: list of (attribute name, const value) or (relation name, unseen node name)
    Map<String, List<Pair<String, String>>> nodeRelationDict2 =  Maps.newHashMap();
    // current relation name
    String curRelationName = "";
    // having unmatched quote string
    boolean inQuote = false;

    String nodeValue = "";

    String trimmed = line.trim();
    for (int i = 0; i < trimmed.length(); i++) {
      char c = trimmed.charAt(i);
      if (c == ' ') {
        // allow space in relation name
        if (state == 2) {
          curCharseq.add(c);
          continue;
        }
      }
      if (c == '\"') {
        // flip inQuote value when a quote symbol is encountered
        inQuote = !inQuote;
      } else if (c == '(') {
        // not significant symbol if inside quote
        if (inQuote)
          continue;
        // get the attribute name
        // e.g. :arg0 (x ...
        // at this point we get "arg0"
        if (state == 2) {
          // in this state, current relation name should be empty
          if (!curRelationName.isEmpty()) {
            log.error("Format error when processing " + trimmed.substring(0, i+1));
            return null;
          }
          // update current relation name for future use
          curRelationName = getStringRepresentation(curCharseq).trim();
          curCharseq.clear();
        }
        state = 1;
      } else if (c == ':') {
        // not significant symbol if inside quote
        if (inQuote)
          continue;
        // Last significant symbol is '/'. Now we encounter ':'
        // Example:
        // :OR (o2 / *OR*
        //    :mod (o3 / official)
        // gets node value "*OR*" at this point
        if (state == 3) {
          nodeValue = getStringRepresentation(curCharseq);
          // clear current char sequence
          curCharseq.clear();
          // pop node name ("o2" is the above example)
          String curNodeName = stack.peek();
          // update node name/value map
          nodeDict.put(curNodeName, nodeValue);
        }
        // Last significant symbol is ':'. Now we encounter ':"
        // Example:
        // :op1 w :quant 30
        // or :day 14 :month 3
        // the problem is that we cannot decide if node value is attribute value (constant)
        // or node value (variable) at this moment
        else if (state == 2) {
          String tmpAttrValue = getStringRepresentation(curCharseq);
          curCharseq.clear();
          String [] parts = tmpAttrValue.split("\\s+");
          if (parts.length < 2) {
            log.error("Error in processing " + trimmed.substring(0, i+1));
            return null;
          }
          // For the above example, node name is "op1", and node value is "w"
          // Note that this node name might not be encountered before
          String relationName = parts[0].trim();
          String relationValue = parts[1].trim();
          // We need to link upper level node to the current
          // top of stack is upper level node
          if (stack.isEmpty()) {
            log.error("Error in processing " + trimmed.substring(0, i+1) + relationName + relationValue);
            return null;
          }
          // if we have not seen this node name before
          if (!nodeDict.containsKey(nodeValue)) {
            nodeRelationDict2.get(stack.peek()).add(new Pair<>(relationName, relationValue));
          } else {
            nodeRelationDict1.get(stack.peek()).add((new Pair<>(relationName, relationValue)));
          }
        }
        state = 2;
      } else if (c == '/') {
        if (inQuote)
          continue;
        // Last significant symbol is '('. Now we encounter '/'
        // Example:
        // (d / default-01
        // get "d" here
        if (state == 1) {
          String nodeName = getStringRepresentation(curCharseq);
          curCharseq.clear();
          // if this node name is already in node_dict, it is duplicate
          if (nodeDict.containsKey(nodeName)) {
            log.error("Duplicate node name " + nodeName + " in parsing AMR");
            return null;
          }
          // push the node name to stack
          String stackMinus2 = stack.peek();
          stack.push(nodeName);
          // add it to node name list
          nodeNameList.add(nodeName);
          // if this node is part of the relation
          // Example:
          // :arg1 (n / nation)
          // curRelationName is arg1
          // nodeName is n
          // we have a relation arg1(upper level node, n)
          if (!curRelationName.isEmpty()) {
            // if relation name ends with "-of", e.g. "arg0-of" b,
            // we can also say b is "arg0" a.
            // If the relation name ends with "-of", we store the reverse relation.
            if (!curRelationName.endsWith("-of")) {
              // stack[-2] is upper_level node we encountered, as we just add nodeName to stack
              nodeRelationDict1.get(stackMinus2).add(new Pair<>(curRelationName, nodeName));
            } else {
              // cur_relation_name[:-3] is to delete "-of"
              String reverseRelation = curRelationName.substring(0, curRelationName.length() - 3);
              nodeRelationDict1.get(nodeName).add(new Pair<>(reverseRelation, stackMinus2));
            }
            // clear curRelationName
            curRelationName = "";
          }
        } else {
          // error if in other state
          log.error("Error in parsing AMR " + trimmed.substring(0, i + 1));
          return null;
        }
        state = 3;

      } else if (c == ')') {
        if (inQuote)
          continue;
        // stack should be non-empty to find upper level node
        if (stack.isEmpty()) {
          log.error("Unmatched parenthesis at position " + i + " in processing " + trimmed
              .substring(0, i + 1));
          return null;
        }
        // Last significant symbol is ':'. Now we encounter ')'
        // Example:
        // :op2 "Brown") or :op2 w)
        // get \"Brown\" or w here
        if (state == 2) {
          String tmpAttrValue = getStringRepresentation(curCharseq);
          curCharseq.clear();
          String[] parts = tmpAttrValue.split("\\s+");
          if (parts.length < 2) {
            log.error("Error in processing " + trimmed.substring(0, i + 1) + tmpAttrValue);
            return null;
          }
          String relationName = parts[0].trim();
          String relationValue = parts[1].trim();
          // store reverse of the relation
          // we are sure relationValue is a node here, as "-of" relation is only between 2 nodes
          if (relationName.endsWith("-of")) {
            // cur_relation_name[:-3] is to delete "-of"
            String reverseRelation = curRelationName.substring(0, curRelationName.length() - 3);
            nodeRelationDict1.get(relationValue).add(new Pair<>(reverseRelation, stack.peek()));
          }
          // attribute value not seen before
          // Note that it might be a constant attribute value, or an unseen node
          // process this after we have seen all the node names
          else if (!nodeDict.containsKey(relationValue)) {
            nodeRelationDict2.get(stack.peek()).add(new Pair<>(relationName, relationValue));
          } else {
            nodeRelationDict1.get(stack.peek()).add(new Pair<>(relationName, relationValue));
          }
        }
        // Last significant symbol is '/'. Now we encounter ')'
        // Example:
        // :arg1 (n / nation)
        // we get "nation" here
        else if (state == 3) {
          nodeValue = getStringRepresentation(curCharseq);
          curCharseq.clear();
          String curNodeName = stack.peek();
          // map node name to its value
          nodeDict.put(curNodeName, nodeValue);
        }
        // pop from stack, as the current node has been processed
        stack.pop();
        curRelationName = "";
        state = 0;
      }
      else {
        // not significant symbols, so we just shift.
        curCharseq.add(c);
      }
    }

    // create data structures to initialize an AMR
    List<String> nodeValueList = Lists.newArrayList();
    List<Map<String, String>> relationList = Lists.newArrayList();
    List<Map<String, String>> attributeList = Lists.newArrayList();
    for (String v : nodeNameList) {
      if (!nodeDict.containsKey(v)) {
        log.error("Error: node name not found " + v);
        return null;
      } else {
        nodeValueList.add(nodeDict.get(v));
      }
      // build relation map and attribute map for this node
      Map<String, String> relationDict = Maps.newHashMap();
      Map<String, String> attributeDict = Maps.newHashMap();
      if (nodeRelationDict1.containsKey(v)) {
        for (Pair<String, String> v1 : nodeRelationDict1.get(v)) {
          relationDict.put(v1.getR(), v1.getL());
        }
      }
      if (nodeRelationDict2.containsKey(v)) {
        for (Pair<String, String> v2: nodeRelationDict2.get(v)) {
          // if value is in quote, it is a constant value
          // strip the quote and put it in attribute map
          String v2_0 = v2.getL();
          String v2_1 = v2.getR();
          if (v2_1.startsWith("\"") && v2_1.endsWith("\"")) {
            attributeDict.put(v2_0, v2_1.substring(1, v2_1.length()-1));
          }
          // if value is a node name
          else if (nodeDict.containsKey(v2_1)) {
            relationDict.put(v2_1, v2_0);
          } else {
            relationDict.put(v2_0, v2_1);
          }
        }
      }
      // each node has a relation map and attribute map
      relationList.add(relationDict);
      attributeList.add(attributeDict);
    }
    // add TOP as an attribute. The attribute value is the top node value
    attributeList.get(0).put("TOP", nodeValueList.get(0));
    return new AMR(nodeNameList, nodeValueList, relationList, attributeList);
  }

  public static String getStringRepresentation(List<Character> list) {
    StringBuilder builder = new StringBuilder(list.size());
    for (Character ch: list) {
      builder.append(ch);
    }
    return builder.toString();
  }
}

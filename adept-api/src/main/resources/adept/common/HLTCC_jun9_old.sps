<!-- Copyright 2012-2017 Raytheon BBN Technologies Corp. All Rights Reserved. -->

<?xml version="1.0" encoding="UTF-8"?>
<structure version="16" xsltversion="1" html-doctype="HTML4 Transitional" compatibility-view="IE9" relativeto="*SPS" encodinghtml="UTF-8" encodingrtf="ISO-8859-1" encodingpdf="UTF-8" useimportschema="1" embed-images="1" enable-authentic-scripts="1" authentic-scripts-in-debug-mode-external="0" generated-file-location="DEFAULT">
	<parameters/>
	<schemasources>
		<namespaces/>
		<schemasources>
			<xsdschemasource name="XML" main="1" schemafile="HLTCC.xsd" workingxmlfile="\\mercury-04\u39\DEFT\users\jwatson\sandbox\deft\uiuc\target\test-classes\edu\uiuc\profiling\output\STILLALONEWOLF_20050102.1100.eng.LDC2005E83.output.xml"/>
		</schemasources>
	</schemasources>
	<modules/>
	<flags>
		<scripts/>
		<mainparts/>
		<globalparts/>
		<designfragments/>
		<pagelayouts/>
		<xpath-functions/>
	</flags>
	<scripts>
		<script language="javascript">function highlightSource(row) {
	var content = document.getElementById(&quot;documentContent&quot;);
	var regex = /(&lt;([^&gt;]+)&gt;)/ig;
	var cleanedContent = content.innerHTML.replace(regex, &quot;&quot;);
	
	var startIndex = row.children[1].innerHTML;
	startIndex = parseInt(startIndex);
	var endIndex = row.children[2].innerHTML;
	endIndex = parseInt(endIndex);

	var contentArray = cleanedContent.split(/\s+/);
	
	var highlightedContent = &quot;&quot;;
	for(var i = 0; i &lt; contentArray.length; i++) {
		if(i == startIndex) {
			highlightedContent += &quot;&lt;span style=\&quot;background-color: yellow\&quot;&gt;&quot;;
		} 
		if (i  == (1 + endIndex) ) {
			highlightedContent += &quot;&lt;/span&gt;&quot;;
		}
		highlightedContent += (&quot; &quot; + contentArray[i]);
	}

	content.innerHTML = highlightedContent;
	
}</script>
	</scripts>
	<script-project>
		<Project version="3" app="AuthenticView"/>
	</script-project>
	<importedxslt/>
	<globalstyles/>
	<mainparts>
		<children>
			<globaltemplate subtype="main" match="/">
				<document-properties/>
				<styles color="#222222"/>
				<children>
					<documentsection>
						<properties columncount="1" columngap="0.50in" headerfooterheight="fixed" pagemultiplepages="0" pagenumberingformat="1" pagenumberingstartat="auto" pagestart="next" paperheight="11in" papermarginbottom="0.79in" papermarginfooter="0.30in" papermarginheader="0.30in" papermarginleft="0.60in" papermarginright="0.60in" papermargintop="0.79in" paperwidth="8.50in"/>
						<watermark>
							<image transparency="50" fill-page="1" center-if-not-fill="1"/>
							<text transparency="50"/>
						</watermark>
					</documentsection>
					<template subtype="source" match="XML">
						<children>
							<template subtype="element" match="adept.common.HltContentContainer">
								<children>
									<newline/>
									<bookmark>
										<action>
											<none/>
										</action>
										<bookmark>
											<fixtext value="Table of Contents"/>
										</bookmark>
									</bookmark>
									<text fixtext="Table of Contents">
										<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
									</text>
									<list>
										<children>
											<listrow>
												<children>
													<link conditional-processing="count(//tokenStream/adept.common.TokenStream/default/document) &gt; 0">
														<children>
															<text fixtext="Document"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Document Value"/>
														</hyperlink>
													</link>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(coreferences/adept.common.Coreference/resolvedEntityMentions/adept.common.EntityMention) &gt; 0">
														<children>
															<text fixtext="Entity Mentions and Coreferences"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Entity Mentions"/>
														</hyperlink>
													</link>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(relations) &gt; 0">
														<children>
															<text fixtext="Relations"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Relations"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(sarcasms) &gt; 0">
														<children>
															<text fixtext="Sarcasms"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Sarcasms"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(opinions) &gt; 0">
														<children>
															<text fixtext="Opinions"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Opinions"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(committedBeliefs) &gt; 0">
														<children>
															<text fixtext="Committed Beliefs"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Committed Beliefs"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(syntacticChunks) &gt; 0">
														<children>
															<text fixtext="Syntactic Chunks"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Syntactic Chunks"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(paraphrases) &gt; 0">
														<children>
															<text fixtext="Paraphrases"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Paraphrases"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(triples) &gt; 0">
														<children>
															<text fixtext="Triples"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Triples"/>
														</hyperlink>
													</link>
													<text fixtext=" "/>
												</children>
											</listrow>
											<listrow>
												<children>
													<link conditional-processing="count(/descendant::tokenStream[1]) &gt; 0">
														<children>
															<text fixtext="Tokens"/>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="#Tokens"/>
														</hyperlink>
													</link>
												</children>
											</listrow>
										</children>
									</list>
									<template subtype="userdefined" match="//tokenStream">
										<children>
											<template subtype="element" match="adept.common.TokenStream">
												<children>
													<template subtype="element" match="default">
														<children>
															<template subtype="element" match="document">
																<children>
																	<paragraph paragraphtag="center">
																		<styles background="#323d40" color="white"/>
																		<children>
																			<text fixtext="Document">
																				<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
																			</text>
																			<text fixtext="   "/>
																			<link>
																				<children>
																					<image>
																						<properties align="bottom"/>
																						<target>
																							<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																						</target>
																					</image>
																				</children>
																				<action>
																					<none/>
																				</action>
																				<hyperlink>
																					<fixtext value="help.htm#Document"/>
																				</hyperlink>
																			</link>
																		</children>
																	</paragraph>
																	<bookmark>
																		<action>
																			<none/>
																		</action>
																		<bookmark>
																			<fixtext value="Document Value"/>
																		</bookmark>
																	</bookmark>
																	<newline/>
																	<text fixtext="Type: "/>
																	<template subtype="element" match="docType">
																		<children>
																			<content subtype="regular"/>
																		</children>
																		<variables/>
																	</template>
																	<newline/>
																	<text fixtext="URI: "/>
																	<template subtype="element" match="uri">
																		<children>
																			<content subtype="regular"/>
																		</children>
																		<variables/>
																	</template>
																	<newline/>
																	<text fixtext="Language: "/>
																	<template subtype="element" match="language">
																		<children>
																			<content subtype="regular"/>
																		</children>
																		<variables/>
																	</template>
																	<newline/>
																	<text fixtext="Headline: "/>
																	<template subtype="element" match="headline">
																		<children>
																			<content subtype="regular"/>
																		</children>
																		<variables/>
																	</template>
																	<newline/>
																	<newline/>
																	<template subtype="element" match="value">
																		<children>
																			<content subtype="regular">
																				<properties id="documentContent"/>
																			</content>
																		</children>
																		<variables/>
																	</template>
																	<newline/>
																	<newline/>
																	<link>
																		<children>
																			<paragraph paragraphtag="center">
																				<children>
																					<text fixtext="Go to Top of Page"/>
																				</children>
																			</paragraph>
																		</children>
																		<action>
																			<none/>
																		</action>
																		<hyperlink>
																			<fixtext value="#Table of Contents"/>
																		</hyperlink>
																	</link>
																</children>
																<variables/>
															</template>
														</children>
														<variables/>
													</template>
												</children>
												<variables/>
											</template>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="coreferences">
										<children>
											<template subtype="element" match="adept.common.Coreference">
												<children>
													<template subtype="element" match="resolvedEntityMentions">
														<children>
															<bookmark>
																<action>
																	<none/>
																</action>
																<bookmark>
																	<fixtext value="Entity Mentions"/>
																</bookmark>
															</bookmark>
															<newline/>
															<paragraph paragraphtag="center">
																<styles background="#323d40" color="white"/>
																<children>
																	<text fixtext="Entity Mentions and Coreferences">
																		<styles font-family="Arial" font-size="14pt" font-weight="bolder"/>
																	</text>
																	<text fixtext="   "/>
																	<link>
																		<children>
																			<image>
																				<properties align="bottom"/>
																				<target>
																					<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																				</target>
																			</image>
																		</children>
																		<action>
																			<none/>
																		</action>
																		<hyperlink>
																			<fixtext value="help.htm#Entity_Mentions"/>
																		</hyperlink>
																	</link>
																</children>
															</paragraph>
															<paragraph paragraphtag="center">
																<children>
																	<tgrid add-row-expand-collapse-buttons="1">
																		<styles border-collapse="collapse" margin-top="10px"/>
																		<children>
																			<tgridbody-cols>
																				<children>
																					<tgridcol>
																						<properties align="center"/>
																					</tgridcol>
																					<tgridcol>
																						<properties align="center"/>
																					</tgridcol>
																					<tgridcol>
																						<properties width="384"/>
																					</tgridcol>
																					<tgridcol>
																						<properties align="center"/>
																					</tgridcol>
																					<tgridcol>
																						<properties align="center"/>
																					</tgridcol>
																					<tgridcol>
																						<properties align="center"/>
																					</tgridcol>
																					<tgridcol/>
																					<tgridcol>
																						<properties width="384"/>
																					</tgridcol>
																				</children>
																			</tgridbody-cols>
																			<tgridheader-rows>
																				<properties align="left"/>
																				<styles margin="2px"/>
																				<children>
																					<tgridrow>
																						<properties align="left" bgcolor="#697376"/>
																						<children>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="ResolvedEntityId">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="SequenceId">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin" text-align="left"/>
																								<children>
																									<text fixtext="Entity Mention Value">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="Begin">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="End">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="MentionType">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="EntityType">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<styles border-bottom="solid gray thin" border-bottom-width="thin" border-left="solid gray thin" border-top="solid gray thin"/>
																								<children>
																									<text fixtext="Context">
																										<styles color="white"/>
																									</text>
																								</children>
																							</tgridcell>
																						</children>
																					</tgridrow>
																				</children>
																			</tgridheader-rows>
																			<tgridbody-rows>
																				<children>
																					<template subtype="element" filter="following::adept.common.EntityMention" match="adept.common.EntityMention">
																						<children>
																							<tgridrow expand-collapse-level="1">
																								<properties _xbgcolor="$color" onclick="highlightSource(this)"/>
																								<children>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin"/>
																										<children>
																											<template subtype="element" match="entityIdDistribution">
																												<children>
																													<template subtype="element" match="entry">
																														<children>
																															<template subtype="element" match="long">
																																<children>
																																	<content subtype="regular">
																																		<format basic-type="xsd" datatype="long"/>
																																	</content>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin"/>
																										<children>
																											<template subtype="element" match="sequenceId">
																												<children>
																													<content subtype="regular">
																														<format basic-type="xsd" datatype="long"/>
																													</content>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-right="5px" text-align="left"/>
																										<children>
																											<link>
																												<children>
																													<template subtype="element" match="value">
																														<children>
																															<content subtype="regular"/>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<action>
																													<none/>
																												</action>
																												<hyperlink>
																													<xpath value="attributes/entry/string[starts-with(.,&quot;http://&quot;)]"/>
																												</hyperlink>
																											</link>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin"/>
																										<children>
																											<template subtype="element" match="tokenOffset">
																												<children>
																													<template subtype="element" match="begin">
																														<children>
																															<content subtype="regular">
																																<format basic-type="xsd" datatype="long"/>
																															</content>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin"/>
																										<children>
																											<template subtype="element" match="tokenOffset">
																												<children>
																													<template subtype="element" match="end">
																														<children>
																															<content subtype="regular">
																																<format basic-type="xsd" datatype="long"/>
																															</content>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin"/>
																										<children>
																											<template subtype="element" match="mentionType">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin"/>
																										<children>
																											<template subtype="element" match="entityType">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																									<tgridcell>
																										<styles border-bottom="solid gray thin" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" text-align="left"/>
																										<children>
																											<template subtype="element" match="context">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridcell>
																								</children>
																							</tgridrow>
																						</children>
																						<variables>
																							<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																						</variables>
																					</template>
																				</children>
																			</tgridbody-rows>
																		</children>
																	</tgrid>
																</children>
															</paragraph>
															<link>
																<children>
																	<paragraph paragraphtag="center">
																		<children>
																			<text fixtext="Go to Top of  Page"/>
																		</children>
																	</paragraph>
																</children>
																<action>
																	<none/>
																</action>
																<hyperlink>
																	<fixtext value="#Table of Contents"/>
																</hyperlink>
															</link>
														</children>
														<variables/>
													</template>
												</children>
												<variables/>
											</template>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="relations">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Relations"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Relations">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Relations"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-collapse="collapse" border-left="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<styles border-collapse="collapse"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="RelationId">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Relation Type">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Begin">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="End">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Argument Value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Argument Type">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Confidence">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Context">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.Relation">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin" border-right="solid gray thin"/>
																						<children>
																							<template subtype="element" match="relationId">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" datatype="long"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin" border-right="solid gray thin" text-align="left"/>
																						<children>
																							<template subtype="element" match="tupleType">
																								<children>
																									<text fixtext="
"/>
																									<content subtype="regular"/>
																								</children>
																								<variables>
																									<variable name="text" value="translate(tupleType  ,  &quot;-&quot; ,  &quot; &quot; )" valuetype="&lt;auto&gt;"/>
																								</variables>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin" border-right="solid gray thin"/>
																						<children>
																							<tgrid>
																								<children>
																									<tgridbody-cols>
																										<children>
																											<tgridcol/>
																										</children>
																									</tgridbody-cols>
																									<tgridbody-rows>
																										<children>
																											<template subtype="element" match="arguments">
																												<children>
																													<template subtype="element" match="adept.common.Argument">
																														<children>
																															<template subtype="element" match="argumentDistribution">
																																<children>
																																	<template subtype="element" match="list">
																																		<children>
																																			<template subtype="element" match="adept.common.Pair">
																																				<children>
																																					<template subtype="element" match="l">
																																						<children>
																																							<template subtype="element" match="tokenOffset">
																																								<children>
																																									<template subtype="element" match="begin">
																																										<children>
																																											<tgridrow>
																																												<properties _xbgcolor="$color"/>
																																												<children>
																																													<tgridcell>
																																														<styles border-bottom-color="gray" border-bottom-width="thin"/>
																																														<children>
																																															<content subtype="regular">
																																																<format basic-type="xsd" datatype="long"/>
																																															</content>
																																														</children>
																																													</tgridcell>
																																												</children>
																																											</tgridrow>
																																										</children>
																																										<variables/>
																																									</template>
																																								</children>
																																								<variables/>
																																							</template>
																																						</children>
																																						<variables/>
																																					</template>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridbody-rows>
																								</children>
																							</tgrid>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin"/>
																						<children>
																							<tgrid>
																								<styles border-right="solid gray thin"/>
																								<children>
																									<tgridbody-cols>
																										<children>
																											<tgridcol/>
																										</children>
																									</tgridbody-cols>
																									<tgridbody-rows>
																										<children>
																											<template subtype="element" match="arguments">
																												<children>
																													<template subtype="element" match="adept.common.Argument">
																														<children>
																															<template subtype="element" match="argumentDistribution">
																																<children>
																																	<template subtype="element" match="list">
																																		<children>
																																			<template subtype="element" match="adept.common.Pair">
																																				<children>
																																					<template subtype="element" match="l">
																																						<children>
																																							<template subtype="element" match="tokenOffset">
																																								<children>
																																									<template subtype="element" match="end">
																																										<children>
																																											<tgridrow>
																																												<properties _xbgcolor="$color"/>
																																												<children>
																																													<tgridcell>
																																														<styles border-bottom-color="gray" border-bottom-width="thin"/>
																																														<children>
																																															<content subtype="regular">
																																																<format basic-type="xsd" datatype="long"/>
																																															</content>
																																														</children>
																																													</tgridcell>
																																												</children>
																																											</tgridrow>
																																										</children>
																																										<variables/>
																																									</template>
																																								</children>
																																								<variables/>
																																							</template>
																																						</children>
																																						<variables/>
																																					</template>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridbody-rows>
																								</children>
																							</tgrid>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin" border-right="solid gray thin" text-align="left"/>
																						<children>
																							<template subtype="element" match="arguments">
																								<children>
																									<template subtype="element" match="adept.common.Argument">
																										<children>
																											<template subtype="element" match="argumentDistribution">
																												<children>
																													<template subtype="element" match="list">
																														<children>
																															<template subtype="element" match="adept.common.Pair">
																																<children>
																																	<template subtype="element" match="l">
																																		<children>
																																			<template subtype="element" match="value">
																																				<children>
																																					<tgrid>
																																						<children>
																																							<tgridbody-cols>
																																								<children>
																																									<tgridcol/>
																																								</children>
																																							</tgridbody-cols>
																																							<tgridbody-rows>
																																								<children>
																																									<tgridrow>
																																										<properties _xbgcolor="$color"/>
																																										<children>
																																											<tgridcell>
																																												<styles border-bottom-color="gray" border-bottom-width="thin" text-align="left"/>
																																												<children>
																																													<content subtype="regular"/>
																																												</children>
																																											</tgridcell>
																																										</children>
																																									</tgridrow>
																																								</children>
																																							</tgridbody-rows>
																																						</children>
																																					</tgrid>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin"/>
																						<children>
																							<tgrid>
																								<children>
																									<tgridbody-cols>
																										<children>
																											<tgridcol/>
																										</children>
																									</tgridbody-cols>
																									<tgridbody-rows>
																										<children>
																											<template subtype="element" match="arguments">
																												<children>
																													<template subtype="element" match="adept.common.Argument">
																														<children>
																															<template subtype="element" match="argumentType">
																																<children>
																																	<tgridrow>
																																		<properties _xbgcolor="$color"/>
																																		<children>
																																			<tgridcell>
																																				<styles border-bottom-color="gray" border-bottom-width="thin"/>
																																				<children>
																																					<template subtype="element" match="type">
																																						<children>
																																							<content subtype="regular"/>
																																						</children>
																																						<variables/>
																																					</template>
																																				</children>
																																			</tgridcell>
																																		</children>
																																	</tgridrow>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																												<variables/>
																											</template>
																										</children>
																									</tgridbody-rows>
																								</children>
																							</tgrid>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin" border-left="solid gray thin" border-right="solid gray thin"/>
																						<children>
																							<template subtype="element" match="confidence">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" string="########.##" datatype="float"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles border-bottom-color="gray" border-bottom-style="solid" border-bottom-width="thin" border-right="solid gray thin" text-align="left"/>
																						<children>
																							<template subtype="element" match="context">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="ttext" value="translate(tupleType,&quot;-&quot;,&quot; &quot;)" valuetype="&lt;auto&gt;"/>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="sarcasms">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Sarcasms"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Sarcasms">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Sarcasms"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-bottom="solid gray thin" border-collapse="collapse" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="384"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol/>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="SarcasmId">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<styles text-align="left"/>
																				<children>
																					<text fixtext="Sarcasm value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Begin">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="End">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Judgment">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Confidence">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.Sarcasm">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="sarcasmId">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" datatype="long"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles text-align="left"/>
																						<children>
																							<template subtype="element" match="value">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="begin">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="end">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="judgment">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="confidence">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" string="########.##" datatype="float"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="opinions">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Opinions"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Opinions">
														<styles font-family="Arial" font-size="14pt" font-weight="bold"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Opinions"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-bottom="solid gray thin" border-collapse="collapse" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="384"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol/>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="OpinionId">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<styles text-align="left"/>
																				<children>
																					<text fixtext="Opinion value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Begin">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="End">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Subjectivity">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Polarity">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.Opinion">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="opinionId">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" datatype="long"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles text-align="left"/>
																						<children>
																							<template subtype="element" match="value">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="begin">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="end">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="subjectivity">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="polarity">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="committedBeliefs">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Committed Beliefs"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Committed Beliefs">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Committed_Beliefs"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-bottom="solid gray thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol/>
																	<tgridcol>
																		<properties width="384"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="SequenceId">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<styles text-align="left"/>
																				<children>
																					<text fixtext="Committed Belief Value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Begin">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="End">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Modality">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.CommittedBelief">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="sequenceId">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" datatype="long"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles text-align="left"/>
																						<children>
																							<template subtype="element" match="value">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="begin">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="end">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="modality">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="syntacticChunks">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Syntactic Chunks"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Syntactic Chunks">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Syntactic_Chunks"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-bottom="solid gray thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol/>
																	<tgridcol>
																		<properties width="384"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="SequenceId">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<styles text-align="left"/>
																				<children>
																					<text fixtext="Syntactic Chunk Value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Begin">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="End">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Type">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.SyntacticChunk">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="sequenceId">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" datatype="long"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles text-align="left"/>
																						<children>
																							<template subtype="element" match="value">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="begin">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="tokenOffset">
																								<children>
																									<template subtype="element" match="end">
																										<children>
																											<content subtype="regular">
																												<format basic-type="xsd" datatype="long"/>
																											</content>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="scType">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="paraphrases">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Paraphrases"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Paraphrases">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Paraphrases"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-bottom="solid gray thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties width="384"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol/>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<styles text-align="left"/>
																				<children>
																					<text fixtext="Paraphrase Value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="POS Tag">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Confidence">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.Paraphrase">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<styles text-align="left"/>
																						<children>
																							<template subtype="element" match="value">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="posTag">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="confidence">
																								<children>
																									<content subtype="regular">
																										<format basic-type="xsd" string="########.##" datatype="float"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<template subtype="element" match="triples">
										<children>
											<bookmark>
												<action>
													<none/>
												</action>
												<bookmark>
													<fixtext value="Triples"/>
												</bookmark>
											</bookmark>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Triples">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Triples"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid add-row-expand-collapse-buttons="1">
														<styles border-bottom="solid gray thin" border-left="solid gray thin" border-right="solid gray thin" border-top="solid gray thin" margin-top="10px"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties width="384"/>
																	</tgridcol>
																	<tgridcol>
																		<properties align="center"/>
																	</tgridcol>
																	<tgridcol/>
																</children>
															</tgridbody-cols>
															<tgridheader-rows>
																<properties align="left"/>
																<children>
																	<tgridrow>
																		<properties align="left" bgcolor="#697376"/>
																		<children>
																			<tgridcell>
																				<styles text-align="left"/>
																				<children>
																					<text fixtext="Entity">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Slot">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<text fixtext="Value">
																						<styles color="white"/>
																					</text>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridheader-rows>
															<tgridbody-rows>
																<children>
																	<template subtype="element" match="adept.common.Triple">
																		<children>
																			<tgridrow expand-collapse-level="1">
																				<properties _xbgcolor="$color"/>
																				<children>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="entity">
																								<children>
																									<template subtype="element" match="name">
																										<children>
																											<content subtype="regular"/>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<children>
																							<template subtype="element" match="slot">
																								<children>
																									<template subtype="element" match="name">
																										<children>
																											<content subtype="regular"/>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																					<tgridcell>
																						<styles text-align="left"/>
																						<children>
																							<template subtype="element" match="tvalue">
																								<children>
																									<template subtype="element" match="name">
																										<children>
																											<content subtype="regular"/>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																					</tgridcell>
																				</children>
																			</tgridrow>
																		</children>
																		<variables>
																			<variable name="color" value="concat(substring(&quot;white&quot;, 1 div ( position() mod 2 = 0 )), substring(&quot;lightgray&quot;, 1 div not(( position() mod 2 = 0 ))))" valuetype="&lt;auto&gt;"/>
																		</variables>
																	</template>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<text fixtext=" "/>
									<bookmark>
										<action>
											<none/>
										</action>
										<bookmark>
											<fixtext value="Tokens"/>
										</bookmark>
									</bookmark>
									<template subtype="userdefined" match="/descendant::tokenStream[1]">
										<children>
											<newline/>
											<paragraph paragraphtag="center">
												<styles background="#323d40" color="white"/>
												<children>
													<text fixtext="Tokens">
														<styles font-family="arial" font-size="14pt" font-weight="bolder"/>
													</text>
													<text fixtext="   "/>
													<link>
														<children>
															<image>
																<properties align="bottom"/>
																<target>
																	<fixtext value="C:\Users\jwatson\Documents\information_icon_small.svg.png"/>
																</target>
															</image>
														</children>
														<action>
															<none/>
														</action>
														<hyperlink>
															<fixtext value="help.htm#Tokens"/>
														</hyperlink>
													</link>
												</children>
											</paragraph>
											<paragraph paragraphtag="center">
												<children>
													<tgrid>
														<properties border="0" width="auto"/>
														<styles border-color="transparent" border-left-color="transparent" border-right-color="transparent" border-top-color="transparent" vertical-align="top"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<styles border-left-color="black" border-left-style="solid" border-left-width="thin" border-right-color="black" border-right-style="solid" border-right-width="thin" border-top-color="black" border-top-style="solid" border-top-width="thin"/>
																	</tgridcol>
																	<tgridcol>
																		<styles border-right-color="black" border-right-style="solid" border-right-width="thin" border-top-color="black" border-top-style="solid" border-top-width="thin"/>
																	</tgridcol>
																	<tgridcol>
																		<styles border-right-color="black" border-right-style="solid" border-right-width="thin" border-top-color="black" border-top-style="solid" border-top-width="thin"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<styles text-align="center" vertical-align="top"/>
																		<children>
																			<tgridcell>
																				<children>
																					<tgrid>
																						<properties border="0" width="auto"/>
																						<styles border-bottom-color="transparent" border-color="transparent" border-left-color="transparent" border-left-style="solid" border-left-width="thin" border-right-color="transparent" border-right-style="solid" border-right-width="thin" border-top-color="transparent" display="inline" margin-top="10px"/>
																						<children>
																							<tgridbody-cols>
																								<children>
																									<tgridcol/>
																									<tgridcol/>
																									<tgridcol/>
																								</children>
																							</tgridbody-cols>
																							<tgridheader-rows>
																								<styles border-top-color="black" border-top-style="solid"/>
																								<children>
																									<tgridrow>
																										<properties bgcolor="#697376"/>
																										<styles text-align="center" vertical-align="top"/>
																										<children>
																											<tgridcell>
																												<children>
																													<text fixtext="SequenceId">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																											<tgridcell>
																												<children>
																													<text fixtext="Value">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																											<tgridcell>
																												<children>
																													<text fixtext="CharOffset">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																										</children>
																									</tgridrow>
																								</children>
																							</tgridheader-rows>
																							<tgridbody-rows>
																								<children>
																									<template subtype="element" match="list">
																										<children>
																											<template subtype="element" match="adept.common.Token">
																												<children>
																													<tgridrow conditional-processing="(position() &lt;=  ceiling(//tokenStream/adept.common.TokenStream/default/document/tokenStreamList/adept.common.TokenStream/list/default/size) div 3)
or
(position() &lt;=  ceiling(//tokenStream//list/default/size) div 3)">
																														<styles text-align="center" vertical-align="top"/>
																														<children>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="sequenceId">
																																		<children>
																																			<content subtype="regular">
																																				<format basic-type="xsd" datatype="long"/>
																																			</content>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																															<tgridcell>
																																<styles text-align="left"/>
																																<children>
																																	<template subtype="element" match="value">
																																		<children>
																																			<content subtype="regular"/>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="charOffset">
																																		<children>
																																			<tgrid>
																																				<properties border="0" width="auto"/>
																																				<styles border-color="transparent" display="inline" margin-top="10px"/>
																																				<children>
																																					<tgridbody-cols>
																																						<children>
																																							<tgridcol/>
																																							<tgridcol/>
																																						</children>
																																					</tgridbody-cols>
																																					<tgridbody-rows>
																																						<children>
																																							<tgridrow>
																																								<styles text-align="center" vertical-align="top"/>
																																								<children>
																																									<tgridcell>
																																										<children>
																																											<template subtype="element" match="begin">
																																												<children>
																																													<content subtype="regular">
																																														<format basic-type="xsd" datatype="long"/>
																																													</content>
																																												</children>
																																												<variables/>
																																											</template>
																																										</children>
																																									</tgridcell>
																																									<tgridcell>
																																										<children>
																																											<template subtype="element" match="end">
																																												<children>
																																													<content subtype="regular">
																																														<format basic-type="xsd" datatype="long"/>
																																													</content>
																																												</children>
																																												<variables/>
																																											</template>
																																										</children>
																																									</tgridcell>
																																								</children>
																																							</tgridrow>
																																						</children>
																																					</tgridbody-rows>
																																				</children>
																																			</tgrid>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																														</children>
																													</tgridrow>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																							</tgridbody-rows>
																						</children>
																					</tgrid>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<tgrid>
																						<properties border="0" width="auto"/>
																						<styles border-left-color="transparent" border-left-style="solid" border-left-width="thin" border-right-color="transparent" border-right-style="solid" border-right-width="thin" display="inline" margin-top="10px"/>
																						<children>
																							<tgridbody-cols>
																								<children>
																									<tgridcol/>
																									<tgridcol/>
																									<tgridcol/>
																								</children>
																							</tgridbody-cols>
																							<tgridheader-rows>
																								<styles border-top-color="black"/>
																								<children>
																									<tgridrow>
																										<properties bgcolor="#697376"/>
																										<styles text-align="center" vertical-align="top"/>
																										<children>
																											<tgridcell>
																												<children>
																													<text fixtext="SequenceId">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																											<tgridcell>
																												<children>
																													<text fixtext="Value">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																											<tgridcell>
																												<children>
																													<text fixtext="CharOffset">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																										</children>
																									</tgridrow>
																								</children>
																							</tgridheader-rows>
																							<tgridbody-rows>
																								<children>
																									<template subtype="element" match="list">
																										<children>
																											<template subtype="element" match="adept.common.Token">
																												<children>
																													<tgridrow conditional-processing="(	
	position() &gt; ceiling ((//tokenStream/adept.common.TokenStream/default/document/tokenStreamList/adept.common.TokenStream/list/default/size) div 3)
	and
	position() &lt; ceiling ((//tokenStream/adept.common.TokenStream/default/document/tokenStreamList/adept.common.TokenStream/list/default/size) div 3) * 2
)
or
(
		position() &gt;  ceiling((//tokenStream//list/default/size)  div 3)
		and
		position() &lt;  ceiling((//tokenStream//list/default/size)  div 3) * 2
)">
																														<styles text-align="center" vertical-align="top"/>
																														<children>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="sequenceId">
																																		<children>
																																			<content subtype="regular">
																																				<format basic-type="xsd" datatype="long"/>
																																			</content>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																															<tgridcell>
																																<styles text-align="left"/>
																																<children>
																																	<template subtype="element" match="value">
																																		<children>
																																			<content subtype="regular"/>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="charOffset">
																																		<children>
																																			<tgrid>
																																				<properties border="0" width="auto"/>
																																				<styles display="inline" margin-top="10px"/>
																																				<children>
																																					<tgridbody-cols>
																																						<children>
																																							<tgridcol/>
																																							<tgridcol/>
																																						</children>
																																					</tgridbody-cols>
																																					<tgridbody-rows>
																																						<children>
																																							<tgridrow>
																																								<styles text-align="center" vertical-align="top"/>
																																								<children>
																																									<tgridcell>
																																										<children>
																																											<template subtype="element" match="begin">
																																												<children>
																																													<content subtype="regular">
																																														<format basic-type="xsd" datatype="long"/>
																																													</content>
																																												</children>
																																												<variables/>
																																											</template>
																																										</children>
																																									</tgridcell>
																																									<tgridcell>
																																										<children>
																																											<template subtype="element" match="end">
																																												<children>
																																													<content subtype="regular">
																																														<format basic-type="xsd" datatype="long"/>
																																													</content>
																																												</children>
																																												<variables/>
																																											</template>
																																										</children>
																																									</tgridcell>
																																								</children>
																																							</tgridrow>
																																						</children>
																																					</tgridbody-rows>
																																				</children>
																																			</tgrid>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																														</children>
																													</tgridrow>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																							</tgridbody-rows>
																						</children>
																					</tgrid>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<tgrid>
																						<properties border="0" width="auto"/>
																						<styles border-left-color="transparent" border-left-style="solid" border-left-width="thin" border-right-color="transparent" border-right-style="solid" border-right-width="thin" display="inline" margin-top="10px"/>
																						<children>
																							<tgridbody-cols>
																								<children>
																									<tgridcol/>
																									<tgridcol/>
																									<tgridcol/>
																								</children>
																							</tgridbody-cols>
																							<tgridheader-rows>
																								<styles border-top-color="black"/>
																								<children>
																									<tgridrow>
																										<properties bgcolor="#697376"/>
																										<styles text-align="center" vertical-align="top"/>
																										<children>
																											<tgridcell>
																												<children>
																													<text fixtext="SequenceId">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																											<tgridcell>
																												<children>
																													<text fixtext="Value">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																											<tgridcell>
																												<children>
																													<text fixtext="CharOffset">
																														<styles color="white"/>
																													</text>
																												</children>
																											</tgridcell>
																										</children>
																									</tgridrow>
																								</children>
																							</tgridheader-rows>
																							<tgridbody-rows>
																								<children>
																									<template subtype="element" match="list">
																										<children>
																											<template subtype="element" match="adept.common.Token">
																												<children>
																													<tgridrow conditional-processing="(
	position() &gt;= ceiling ((//tokenStream/adept.common.TokenStream/default/document/tokenStreamList/adept.common.TokenStream/list/default/size) div 3) * 2
)
or
(
	position() &gt;= ceiling ((//tokenStream//list/default/size) div 3) * 2
)">
																														<styles text-align="center" vertical-align="top"/>
																														<children>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="sequenceId">
																																		<children>
																																			<content subtype="regular">
																																				<format basic-type="xsd" datatype="long"/>
																																			</content>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																															<tgridcell>
																																<styles text-align="left"/>
																																<children>
																																	<template subtype="element" match="value">
																																		<children>
																																			<content subtype="regular"/>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="charOffset">
																																		<children>
																																			<tgrid>
																																				<properties border="0" width="auto"/>
																																				<styles display="inline" margin-top="10px"/>
																																				<children>
																																					<tgridbody-cols>
																																						<children>
																																							<tgridcol/>
																																							<tgridcol/>
																																						</children>
																																					</tgridbody-cols>
																																					<tgridbody-rows>
																																						<children>
																																							<tgridrow>
																																								<styles text-align="center" vertical-align="top"/>
																																								<children>
																																									<tgridcell>
																																										<children>
																																											<template subtype="element" match="begin">
																																												<children>
																																													<content subtype="regular">
																																														<format basic-type="xsd" datatype="long"/>
																																													</content>
																																												</children>
																																												<variables/>
																																											</template>
																																										</children>
																																									</tgridcell>
																																									<tgridcell>
																																										<children>
																																											<template subtype="element" match="end">
																																												<children>
																																													<content subtype="regular">
																																														<format basic-type="xsd" datatype="long"/>
																																													</content>
																																												</children>
																																												<variables/>
																																											</template>
																																										</children>
																																									</tgridcell>
																																								</children>
																																							</tgridrow>
																																						</children>
																																					</tgridbody-rows>
																																				</children>
																																			</tgrid>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																														</children>
																													</tgridrow>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																							</tgridbody-rows>
																						</children>
																					</tgrid>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
												</children>
											</paragraph>
											<link>
												<children>
													<paragraph paragraphtag="center">
														<children>
															<text fixtext="Go to Top of Page"/>
														</children>
													</paragraph>
												</children>
												<action>
													<none/>
												</action>
												<hyperlink>
													<fixtext value="#Table of Contents"/>
												</hyperlink>
											</link>
										</children>
										<variables/>
									</template>
									<newline/>
								</children>
								<variables/>
							</template>
						</children>
						<variables/>
					</template>
				</children>
			</globaltemplate>
		</children>
	</mainparts>
	<globalparts/>
	<designfragments/>
	<xmltables/>
	<authentic-custom-toolbar-buttons/>
</structure>
###
# #%L
# adept-kb
# %%
# Copyright (C) 2012 - 2017 Raytheon BBN Technologies
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###
import sys
import os
import csv
import re



indir = sys.argv[1]
summaryf = sys.argv[2]
outf = open(summaryf, "w")
foundre = re.compile(r"Found (\d+) (\S.+)")
writtenre = re.compile(r"Wrote\s+(\d+)\s+(\S.+)")
selectedre = re.compile(r"Randomly selected\s+(\d+)\s+(\S.+)")
injectedre = re.compile(r"Injected\s+(\d+)\s+(\S.+)")
null_backup = re.compile(r"Provenance string for (\S+) \.+ was null")
null_provenancere = re.compile(r"Provenance string for (\S+[a-z]) [a-z\d\-]+ was null")
exceptionre = re.compile(r"Exception")
exceptionpartialre = re.compile(r"(\S.+) =")

def rowToStandardUpdate(cells, confCell, mentCountCell, docCountCell, minConf, maxConf, maxMentCount, maxDocCount):
	minConf = min(float(cells[confCell]), minConf)
	maxConf = max(float(cells[confCell]), maxConf)
	maxMentCount = max(int(cells[mentCountCell]), maxMentCount)
	docCountCell = max(int(cells[docCountCell]), maxDocCount)	
	return (minConf, maxConf, maxMentCount, docCountCell)

def writeStandardLines(outf, minConf, maxConf, maxMentCount, maxDocCount):
	outf.write("Confidence Range: "+str(minConf)+" - "+str(maxConf)+"\n")
	outf.write("Max Mention Count: "+str(maxMentCount)+"\n")
	outf.write("Max Distinct Docs: "+str(maxDocCount)+"\n")
def setStandardDefaults():
	nlines = 0
	maxmentions = 0
	maxdocs = 0
	minconfidence = 1000000 
	maxconfidence = -1
	return (nlines, minconfidence, maxconfidence, maxmentions, maxdocs)
def printLogDictionary(s, d, outf):
	if d.keys():
		outf.write("- " +s+"\n")
	for k in sorted(d.keys()):
		outf.write("\t"+k+" \t"+str(d[k])+"\n")


teams = os.listdir(indir)
for t in sorted(teams):
	outf.write("========== "+t+" ============\n")
	#### collect information from log
	logf = os.path.join(indir, "logs", t+".txt")
	if not os.path.exists(logf):
		outf.write("No log file in: " +logf+"\n")
		continue
	inf = open(logf)
	nlines = 0
	found = {}
	written = {}
	injected = {}
	selected = {}
	reAndMap = [(foundre, found), (writtenre, written), (injectedre, injected), (selectedre, selected)]
	exceptions = {}
	nullprovenance = {}
	
	for line in inf:
		nlines+=1
		matches = foundre.search(line)
		for (r, m) in reAndMap:
			matches = r.search(line)
			if matches:
				m[matches.group(2)] = matches.group(1)
		matches = null_provenancere.search(line)
		if matches:
			nullprovenance.setdefault(matches.group(1), 0)
			nullprovenance[matches.group(1)]+=1
		matches = exceptionre.search(line)
		if matches:
			partial = exceptionpartialre.search(line)
			if partial:
				exceptions.setdefault(partial.group(1), 0)
				exceptions[partial.group(1)]+=1	
			else:
				exceptions.setdefault(line.strip(), 0)
				exceptions[line.strip()]+=1	
	outf.write("==== "+t+" log ===\n")
	outf.write("# Lines in log: "+str(nlines)+"\n")
	printLogDictionary("Found(per log)", found, outf)
	printLogDictionary("Selected(per log)", selected, outf)
	printLogDictionary("Injected(per log)", injected, outf)
	printLogDictionary("Written(per log)", written, outf)
	printLogDictionary("Logged Exceptions", exceptions, outf)
	printLogDictionary("Logged Null Provenance", nullprovenance, outf)
	outf.write("\n")
	inf.close()	
	#### Collect information from entity.csv
	entityf = os.path.join(indir, t, t+"."+"entities.csv")
	if not os.path.exists(entityf):
		continue
	with open(entityf, 'rb') as csvfile:
		ereader = csv.reader(csvfile)  
		(nlines, minconfidence, maxconfidence, maxmentions, maxdocs)= setStandardDefaults()
		nlines = 0
		types = set()
		maxstrings = 0
		for row in ereader:
			nlines+=1
			if nlines == 1:
				continue
			if len(row) > 6:
				types.add(row[0])
				(minconfidence, maxconfidence, maxmentions, maxdocs) = rowToStandardUpdate(row, 3, 4, 5, minconfidence, maxconfidence, maxmentions, maxdocs)
				maxstrings = max(int(row[6]), maxstrings)
		csvfile.close()
		outf.write("==== " + t +".entity.csv summary" +" ====\n")
		if nlines > 1:
			outf.write("Number of Entities: "+str(nlines-1)+"\n")
			writeStandardLines(outf, minconfidence, maxconfidence, maxmentions, maxdocs)	
			outf.write("Max Unique Strings: "+str(maxstrings)+"\n")
			outf.write("Entity Types: ")
			for e in types:
				outf.write("["+e +"], ")
			outf.write("\n")
		else:
			outf.write("-- Empty --\n")		
		outf.write("\n")
	
	multiargpath = ["relations", "events"]
	for p in multiargpath:
		f = os.path.join(indir, t, t+"."+p+".csv")
		if not os.path.exists(f):
			continue	
		with open(f, 'rb') as csvfile:
			rreader = csv.reader(csvfile)  		
			(nlines, minconfidence, maxconfidence, maxmentions, maxdocs)= setStandardDefaults()
			typesAndRoles = dict()
			minargs = 1000000 
			maxargs = -1		
			for row in rreader:
				nlines+=1
				if nlines == 1:
					continue			
				if len(row) > 16:
					typesAndRoles.setdefault(row[0], set())
					(minconfidence, maxconfidence, maxmentions, maxdocs) = rowToStandardUpdate(row, 2, 3, 4, minconfidence, maxconfidence, maxmentions, maxdocs)
					maxargs = max(int(row[15]), maxargs)
					minargs = min(int(row[15]), minargs)
					#hack only look at first argument role and assume this will cover the ontology using randomness
					#typesAndRoles[row[0]].add(row[16])
					for i in range(0,5):
						if 16+(i*6) < len(row):
							r = row[16+(i*6)]
							typesAndRoles[row[0]].add(r)

					
			csvfile.close()
			outf.write("==== " + t+"."+p+".csv "+" summary" +" ====\n")
			if nlines > 1:
				outf.write("Number of "+p+": "+str(nlines-1)+"\n")
				writeStandardLines(outf, minconfidence, maxconfidence, maxmentions, maxdocs)	
				outf.write("Number of Arguments Range: "+str(minargs)+" - "+str(maxargs) +"\n")
				outf.write(p+" types & roles: ")
				for e in typesAndRoles.keys():
					outf.write(e +": {")
					for r in typesAndRoles[e]:
						outf.write(r+", ")
					outf.write("}; ")
				outf.write("\n")
			else:
				outf.write("-- Empty --\n")	
			outf.write("\n")
	
	sentbeliefpath = ["sentiments", "beliefs"]
	for p in sentbeliefpath:
		f = os.path.join(indir, t, t+"."+p+".csv")
		if not os.path.exists(f):
			continue	
		with open(f, 'rb') as csvfile:
			rreader = csv.reader(csvfile)  		
			(nlines, minconfidence, maxconfidence, maxmentions, maxdocs)= setStandardDefaults()
			minargs = 1000000 
			maxargs = -1	
			typesAndRoles = dict()			
			for row in rreader:
				nlines+=1
				if nlines == 1:
					continue			
				if len(row) > 16:	
					typesAndRoles.setdefault(row[0], set())
					(minconfidence, maxconfidence, maxmentions, maxdocs) = rowToStandardUpdate(row, 2, 3, 4, minconfidence, maxconfidence, maxmentions, maxdocs)
					maxargs = max(int(row[15]), maxargs)
					minargs = min(int(row[15]), minargs)					
					for i in range(0,5):
						if 19+(i*6) < len(row):
							r = row[16+(i*6)]
							c = row[19+(i*6)]
							words = c.split(" ")
							typesAndRoles[row[0]].add(r+"("+words[0]+")")
					
			csvfile.close()
			outf.write("==== " + t+"."+p+".csv "+" summary" +" ====\n")
			if nlines > 1:
				outf.write("Number of "+p+": "+str(nlines-1)+"\n")
				writeStandardLines(outf, minconfidence, maxconfidence, maxmentions, maxdocs)	
				outf.write("Number of Arguments Range: "+str(minargs)+" - "+str(maxargs) +"\n")
				for e in typesAndRoles.keys():
					outf.write(e+": {")
					for r in typesAndRoles[e]:
						outf.write(r+"," )
					outf.write("}; ")
				outf.write("\n")
			else:
				outf.write("-- Empty --\n")	
			outf.write("\n")				

outf.close()



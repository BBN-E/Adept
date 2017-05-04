## GenerateHumanReadableReport ##

This utility is used to check your knowledge base for issues and sample the contents for further analysis. The code is defined in `adept/adept-kb/src/main/java/adept/utilities/GenerateHumanReadableReport.java` and is built as part of running `mvn install -pl :adept-kb -am`.

After building, it can be run with `adept/adept-kb/target/appassembler/bin/GenerateHumanReadableReport`:

```
GenerateHumanReadableReport
  </path/to/output/dir>
  <teamname>
  -p KBParameters.xml
  -s <sample size limit>
  > </path/to/output/dir>/logs/<teamname>.txt
```

An template parameter file is available as `adept/adept-kb/src/main/resources/adept/kbapi/KBParameters.template.xml`. You will need to follow the comments in that file to craft your own `KBParameters.xml` with appropriate URLs and passwords for your team's knowledge base.

The sample size is optional, but can help return results faster for large knowledge bases by limiting the number of objects that are read. 5000 is a good value for most evaluations.

If for some reason you need to rerun the report and don't want duplicate objects in your output, you can specify `-r` to resume.

After running, the output directory will contain an Excel-compatible CSV file for each object type (entity, relation, event, belief, and sentiment). Each row in these files contains one object, with some counts and a few text justifications. The stdout log will contain warnings about potential issues when reading from the knowledge base.

You can generate a summary of the output by using the script `adept/adept-kb/src/main/python/fast_summary.py`:

```
python
  fast_summary.py
  <path/to/output/dir>
  <path/to/output/dir>/summary.txt
```

This script will collect summary statistics by type for each kind of object, and accumulate counts for any logged errors.

If you have any questions about using this tool, please contact [DEFT Support](mailto:deft-support@bbn.com). If you encounter a bug with KB operations, please open a [JIRA ticket](https://www.deftbugs.bbn.com:8443/).

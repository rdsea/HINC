#Basic Interoperability Metadata Example - Data Format
A basic example that only focuses on data formats.
Can be used to merge with a full interoperability metadata example.
 
##Metadata Description

####CSV-Input

| key | assigned value | description|
|:--- |:---------------| :--------: |
|dataformat|a csv dataformat object| type identified by dataformat_name|
|**dataformat** |||
|.dataformat_name|  "csv" |  the name of the dataformat |
|.encoding|  "utf-8" | the character encoding of the textual data |
|.headers| "key1, key2, key3" | a list of headers that correspond to the csv data |
|.seperator| "," | the seperator for data columns|
|.newline_seperator| "\n" | the seperator for data rows|
|.headers_included| false | if the header is included in data messages or not|



####JSON-Output

| key | assigned value | description|
|:--- |:---------------| :--------: |
|dataformat|a json dataformat object| type identified by dataformat_name|
|**dataformat** |||
|.dataformat_name| "json" | the name of the dataformat|
|.encoding|"utf-8"| the character encoding of the textual data |
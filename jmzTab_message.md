


---

# Introduction #

These error messages are classified in two categories: 'Format Type' (reporting format problems) and 'Logical Type' (reporting errors related to the logical relationships among the different sections in a file).

The validation messages have a unique identifier are classified in three levels: Info, Warn and Error. All of them come from the notational conventions included in the specification document. This way, all the rules in the specification which use "MUST", "MUST NOT", "REQUIRED" as keywords, are defined at the Error level; the ones that use "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT" as keywords are defined at the Warn level. Finally, others keywords such as "RECOMMENDED", "MAY" and "OPTIONAL", are defined at the Info Level.

## Message List ##

| **Code**: | 1000 |
|:----------|:-----|
| **Level**: | Error |
| **Original**: | "{0}" MUST start with [MTD, PRH, PRT, PEH, PEP, PSH, PSM, SMH, SML, COM] |
| **Cause**: | Every line in an mzTab file MUST start with a three letter code identifying the type of line delimited by a Tab character. The three letter codes are as follows [MTD, PRH, PRT, PEH, PEP, PSH, PSM, SMH, SML, COM] |
|  |  |
|  |  |
| **Code**: | 1001 |
| **Level**: | Error |
| **Original**: | The number of Data line items is "{0}", whereas the number of header line items count is "{1}". They MUST be the same. |
| **Cause**: | The number of Data line items does not match with the number of Header line items. Normally, the user has not used the Unicode Horizontal Tab character (Unicode codepoint 0009) as the column delimiter, there is a file encoding error, or the user has not provided the definition of optional columns in the header line. |
|  |  |
|  |  |
| **Code**: | 1002 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid element name, which is given by indicating the number in square brackets. |
| **Cause**: | Indexed element structure like: element\_name[id](id.md)-element\_property. "[id](id.md)" and "-element\_property" are optional. |
|  |  |
|  |  |
| **Code**: | 1003 |
| **Level**: | Error |
| **Original**: | Optional Column "{0}" is not a valid abundance column format. |
| **Cause**: | Only protein, peptide and small molecule sections can allow to have abundance columns. The column name psm\_abundance\_XXX is not correct.. |
|  |  |
|  |  |
| **Code**: | 1004 |
| **Level**: | Error |
| **Original**: | Mandatory column "{0}" are required on the header line. |
| **Cause**: | Check the mandatory columns (e.g. header name). All column labels and field names are case-sensitive. The column delimiter is the Unicode Horizontal Tab character. |
|  |  |
|  |  |
| **Code**: | 1005 |
| **Level**: | Error |
| **Original**: | Optional column headers "{0}" are not allowed to be displayed in "{1}" line. |
| **Cause**: | Only following ms\_run optional columns allowed to be displayed in the header line: Protein header optional columns num\_psms\_ms\_run[1-n], num\_peptides\_distinct\_ms\_run[1-n], num\_peptides\_unique\_ms\_run[1-n], Peptide header optional column search\_engine\_score\_ms\_run[1-n] and Small molecule optional column search\_engine\_score\_ms\_run[1-n]. |
|  |  |
|  |  |
| **Code**: | 1007 |
| **Level**: | Error |
| **Original**: | Optional CV parameter column headers "{0}" format is not correct. |
| **Cause**: | Optional column headers MUST start with the prefix "opt_". Column names MUST only contain the following characters: 'A'-'Z', 'a'-'z', '0'-'9', '_', '-', '[', ']', and ':'. CV parameter accessions MAY be used for optional columns following the format: opt\_cv_{accession}_{parameter name}. {parameter\_name} can not set to 'null'. Spaces within the parameter's name MUST be replaced by '_'.  This is an optional column, and MAY be added at the right of the table._|
|  |  |
|  |  |
| **Code**: | 1008 |
| **Level**: | Error |
| **Original**: | "{0}" should have three parts, which are split by a Tab char. |
| **Cause**: | MTD line including three parts, which are split by a Tab char. "MTD	{defineLabel}	{valueLabel}". Each of them should not be empty. |
|  |  |
|  |  |
| **Code**: | 1009 |
| **Level**: | Error |
| **Original**: | Metadata define label "{0}" can not be recognized. |
| **Cause**: | MTD defineLabel should include "{element}([{id}])-{property}", which is split by "-" char. |
|  |  |
|  |  |
| **Code**: | 1010 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" should be 'Complete' or 'Summary'. |
| **Cause**: | The results included in an mzTab file can be reported in two ways: 'Complete' (when results for each assay/replicate are included) and 'Summary', when only the most representative results are reported. |
|  |  |
|  |  |
| **Code**: | 1011 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" should be 'Identification' or 'Quantification'. |
| **Cause**: | The results included in an mzTab file MUST be flagged as 'Identification' or 'Quantification'  - the latter encompassing approaches that are quantification only or quantification and identification. |
|  |  |
|  |  |
| **Code**: | 1012 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid parameter. |
| **Cause**: | Parameters are always reported as [label, accession, name, value](CV.md). Any field that is not available MUST be left empty, except 'name'. mzTab makes use of CV parameters. As mzTab is expected to be used in several experimental environments where parameters might not yet be available for the generated scores etc., all parameters can either be reported as CV parameters, or by user parameters that only contain a name and a value. In the case when the name of the param contains commas, quotes MUST be added to avoid problems with the parsing: [label, accession, "first part of the param name , second part of the name", value]. |
|  |  |
|  |  |
| **Code**: | 1013 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid list of parameters. |
| **Cause**: | If multiple parameters are given for a given step, these should be separated by a "|". Parameters are always reported as [label, accession, name, value](CV.md). |
|  |  |
|  |  |
| **Code**: | 1014 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid publication. |
| **Cause**: | PubMed ids must be prefixed by "pubmed:", DOIs by "doi:". Multiple identifiers MUST be separated by "|". For example: "MTD  publication[1](1.md)  pubmed:21063943|doi:10.1007/978-1-60761-987-1\_6". |
|  |  |
|  |  |
| **Code**: | 1015 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid URI. |
| **Cause**: | A URI pointing to the unit's source data, (e.g. a PRIDE experiment or a PeptideAtlas build). For example, "MTD  PRIDE\_1234-uri  http://www.ebi.ac.uk/pride/url/to/experiment". |
|  |  |
|  |  |
| **Code**: | 1016 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid URL. |
| **Cause**: | Location of the external data file. For example: "[file://C:path](file://C:path)	omyile" or "[ftp://ftp.ebi.ac.uk/path/to/file](ftp://ftp.ebi.ac.uk/path/to/file)" |
|  |  |
|  |  |
| **Code**: | 1017 |
| **Level**: | Error |
| **Original**: | "{0}" value "{1}" is not a valid e-mail address. |
| **Cause**: | A e-mail like userName@serverName |
|  |  |
|  |  |
| **Code**: | 1018 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid Integer value. |
| **Cause**: | This is not an Integer. Thousand separators MUST NOT be used in mzTab files. If ratios are included and the denominator is zero, the "INF" value MUST be used. If the result leads to calculation errors (for example 0/0), this MUST be reported as "not a number" ("NaN"). |
|  |  |
|  |  |
| **Code**: | 1019 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid Double value. |
| **Cause**: | In mzTab files the dot (".") MUST be used as decimal separator. Thousand separators MUST NOT be used in mzTab files. If ratios are included and the denominator is zero, the "INF" value MUST be used. If the result leads to calculation errors (for example 0/0), this MUST be reported as "not a number" ("NaN"). |
|  |  |
|  |  |
| **Code**: | 1020 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid Reliability value. The value should be 1..3 |
| **Cause**: | The reliability of the given protein identification. This must be supplied by the resource and has to be one of the following values:  1: high reliability; 2: medium reliability; 3: poor reliability. |
|  |  |
|  |  |
| **Code**: | 1021 |
| **Level**: | Error |
| **Original**: | =Column "{0}" value "{1}" is not valid string list. The split char is "{1}" |
| **Cause**: | =If multiple String, they must be split using a char. |
|  |  |
|  |  |
| **Code**: | 1022 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid list of Doubles. The split char is "{1}" |
| **Cause**: | If multiple Double using a split char. |
|  |  |
|  |  |
| **Code**: | 1023 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid modification list. |
| **Cause**: | A comma delimited list of modifications found in the given protein/peptide/small molecule. Modifications or substitutions are modelled using a specific modification object with the following format: {position}{Parameter}-{Modification or Substitution identifier}|{neutral loss}. {position} is optional depending on the section where the modification is reported. {Parameter} is optional. It MAY be used to report a quantity e.g. a probability score associated with the modification or location. {neutral loss} is optional. Neutral losses are reported as cvParams. Neutral losses MAY be associated with certain modifications. In this case the neutral loss is reported after the modification object separated by the '|' character. Otherwise, they are reported in the same way that modification objects are (as separate, comma-separated objects in the modification column). |
|  |  |
|  |  |
| **Code**: | 1024 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid list of GO (Gene Ontology) terms. |
| **Cause**: | A '|'-delimited list of GO accessions for this protein. Each GO accession starts with "GO:". |
|  |  |
|  |  |
| **Code**: | 1025 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid Boolean (0/1). |
| **Cause**: | Boolean (0/1). To report the results of a target-decoy search, decoy identifications MAY be labelled using the optional column "opt\_global\_cv\_MS:1002217\_decoy\_peptide". The value of this column MUST be a Boolean (1/0). |
|  |  |
|  |  |
| **Code**: | 1026 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid spectra reference, or the ms\_run[1-n]-location is not defined in the metadata section. |
| **Cause**: | The reference must be specified in the format ms\_run[1-n]:{SPECTRA\_REF}. Multiple spectra MUST be referenced using a '|' delimited list. |
|  |  |
|  |  |
| **Code**: | 1027 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid CHEMMOD accession format. |
| **Cause**: | CHEMMODS have the format CHEMMOD:+/-{chemical formula or m/z delta}. Valid CHEMMODS are for example "CHEMMOD:+NH4" or "CHEMMOD:-10.1098". Mass deltas MUST NOT be used for CHEMMODs if the delta can be expressed through a known chemical formula. |
|  |  |
|  |  |
| **Code**: | 1028 |
| **Level**: | Warn |
| **Original**: | Column "{0}" value "{1}" do not use CV parameters. |
| **Cause**: | A "|" delimited list of search engine score(s), Scores SHOULD be reported using CV parameters whenever possible. |
|  |  |
|  |  |
| **Code**: | 1029 |
| **Level**: | Error |
| **Original**: | Metadata value label "{0}" column name "{1}" cannot be found in the list of stable column names. |
| **Cause**: | The format of the column label should be {column name}={Parameter defining the unit}. {Parameter defining the unit}, where Parameter has the format of a Param. |
|  |  |
|  |  |
| **Code**: | 2000 |
| **Level**: | Error |
| **Original**: | Column "{0}" is an empty cell, or the current column cannot use "null". |
| **Cause**: | In the table-based sections (protein, peptide, psm, and small molecule) there MUST NOT be any empty cells. Missing values MUST be reported using "null". |
|  |  |
|  |  |
| **Code**: | 2001 |
| **Level**: | Warn |
| **Original**: | Column "{0}" field value does not allow a "null" value. |
| **Cause**: | Some kind of field value can not allow a "null" or empty value. In general, "null" values SHOULD not be given within any column of a "Complete" file. |
|  |  |
|  |  |
| **Code**: | 2002 |
| **Level**: | Error |
| **Original**: | current line belongs to "{0}" section, "{1}" line can not be displayed here. |
| **Cause**: | mzTab line including five main sections: metadata, protein, peptide, psm, small molecule. Every section in an mzTab file MUST only occur once (if present). The line order MUST be the following one: metadata lines should be displayed first, then the protein header, protein, peptide header, peptide, psm header, psm, small molecule header, and small molecule. |
|  |  |
|  |  |
| **Code**: | 2003 |
| **Level**: | Error |
| **Original**: | header line "{0}" can only occur once! |
| **Cause**: | Each table based section (protein, peptide, small molecule) MUST start with the corresponding header line. These header lines MUST only occur once in the document since each section also MUST only occur once. |
|  |  |
|  |  |
| **Code**: | 2004 |
| **Level**: | Error |
| **Original**: | current line "{0}" is a data line, the corresponding header line has not been defined yet. |
| **Cause**: | Each table based section (protein, peptide, small molecule) MUST start with the corresponding header line. These header lines MUST only occur once in the document since each section also MUST only occur once. |
|  |  |
|  |  |
| **Code**: | 2005 |
| **Level**: | Error |
| **Original**: | Optional column header "{0}" ms\_run[1-n] should be defined in the metadata section first. |
| **Cause**: | Protein header optional columns num\_psms\_ms\_run[1-n], num\_peptides\_distinct\_ms\_run[1-n], num\_peptides\_unique\_ms\_run[1-n], Peptide header optional column search\_engine\_score\_ms\_run[1-n] and Small molecule optional column search\_engine\_score\_ms\_run[1-n]. In all of them, ms\_run[1-n] should be defined in the metadata section first. |
|  |  |
|  |  |
| **Code**: | 2006 |
| **Level**: | Error |
| **Original**: | Abundance optional column header "{0}" assay[1-n] should be defined in the metadata section first. |
| **Cause**: | Protein abundance optional columns protein\_abundance\_assay[1-n], Peptide abundance optional column peptide\_abundance\_assay[1-n] and Small molecule abundance optional column smallmolecule\_abundance\_assay[1-n]. In all of them, assay[1-n] should be defined in the metadata section first. |
|  |  |
|  |  |
| **Code**: | 2007 |
| **Level**: | Error |
| **Original**: | Abundance optional column header "{0}" study\_variable[1-n] should be defined in the metadata section first. |
| **Cause**: | Protein abundance optional columns protein\_abundance\_study\_variable[1-n], protein\_abundance\_stdev\_study\_variable[1-n], protein\_abundance\_std\_error\_study\_variable[1-n], Peptide abundance optional column peptide\_abundance\_study\_variable[1-n], peptide\_abundance\_stdev\_study\_variable[1-n], peptide\_abundance\_std\_error\_study\_variable[1-n] and Small molecule abundance optional column smallmolecule\_abundance\_study\_variable[1-n], smallmolecule\_abundance\_stdev\_study\_variable[1-n], smallmolecule\_abundance\_std\_error\_study\_variable[1-n]. In all of them, study\_variable[1-n] should be defined in the metadata section first. |
|  |  |
|  |  |
| **Code**: | 2008 |
| **Level**: | Error |
| **Original**: | "{0}" MUST be included in the metadata section when 'mzTab-mode' is "{1}" and 'mzTab-type' is "{2}". |
| **Cause**: | Reference specification 5.5 |
|  |  |
|  |  |
| **Code**: | 2009 |
| **Level**: | Error |
| **Original**: | "{0}" MUST be included in the header line when 'mzTab-mode' is "{1}" and 'mzTab-type' is "{2}". |
| **Cause**: | Reference specification 5.5 |
|  |  |
|  |  |
| **Code**: | 2010 |
| **Level**: | Error |
| **Original**: | "{0}" duplicate defined in the metadata section. |
| **Cause**: | mzTab-ID, title, description, false\_discovery\_rate, fixed\_mode, variable\_mode, quantification\_method, protein-quantification\_unit, peptide-quantification\_unit, small\_molecule-quantification\_unit. All of them only can only be displayed once in the metadata section. |
|  |  |
|  |  |
| **Code**: | 2011 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is a duplicate. |
| **Cause**: | The accession of the protein in the source database. A protein accession MUST be unique within one mzTab file. If different quantification values are required for the same underlying accession, for example if differentially modified forms of a protein have been quantified, a suitable suffix SHOULD be appended to the accession. |
|  |  |
|  |  |
| **Code**: | 2012 |
| **Level**: | Error |
| **Original**: | "{0}" should be defined when assays and study variables are reported in the metadata section. |
| **Cause**: | If both assays and study variables are reported in an mzTab file, the references to assays and study variables are MANDATORY. |
|  |  |
|  |  |
| **Code**: | 2013 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not a valid protein coverage value. It should be between 0 and 1. |
| **Cause**: | A value between 0 and 1 defining the protein coverage |
|  |  |
|  |  |
| **Code**: | 2014 |
| **Level**: | Error |
| **Original**: | "{0}" id number "{1}" should be a number, and the value should be greater than 0. |
| **Cause**: | In mztab, multiple members are numbered 1..n, for example: Multiple sample processing are numbered sample\_processing[1](1.md), sample\_processing[2](2.md) |
|  |  |
|  |  |
| **Code**: | 2015 |
| **Level**: | Error |
| **Original**: | Column "{0}" include a terminal modification "{1}", and its position must be between 0 and sequence "{2}".length + 1. |
| **Cause**: | Terminal modifications MUST be reported at position 0 or protein size + 1 respectively. |
|  |  |
|  |  |
| **Code**: | 2016 |
| **Level**: | Warn |
| **Original**: | Column "{0}" value "{1}" is kind of CHEMMODS. Their use is not recommended. |
| **Cause**: | For proteins and peptides, modifications SHOULD be reported using either UNIMOD or PSI-MOD. As these two ontologies/CVs are not applicable to small molecules, so-called CHEMMODs can also be defined. CHEMMODs MUST NOT be used if the modification can be reported using a PSI-MOD or UNIMOD accession.  Mass deltas MUST NOT be used for CHEMMODs if the delta can be expressed through a known chemical formula. |
|  |  |
|  |  |
| **Code**: | 2017 |
| **Level**: | Error |
| **Original**: | Column "{0}" value "{1}" is not an original, unaltered amino acid sequence. |
| **Cause**: | It is possible to report substitutions of amino acids using SUBST:{amino acid}. In these cases, the "sequence" column MUST contain the original, unaltered sequence. |
|  |  |
|  |  |
| **Code**: | 2018 |
| **Level**: | Warn |
| **Original**: | Software parameter "{0}" does not contain version information. |
| **Cause**: | The parameter's value SHOULD contain the software's version. The order (numbering) should reflect the order in which the tools were used. |
|  |  |
|  |  |
| **Code**: | 2019 |
| **Level**: | Error |
| **Original**: | Missing abundance column "{0}" in the header line. |
| **Cause**: | XXXX\_abundance\_study\_variable[1-n], XXXX\_abundance\_stdev\_study\_variable[1-n], XXXX\_abundance\_std\_error\_study\_variable[1-n] should be displayed together, the order cannot change either. |
|  |  |
|  |  |
| **Code**: | 2020 |
| **Level**: | Error |
| **Original**: | Abundance columns "{0}", "{1}", "{2}" study\_variable id number should be the same. |
| **Cause**: | XXXX\_abundance\_study\_variable[1-n], XXXX\_abundance\_stdev\_study\_variable[1-n], XXXX\_abundance\_std\_error\_study\_variable[1-n] should have the same study\_variable[id](id.md) number. |
|  |  |
|  |  |
| **Code**: | 2021 |
| **Level**: | Warn |
| **Original**: | Column "{0}" value "{1}", "{2}" is "null". The use of "null" is allowed here however its use is discouraged. |
| **Cause**: | If "spectra\_ref" is present, the element "ms\_run[1-n]-location" MUST be reported in the metadata section. Please check table 1 in the specification document. |
|  |  |
|  |  |
| **Code**: | 2022 |
| **Level**: | Warn |
| **Original**: | Column "{0}" value "{1}" represents Ambiguity of modification position at the Protein level. Ambiguity of modification position MUST NOT be reported at the Protein level. |
| **Cause**: | Ambiguity of modification position MUST NOT be reported at the Protein level. |
|  |  |
|  |  |
| **Code**: | 2023 |
| **Level**: | Warn |
| **Original**: | "{0}" should be defined when assays are reported in the metadata section or if a PSM section is present in the file. |
| **Cause**: | If assays are reported or if a PSM section is present in the file, these attributes are MANDATORY, since back references to the MS run MUST be provided in certain sections. If the actual location of the MS run is unknown, a place holder value SHOULD be inserted. |
|  |  |
|  |  |
| **Code**: | 2024 |
| **Level**: | Error |
| **Original**: | Fixed modifications are not defined in the metadata section. |
| **Cause**: | Fixed modifications should be defined in the metadata section, if PSM section is present. |
|  |  |
|  |  |
| **Code**: | 2025 |
| **Level**: | Error |
| **Original**: | Variable modifications are not defined in the metadata section. |
| **Cause**: | Variable modifications should be defined in the metadata section, if PSM section is present. |
|  |  |
|  |  |
| **Code**: | 2026 |
| **Level**: | Warn |
| **Original**: | The existence of a Peptide Section line "{0}" is NOT RECOMMENDED in identification only files. |
| **Cause**: | The existence of a Peptide section is NOT RECOMMENDED to be used in identification only files. |
|  |  |
|  |  |
| **Code**: | 2027 |
| **Level**: | Error |
| **Original**: | If mzTab-type is "Quantification", then at least one section with {protein|peptide|small\_molecule}**abundance_columns MUST be present_|**| **Cause**: | If mzTab-type is "Quantification", then at least one section with {protein|peptide|small\_molecule}**abundance_columns MUST be present_|**|  |  |
|  |  |
| **Code**: | 2028 |
| **Level**: | Error |
| **Original**: | "{0}" contains duplicate identifier. |
| **Cause**: | study\_variable[1-n]-assay\_refs and study\_variable[1-n]-sample\_refs MUST NOT contain duplicated identifier. |
|  |  |
|  |  |
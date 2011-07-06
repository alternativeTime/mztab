package uk.ac.ebi.tools.mztab_java.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ebi.tools.mztab_java.MzTabFile;
import uk.ac.ebi.tools.mztab_java.MzTabParsingException;

public class Unit {
	private Logger logger = Logger.getLogger(Unit.class);
	/**
	 * Pattern to parse meta-data lines. It extracts
	 * the line type, UNIT_ID, SUB_ID (null if not
	 * present), and the field name and value.
	 */
	private static final Pattern MZTAB_LINE_PATTERN = Pattern.compile("^(\\w{3})\t([^-]+)-(sub\\[\\d+\\])?-?([^\t]+)\t(.+)");
	/**
	 * Unit attributes
	 */
	private String					unitId;
	private String 					title;
	private String 					description;
	private ArrayList<ParamList> 	sampleProcessing;
	private ArrayList<Instrument> 	instrument;
	private ArrayList<Param>		software;
	private ParamList				falseDiscoveryRate;
	/**
	 * DOIs must be prefixed by "doi:", PubMed ids by "pubmed:"
	 */
	private ArrayList<String>		publication;
	private ArrayList<Contact>		contact;
	private URI						uri;
	private ParamList				mod;
	private Param					modProbabilityMethod;
	private Param					quantificationMethod;
	private Param					proteinQuantificationUnit;
	private Param					peptideQuantificationUnit;
	private ArrayList<Param>		customParams;
	
	private ArrayList<Param>		species;
	private ArrayList<Param>		tissue;
	private ArrayList<Param>		cellType;
	private ArrayList<Param>		disease;
	
	private ArrayList<Subsample>	subsamples;
	
	/**
	 * Constructur constructing an empty Uni.
	 */
	public Unit() {
		
	}
	
	/**
	 * Parses the given mztab string and sets the various
	 * properties accordingly. Properties that are already
	 * set and not defined in the passed string are not
	 * altered. The only exception to this rule is the unit
	 * id: once the unit id was set unmarshalling properties
	 * assigned to a different unit causes a parsing exception
	 * to be thrown. 
	 * @param mzTabString
	 * @throws MzTabParsingException Thrown on any parsing error.
	 */
	public void unmarshall(String mzTabString) throws MzTabParsingException {
		// parse the string line by line
		String[] lines = mzTabString.split("\r?\n");
		
		for (String line : lines) {
			// ignore empty and non-metadata lines
			if (line.trim().length() < 1 || !"MTD".equals(line.substring(0, 3)))
				continue;
			
			// parse the line
			Matcher matcher = MZTAB_LINE_PATTERN.matcher(line);
			
			// throw a parsing exception if the line couldn't be parsed
			if (!matcher.find())
				throw new MzTabParsingException("Invalid meta-data line encountered: <" + line + ">");
			
			// get the various fields
			String unitId 	= matcher.group(2).trim();
			String subId 	= matcher.group(3);
			String field	= matcher.group(4);
			String value	= matcher.group(5);
			
			if (subId != null)
				subId = subId.trim();
			if (field != null)
				field = field.trim();
			if (value != null)
				value = value.trim();
			
			// check that the unitId didn't change - if it wasn't set yet, set it
			if (this.unitId == null)
				this.unitId = unitId;
			else if (!this.unitId.equals(unitId))
				throw new MzTabParsingException("Metadata line passed to Unit object (id = " + this.unitId + ") with a different UNIT_ID (" + unitId + ")");
			
			// parse the field
			parseField(subId, field, value);
		}
	}

	private void parseField(String subId, String field, String value) throws MzTabParsingException {
		logger.debug("parsing field: subId = " + subId + ", field = " + field + ", value = " + value);
		
		try {
			// simple fields with only one value
			if ("title".equals(field))
				title = value.trim();
			else if ("description".equals(field) && subId == null)
				description = value.trim();
			else if ("false_discovery_rate".equals(field))
				falseDiscoveryRate = new ParamList(value);
			else if ("uri".equals(field))
				uri = new URI(value);
			else if ("mod".equals(field))
				mod = new ParamList(value);
			else if ("mod-probability_method".equals(field))
				modProbabilityMethod = new Param(value);
			else if ("quantification_method".equals(field))
				quantificationMethod = new Param(value);
			else if ("protein-quantification_unit".equals(field))
				proteinQuantificationUnit = new Param(value);
			else if ("peptide-quantification_unit".equals(field))
				peptideQuantificationUnit = new Param(value);
			
			/**
			 * Complex fields with multiple values
			 */
			
			// sample processing
			else if (field.startsWith("sample_processing")) {
				int sampleProcessingIndex = Integer.parseInt( field.substring(18, field.length() - 1) ); // extract the processing step number
				// create the array if necessary
				if (sampleProcessing == null)
					sampleProcessing = new ArrayList<ParamList>();
				// set the param
				sampleProcessing.add(sampleProcessingIndex - 1, new ParamList(value));
			}
			
			// instruments
			else if (field.startsWith("instrument")) {
				// get the instrument's index
				int instrumentIndex = Integer.parseInt( field.substring(11, field.indexOf(']', 11)) );
				// create the instrument array if necessary
				if (instrument == null)
					instrument = new ArrayList<Instrument>();
				// create the instrument if necessary
				if (instrument.get(instrumentIndex - 1) == null)
					instrument.add(instrumentIndex - 1, new Instrument());
				// check which value is set
				if (field.endsWith("source"))
					instrument.get(instrumentIndex - 1).setSource(new Param(value));
				else if (field.endsWith("analyzer"))
					instrument.get(instrumentIndex - 1).setAnalyzer(new Param(value));
				else if (field.endsWith("detector"))
					instrument.get(instrumentIndex - 1).setDetector(new Param(value));
			}
			
			// software
			else if (field.startsWith("software")) {
				// get the software's 1-based index
				int softwareIndex = Integer.parseInt( field.substring(9, field.length() - 1) );
				// create the software array if necessary
				if (software == null)
					software = new ArrayList<Param>();
				// add the software
				software.add(softwareIndex - 1, new Param(value));
			}
			
			// publication
			else if (field.equals("publication")) {
				// split the string
				String[] publications = value.trim().split("\\|");
				// create the publications array if necessary
				if (publication == null)
					publication = new ArrayList<String>(publications.length);
				// add the publications
				for (String pub : publications)
					publication.add(pub);
			}
			
			// contact
			else if (field.startsWith("contact")) {
				// get the instrument's index
				int contactIndex = Integer.parseInt( field.substring(8, field.indexOf(']', 8)) );
				// create the instrument array if necessary
				if (contact == null)
					contact = new ArrayList<Contact>();
				// create the instrument if necessary
				if (contact.size() < contactIndex)
					contact.add(contactIndex - 1, new Contact());
				// check which value is set
				if (field.endsWith("name"))
					contact.get(contactIndex - 1).setName(value.trim());
				else if (field.endsWith("email"))
					contact.get(contactIndex - 1).setEmail(value.trim());
				else if (field.endsWith("affiliation"))
					contact.get(contactIndex - 1).setAffiliation(value.trim());
			}
			
			// TODO: define how -custom params are handled and react on that
			else if (field.equals("custom")) {
				if (customParams == null)
					customParams = new ArrayList<Param>();
				
				customParams.add(new Param(value));
			}
			
			// species, tissue, cell type, disease - on the unit level
			else if (subId == null && field.startsWith("species")) {
				// get the instrument's index
				int speciesIndex = Integer.parseInt( field.substring(8, field.length() - 1) );
				// create the instrument array if necessary
				if (species == null)
					species = new ArrayList<Param>();
				
				species.add(speciesIndex - 1, new Param(value));
			}
			else if (subId == null && field.startsWith("tissue")) {
				// get the instrument's index
				int tissueIndex = Integer.parseInt( field.substring(7, field.length() - 1) );
				// create the instrument array if necessary
				if (tissue == null)
					tissue = new ArrayList<Param>();
				
				tissue.add(tissueIndex - 1, new Param(value));
			}
			else if (subId == null && field.startsWith("cell_type")) {
				// get the instrument's index
				int cellTypeIndex = Integer.parseInt( field.substring(10, field.length() - 1) );
				// create the instrument array if necessary
				if (cellType == null)
					cellType = new ArrayList<Param>();
				
				cellType.add(cellTypeIndex - 1, new Param(value));
			}
			else if (subId == null && field.startsWith("disease")) {
				// get the instrument's index
				int diseaseIndex = Integer.parseInt( field.substring(8, field.length() - 1) );
				// create the instrument array if necessary
				if (disease == null)
					disease = new ArrayList<Param>();
				
				disease.add(diseaseIndex - 1, new Param(value));
			}
			
			/**
			 * Parse subsample specific data
			 */
			else if (subId != null) {
				// extract the index
				int subIndex = Integer.parseInt( subId.substring(4, subId.length() - 1));
				// make sure the subsample array exists
				if (subsamples == null)
					subsamples = new ArrayList<Subsample>();
				// make sure this subsample already exists
				if (subsamples.size() < subIndex)
					subsamples.add(subIndex - 1, new Subsample(this.unitId, subIndex));
				
				Subsample subsample = subsamples.get(subIndex - 1);
				
				// parse the field
				if ("description".equals(field))
					subsample.setDescription(value.trim());
				else if ("quantitation_reagent".equals(field))
					subsample.setQuantitationReagent(new Param(value));
				else if ("custom".equals(field)) {
					if (subsample.getCustomParams() == null)
						subsample.setCustomParams(new ArrayList<Param>(1));
					subsample.getCustomParams().add(new Param(value));
				}
				else if (field.startsWith("species")) {
					// get the instrument's index
					int speciesIndex = Integer.parseInt( field.substring(8, field.length() - 1) );
					// create the instrument array if necessary
					if (subsample.getSpecies() == null)
						subsample.setSpecies(new ArrayList<Param>());
					
					subsample.getSpecies().add(speciesIndex - 1, new Param(value));
				}
				else if (field.startsWith("tissue")) {
					// get the instrument's index
					int tissueIndex = Integer.parseInt( field.substring(7, field.length() - 1) );
					// create the instrument array if necessary
					if (subsample.getTissue() == null)
						subsample.setTissue(new ArrayList<Param>());
					
					subsample.getTissue().add(tissueIndex - 1, new Param(value));
				}
				else if (field.startsWith("cell_type")) {
					// get the instrument's index
					int cellTypeIndex = Integer.parseInt( field.substring(10, field.length() - 1) );
					// create the instrument array if necessary
					if (subsample.getCellType() == null)
						subsample.setCellType(new ArrayList<Param>());
					
					subsample.getCellType().add(cellTypeIndex - 1, new Param(value));
				}
				else if (field.startsWith("disease")) {
					// get the instrument's index
					int diseaseIndex = Integer.parseInt( field.substring(8, field.length() - 1) );
					// create the instrument array if necessary
					if (subsample.getDisease() == null)
						subsample.setDisease( new ArrayList<Param>() );
					
					subsample.getDisease().add(diseaseIndex - 1, new Param(value));
				}
					
			}
		}
		catch (Exception e) {
			throw new MzTabParsingException("Failed to parse mztab metadata field.", e);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public static Pattern getMztabLinePattern() {
		return MZTAB_LINE_PATTERN;
	}

	public String getUnitId() {
		return unitId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<ParamList> getSampleProcessing() {
		return sampleProcessing;
	}

	public ArrayList<Instrument> getInstrument() {
		return instrument;
	}

	public ArrayList<Param> getSoftware() {
		return software;
	}

	public ParamList getFalseDiscoveryRate() {
		return falseDiscoveryRate;
	}

	public ArrayList<String> getPublication() {
		return publication;
	}

	public ArrayList<Contact> getContact() {
		return contact;
	}

	public URI getUri() {
		return uri;
	}

	public ParamList getMod() {
		return mod;
	}

	public Param getModProbabilityMethod() {
		return modProbabilityMethod;
	}

	public Param getQuantificationMethod() {
		return quantificationMethod;
	}

	public Param getProteinQuantificationUnit() {
		return proteinQuantificationUnit;
	}

	public Param getPeptideQuantificationUnit() {
		return peptideQuantificationUnit;
	}

	public ArrayList<Param> getCustomParams() {
		return customParams;
	}

	public ArrayList<Param> getSpecies() {
		return species;
	}

	public ArrayList<Param> getTissue() {
		return tissue;
	}

	public ArrayList<Param> getCellType() {
		return cellType;
	}

	public ArrayList<Param> getDisease() {
		return disease;
	}

	public ArrayList<Subsample> getSubsamples() {
		return subsamples;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSampleProcessing(ArrayList<ParamList> sampleProcessing) {
		this.sampleProcessing = sampleProcessing;
	}

	public void setInstrument(ArrayList<Instrument> instrument) {
		this.instrument = instrument;
	}

	public void setSoftware(ArrayList<Param> software) {
		this.software = software;
	}

	public void setFalseDiscoveryRate(ParamList falseDiscoveryRate) {
		this.falseDiscoveryRate = falseDiscoveryRate;
	}

	public void setPublication(ArrayList<String> publication) {
		this.publication = publication;
	}

	public void setContact(ArrayList<Contact> contact) {
		this.contact = contact;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public void setMod(ParamList mod) {
		this.mod = mod;
	}

	public void setModProbabilityMethod(Param modProbabilityMethod) {
		this.modProbabilityMethod = modProbabilityMethod;
	}

	public void setQuantificationMethod(Param quantificationMethod) {
		this.quantificationMethod = quantificationMethod;
	}

	public void setProteinQuantificationUnit(Param proteinQuantificationUnit) {
		this.proteinQuantificationUnit = proteinQuantificationUnit;
	}

	public void setPeptideQuantificationUnit(Param peptideQuantificationUnit) {
		this.peptideQuantificationUnit = peptideQuantificationUnit;
	}

	public void setCustomParams(ArrayList<Param> customParams) {
		this.customParams = customParams;
	}

	public void setSpecies(ArrayList<Param> species) {
		this.species = species;
	}

	public void setTissue(ArrayList<Param> tissue) {
		this.tissue = tissue;
	}

	public void setCellType(ArrayList<Param> cellType) {
		this.cellType = cellType;
	}

	public void setDisease(ArrayList<Param> disease) {
		this.disease = disease;
	}

	public void setSubsamples(ArrayList<Subsample> subsamples) {
		this.subsamples = subsamples;
	}

	/**
	 * Converts the given meta-data to an mzTab formatted string.
	 * @return
	 */
	public String toMzTab() {
		String mzTab = "";
		
		if (title != null)
			mzTab += createField("title", title);
		if (description != null)
			mzTab += createField("description", description);
		// sample processing
		if (sampleProcessing != null) {
			for (Integer i = 1; i <= sampleProcessing.size(); i++)
				mzTab += createField(String.format("sample_processing[%d]", i), sampleProcessing.get(i - 1));
		}
		// instrument
		if (instrument != null) {
			for (Integer i = 1; i <= instrument.size(); i++) {
				mzTab += createField(String.format("instrument[%d]-source", i), instrument.get(i - 1).getSource());
				mzTab += createField(String.format("instrument[%d]-analyzer", i), instrument.get(i - 1).getAnalyzer());
				mzTab += createField(String.format("instrument[%d]-detector", i), instrument.get(i - 1).getDetector());
			}
		}
		// software
		if (software != null) {
			for (Integer i = 1; i <= software.size(); i++)
				mzTab += createField(String.format("software[%d]", i), software.get(i - 1));
		}
		// false discovery rate
		if (falseDiscoveryRate != null)
			mzTab += createField("false_discovery_rate", falseDiscoveryRate);
		// publication
		if (publication != null) {
			String string = "";
			
			for (String p : publication)
				string += (string.length() > 1 ? "," : "") + p;
			
			mzTab += createField("publication", string);
		}
		// contact
		if (contact != null) {
			for (int i = 1; i <= contact.size(); i++) {
				mzTab += createField(String.format("contact[%d]-name", i), contact.get(i - 1).getName());
				mzTab += createField(String.format("contact[%d]-affiliation", i), contact.get(i - 1).getAffiliation());
				mzTab += createField(String.format("contact[%d]-email", i), contact.get(i - 1).getEmail());
			}
		}
		// uri
		if (uri != null)
			mzTab += createField("uri", uri);
		// mods
		if (mod != null)
			mzTab += createField("mod", mod);
		// mod probability method
		if (modProbabilityMethod != null)
			mzTab += createField("mod-probability_method", modProbabilityMethod);
		// quantification method
		if (quantificationMethod != null)
			mzTab += createField("quantification_method", quantificationMethod);
		// protein quant unit
		if (proteinQuantificationUnit != null)
			mzTab += createField("protein-quantification_unit", proteinQuantificationUnit);
		// peptide quant unit
		if (peptideQuantificationUnit != null)
			mzTab += createField("peptide-quantification_unit", peptideQuantificationUnit);
		// custom
		if (customParams != null) {
			for (Param p : customParams)
				mzTab += createField("custom", p);
		}
		// species
		if (species != null) {
			for (int i = 1; i <= species.size(); i++)
				mzTab += createField(String.format("species[%d]", i), species.get(i - 1));
		}
		// tissue
		if (tissue != null) {
			for (int i = 1; i <= tissue.size(); i++)
				mzTab += createField(String.format("tissue[%d]", i), tissue.get(i - 1));
		}
		// cell_type
		if (cellType != null) {
			for (int i = 1; i <= cellType.size(); i++)
				mzTab += createField(String.format("cell_type[%d]", i), cellType.get(i - 1));
		}
		// disease
		if (disease != null) {
			for (int i = 1; i <= disease.size(); i++)
				mzTab += createField(String.format("disease[%d]", i), disease.get(i - 1));
		}
		// subsamples
		if (subsamples != null) {
			for (Subsample s : subsamples)
				mzTab += s.toMzTab();
		}
		
		return mzTab;
	}
	
	private String createField(String fieldName, Object value) {
		if (value == null)
			return "";
		
		return unitId + "-" + fieldName + MzTabFile.SEPARATOR + value.toString() + MzTabFile.EOL;
	}
}

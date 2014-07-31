package uk.ac.ebi.pride.jmztab.utils.parser;

import uk.ac.ebi.pride.jmztab.model.*;
import uk.ac.ebi.pride.jmztab.utils.errors.LogicalErrorType;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabError;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabException;

/**
 * Parse and validate Peptide header line into a {@link MZTabColumnFactory}.
 *
 * @author qingwei
 * @author ntoro
 * @since 10/02/13
 */
public class PEHLineParser extends MZTabHeaderLineParser {
    public PEHLineParser(Metadata metadata) {
        super(MZTabColumnFactory.getInstance(Section.Peptide_Header), metadata);
    }

    public void parse(int lineNumber, String line, MZTabErrorList errorList) throws MZTabException {
        super.parse(lineNumber, line, errorList);
    }

    //TODO review doc

    /**
     * In "Quantification" file, following optional columns are mandatory:
     * 1. peptide_abundance_study_variable[1-n]
     * 2. peptide_abundance_stdev_study_variable[1-n]
     * 3. peptide_abundance_std_error_study_variable[1-n]
     * 4. best_search_engine_score[1-n]
     *
     * Beside above, in "Complete" and "Quantification" file, following optional columns are also mandatory:
     * 1. search_engine_score[1-n]_ms_run[1-n]
     * 2. peptide_abundance_assay[1-n]
     * 3. spectra_ref             // This is special, currently all "Quantification" file's peptide line header
     *                            // should provide, because it is difficult to judge MS2 based quantification employed.
     *
     * NOTICE: this method will be called at end of parse() function.
     *
     * @see MZTabHeaderLineParser#parse(int, String, MZTabErrorList)
     * @see #refineOptionalColumn(MZTabDescription.Mode, MZTabDescription.Type, String)
     */
    @Override
    protected void refine() throws MZTabException {
        MZTabDescription.Mode mode = metadata.getMZTabMode();
        MZTabDescription.Type type = metadata.getMZTabType();

        //peptide_search_engine_score
        if (metadata.getPeptideSearchEngineScoreMap().size() == 0) {
            throw new MZTabException(new MZTabError(LogicalErrorType.NotDefineInMetadata, lineNumber, "peptide_search_engine_score[1-n]", mode.toString(), type.toString()));
        }

        if (type == MZTabDescription.Type.Quantification) {
            if (metadata.getPeptideQuantificationUnit() == null) {
                throw new MZTabException(new MZTabError(LogicalErrorType.NotDefineInMetadata, lineNumber, "peptide-quantification_unit", mode.toString(), type.toString()));
            }
            for (SearchEngineScore searchEngineScore : metadata.getPeptideSearchEngineScoreMap().values()) {
                String searchEngineScoreLabel = "[" + searchEngineScore.getId() + "]";
                refineOptionalColumn(mode, type, "best_search_engine_score" + searchEngineScoreLabel);
            }

            for (StudyVariable studyVariable : metadata.getStudyVariableMap().values()) {
                String svLabel = "_study_variable[" + studyVariable.getId() + "]";
                refineOptionalColumn(mode, type, "peptide_abundance" + svLabel);
                refineOptionalColumn(mode, type, "peptide_abundance_stdev" + svLabel);
                refineOptionalColumn(mode, type, "peptide_abundance_std_error" + svLabel);
            }
            if (mode == MZTabDescription.Mode.Complete) {
                for (MsRun msRun : metadata.getMsRunMap().values()) {
                    String msRunLabel = "_ms_run[" + msRun.getId() + "]";
                    for (SearchEngineScore searchEngineScore : metadata.getPeptideSearchEngineScoreMap().values()) {
                        String searchEngineScoreLabel = "[" + searchEngineScore.getId() + "]";
                        refineOptionalColumn(mode, type, "search_engine_score" + searchEngineScoreLabel + msRunLabel);
                    }
                }
                for (Assay assay : metadata.getAssayMap().values()) {
                    String assayLabel = "_assay[" + assay.getId() + "]";
                    refineOptionalColumn(mode, type, "peptide_abundance" + assayLabel);
                }
            }
        }
    }
}

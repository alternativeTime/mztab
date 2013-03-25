package uk.ac.ebi.pride.jmztab.utils;

import uk.ac.ebi.pride.jmztab.model.*;

import java.util.*;

/**
* User: qingwei
* Date: 28/02/13
*/
public class MZTabFileMerger {
    private List<MZTabFile> mzTabFileList = new ArrayList<MZTabFile>();

    /**
     * Whether combine abundance columns and metadata which has same SubSample.
     */
    private boolean combine = false;

    public MZTabFileMerger() {}

    public void addAllTabFiles(Collection<MZTabFile> tabFileList) {
        mzTabFileList.addAll(tabFileList);
    }

    public void addTabFile(MZTabFile tabFile) {
        mzTabFileList.add(tabFile);
    }

    public List<MZTabFile> getMzTabFileList() {
        return Collections.unmodifiableList(mzTabFileList);
    }

    /**
     * Whether combine abundance columns and metadata which has same SubSample
     */
    public boolean isCombine() {
        return combine;
    }

    public void setCombine(boolean combine) {
        this.combine = combine;
    }

    /**
     * auto generate a new unit_id, like:
     * {oldUnitId}_{xxxxxx}.
     */
    private String generateNewUnitId(String oldUnitId) {
        return oldUnitId + "_" + System.currentTimeMillis();
    }

    /**
     * Check whether overlap between two collection. If there exists more than one unit_id, which belong to
     * two collections, return first matched. Otherwise, return null;
     */
    private String overlap(Set<String> srcUnitIds, Set<String> tarUnitIds) {
        for (String unitId : srcUnitIds) {
            if (tarUnitIds.contains(unitId)) {
                return unitId;
            }
        }
        return null;
    }

    private Integer overlap(Set<Integer> srcPositions, Set<Integer> tarPositions) {
        for (Integer position : srcPositions) {
            if (tarPositions.contains(position)) {
                return position;
            }
        }
        return null;
    }

    /**
     * merge srcFile into tarFile. In new MZTabFile, all tarFile metadata, header section, data section not change.
     * srcFile metadata, optional header columns, data position maybe changed, if there exists conflict.
     */
    private MZTabFile mergeFile(MZTabFile srcFile, MZTabFile tarFile) {
        // Step 1: combine metadata
        Metadata metadata = new Metadata();
        Metadata tarMetadata = tarFile.getMetadata();
        Metadata srcMetadata = srcFile.getMetadata();

        for (Unit tarUnit : tarMetadata.values()) {
            metadata.addUnit(tarUnit);
        }

        // modify src metadata overlap unit id.
        String unitId;
        while ((unitId = overlap(srcMetadata.getUnitIds(), tarMetadata.getUnitIds())) != null) {
            String newUnitId = generateNewUnitId(unitId);
            srcFile.modifyUnitId(unitId, newUnitId);
        }

        SubUnit subUnit;
        for (Unit srcUnit : srcMetadata.values()) {
            // modify sub unit id.
            if (srcUnit instanceof SubUnit) {
                subUnit = (SubUnit) srcUnit;
                int subId = metadata.getSubUnits().lastKey();
                if (subUnit.getSubId() <= subId && !combine) {
                    subUnit.setSubId(subId + 1);
                }
            }

            metadata.addUnit(srcUnit);
        }
        MZTabFile tabFile = new MZTabFile(metadata);

        // combine protein header line.
        MZTabColumnFactory tarProteinColumnFactory = tarFile.getProteinColumnFactory();
        MZTabColumnFactory srcProteinColumnFactory = srcFile.getProteinColumnFactory();
        MZTabColumnFactory proteinColumnFactory;

        if (tarProteinColumnFactory != null) {
            proteinColumnFactory = MZTabColumnFactory.getInstance(Section.Protein);
            proteinColumnFactory.addAllAbundanceColumn(tarProteinColumnFactory.getAbundanceColumnMapping().values());
            proteinColumnFactory.addAllOptionalColumn(tarProteinColumnFactory.getOptionalColumnMapping().values());

            if (srcProteinColumnFactory != null) {
                // combine two protein header columns. move srcTabFile optional columns to the left.
                if (!combine) {
                    int offset = tarProteinColumnFactory.getColumnMapping().lastKey() - tarProteinColumnFactory.getStableColumnMapping().lastKey();
                    Integer position;
                    while ((position = overlap(srcProteinColumnFactory.getAbundanceColumnMapping().keySet(), tarProteinColumnFactory.getAbundanceColumnMapping().keySet())) != null) {
                        srcProteinColumnFactory.modifyColumnPosition(position, position + offset);
                    }
                    while ((position = overlap(srcProteinColumnFactory.getOptionalColumnMapping().keySet(), tarProteinColumnFactory.getOptionalColumnMapping().keySet())) != null) {
                        srcProteinColumnFactory.modifyColumnPosition(position, position + offset);
                    }
                }

                proteinColumnFactory.addAllAbundanceColumn(srcProteinColumnFactory.getAbundanceColumnMapping().values());
                proteinColumnFactory.addAllOptionalColumn(srcProteinColumnFactory.getOptionalColumnMapping().values());
            }
        } else if (srcProteinColumnFactory != null) {
            proteinColumnFactory = MZTabColumnFactory.getInstance(Section.Protein);
            proteinColumnFactory.addAllAbundanceColumn(srcProteinColumnFactory.getAbundanceColumnMapping().values());
            proteinColumnFactory.addAllOptionalColumn(srcProteinColumnFactory.getOptionalColumnMapping().values());
        } else {
            proteinColumnFactory = null;
        }
        tabFile.setProteinColumnFactory(proteinColumnFactory);


        // combine protein data table.
        if (proteinColumnFactory != null) {
            for (Protein protein : tarFile.getProteins()) {
                tabFile.addProtein(protein);
            }
            for (Protein protein : srcFile.getProteins()) {
                tabFile.addProtein(protein);
            }
        }

        // combine peptide header line.
        MZTabColumnFactory tarPeptideColumnFactory = tarFile.getPeptideColumnFactory();
        MZTabColumnFactory srcPeptideColumnFactory = srcFile.getPeptideColumnFactory();
        MZTabColumnFactory peptideColumnFactory;

        if (tarPeptideColumnFactory != null) {
            peptideColumnFactory = MZTabColumnFactory.getInstance(Section.Peptide);
            peptideColumnFactory.addAllAbundanceColumn(tarPeptideColumnFactory.getAbundanceColumnMapping().values());
            peptideColumnFactory.addAllOptionalColumn(tarPeptideColumnFactory.getOptionalColumnMapping().values());

            if (srcPeptideColumnFactory != null) {
                // combine two peptide header columns. move srcTabFile optional columns to the left.
                if (!combine) {
                    int offset = tarPeptideColumnFactory.getColumnMapping().lastKey() - tarPeptideColumnFactory.getStableColumnMapping().lastKey();
                    Integer position;
                    while ((position = overlap(srcPeptideColumnFactory.getAbundanceColumnMapping().keySet(), tarPeptideColumnFactory.getAbundanceColumnMapping().keySet())) != null) {
                        srcPeptideColumnFactory.modifyColumnPosition(position, position + offset);
                    }
                    while ((position = overlap(srcPeptideColumnFactory.getOptionalColumnMapping().keySet(), tarPeptideColumnFactory.getOptionalColumnMapping().keySet())) != null) {
                        srcPeptideColumnFactory.modifyColumnPosition(position, position + offset);
                    }
                }

                peptideColumnFactory.addAllAbundanceColumn(srcPeptideColumnFactory.getAbundanceColumnMapping().values());
                peptideColumnFactory.addAllOptionalColumn(srcPeptideColumnFactory.getOptionalColumnMapping().values());
            }
        } else if (srcPeptideColumnFactory != null) {
            peptideColumnFactory = MZTabColumnFactory.getInstance(Section.Peptide);
            peptideColumnFactory.addAllAbundanceColumn(srcPeptideColumnFactory.getAbundanceColumnMapping().values());
            peptideColumnFactory.addAllOptionalColumn(srcPeptideColumnFactory.getOptionalColumnMapping().values());
        } else {
            peptideColumnFactory = null;
        }
        tabFile.setPeptideColumnFactory(peptideColumnFactory);


        // combine peptide data table.
        if (peptideColumnFactory != null) {
            for (Peptide peptide : tarFile.getPeptides()) {
                tabFile.addPeptide(peptide);
            }
            for (Peptide peptide : srcFile.getPeptides()) {
                tabFile.addPeptide(peptide);
            }
        }

        // combine small molecule header line.
        MZTabColumnFactory tarSmallMoleculeColumnFactory = tarFile.getSmallMoleculeColumnFactory();
        MZTabColumnFactory srcSmallMoleculeColumnFactory = srcFile.getSmallMoleculeColumnFactory();
        MZTabColumnFactory smallMoleculeColumnFactory;

        if (tarSmallMoleculeColumnFactory != null) {
            smallMoleculeColumnFactory = MZTabColumnFactory.getInstance(Section.Small_Molecule);
            smallMoleculeColumnFactory.addAllAbundanceColumn(tarSmallMoleculeColumnFactory.getAbundanceColumnMapping().values());
            smallMoleculeColumnFactory.addAllOptionalColumn(tarSmallMoleculeColumnFactory.getOptionalColumnMapping().values());

            if (srcSmallMoleculeColumnFactory != null) {
                // combine two small molecule header columns. move srcTabFile optional columns to the left.
                if (!combine) {
                    int offset = tarSmallMoleculeColumnFactory.getColumnMapping().lastKey() - tarSmallMoleculeColumnFactory.getStableColumnMapping().lastKey();
                    Integer position;
                    while ((position = overlap(srcSmallMoleculeColumnFactory.getAbundanceColumnMapping().keySet(), tarSmallMoleculeColumnFactory.getAbundanceColumnMapping().keySet())) != null) {
                        srcSmallMoleculeColumnFactory.modifyColumnPosition(position, position + offset);
                    }
                    while ((position = overlap(srcSmallMoleculeColumnFactory.getOptionalColumnMapping().keySet(), tarSmallMoleculeColumnFactory.getOptionalColumnMapping().keySet())) != null) {
                        srcSmallMoleculeColumnFactory.modifyColumnPosition(position, position + offset);
                    }
                }

                smallMoleculeColumnFactory.addAllAbundanceColumn(srcSmallMoleculeColumnFactory.getAbundanceColumnMapping().values());
                smallMoleculeColumnFactory.addAllOptionalColumn(srcSmallMoleculeColumnFactory.getOptionalColumnMapping().values());
            }
        } else if (srcSmallMoleculeColumnFactory != null) {
            smallMoleculeColumnFactory = MZTabColumnFactory.getInstance(Section.Small_Molecule);
            smallMoleculeColumnFactory.addAllAbundanceColumn(srcSmallMoleculeColumnFactory.getAbundanceColumnMapping().values());
            smallMoleculeColumnFactory.addAllOptionalColumn(srcSmallMoleculeColumnFactory.getOptionalColumnMapping().values());
        } else {
            smallMoleculeColumnFactory = null;
        }
        tabFile.setSmallMoleculeColumnFactory(smallMoleculeColumnFactory);


        // combine small molecule data table.
        if (smallMoleculeColumnFactory != null) {
            for (SmallMolecule smallMolecule : tarFile.getSmallMolecules()) {
                tabFile.addSmallMolecule(smallMolecule);
            }
            for (SmallMolecule smallMolecule : srcFile.getSmallMolecules()) {
                tabFile.addSmallMolecule(smallMolecule);
            }
        }


        return tabFile;
    }

    public MZTabFile merge() {
        if (mzTabFileList.isEmpty()) {
            return null;
        }

        MZTabFile mainTabFile = mzTabFileList.get(0);
        mzTabFileList.remove(0);
        if (mzTabFileList.size() == 0) {
            return mainTabFile;
        }

        for (MZTabFile tabFile : mzTabFileList) {
            mainTabFile = mergeFile(tabFile, mainTabFile);
        }

        return mainTabFile;
    }
}

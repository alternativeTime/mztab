package uk.ac.ebi.pride.jmztab.errors;

/**
 * User: Qingwei
 * Date: 29/01/13
 */
public class LogicalErrorType extends MZTabErrorType {
    public static MZTabErrorType NULL = createError(Category.Logical, "NULL");

    public static MZTabErrorType Duplication = createError(Category.Logical, "Duplication");
    public static MZTabErrorType UnitID = createError(Category.Logical, "UnitID");
    public static MZTabErrorType ColUnit = createError(Category.Logical, "ColUnit");


//    public static MZTabErrorType MTDDefineIDLabel = createError(Category.Logical, "MTDDefineIDLabel");
//    public static MZTabErrorType MTDElementLabel = createError(Category.Logical, "MTDElementLabel");
//    public static MZTabErrorType MTDPropertyLabel = createError(Category.Logical, "MTDPropertyLabel");
}

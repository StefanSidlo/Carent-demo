package model;

/**
 * @author Stefan Sidlovsky
 */
public enum FourXFour {
    YES,
    NO;

    public String toDaoFormat() {
        switch (this){
            case YES:
                return "YES";
            case NO:
                return "NO";
            default:
                return "";
        }
    }

    public static FourXFour fromDaoFormat(String daoText){
        switch (daoText){
            case "YES":
                return YES;
            case "NO":
                return NO;
            default:
                throw new IllegalArgumentException("Unsupported dao text representation");
        }
    }
}

package model;

/**
 * @author Stefan Sidlovsky
 */
public enum Availability {
    AVAILABLE,
    ORDERED;

    public String toDaoFormat() {
        switch (this){
            case AVAILABLE:
                return "AV";
            case ORDERED:
                return "OD";
            default:
                return "";
        }
    }

    public static Availability fromDaoFormat(String daoText){
        switch (daoText){
            case "AV":
                return AVAILABLE;
            case "OD":
                return ORDERED;
            default:
                throw new IllegalArgumentException("Unsupported dao text representation");
        }
    }
}

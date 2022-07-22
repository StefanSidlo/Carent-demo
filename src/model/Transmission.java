package model;

/**
 * @author Stefan Sidlovsky
 */
public enum Transmission {
    MANUAL,
    AUTOMATIC;


    public String toDaoFormat() {
        switch (this){
            case MANUAL:
                return "MANUAL";
            case AUTOMATIC:
                return "AUTOMATIC";
            default:
                return "";
        }
    }

    public static Transmission fromDaoFormat(String daoText){
        switch (daoText){
            case "MANUAL":
                return MANUAL;
            case "AUTOMATIC":
                return AUTOMATIC;
            default:
                throw new IllegalArgumentException("Unsupported dao text representation");
        }
    }
}

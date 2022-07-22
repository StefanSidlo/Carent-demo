package model;

/**
 * @author Stefan Sidlovsky
 */
public enum Fuel {
    DIESEL,
    PETROL;

    public String toDaoFormat() {
        switch (this){
            case DIESEL:
                return "DIESEL";
            case PETROL:
                return "PETROL";
            default:
                return "";
        }
    }

    public static Fuel fromDaoFormat(String daoText){
        switch (daoText){
            case "DIESEL":
                return DIESEL;
            case "PETROL":
                return PETROL;
            default:
                throw new IllegalArgumentException("Unsupported dao text representation");
        }
    }
}

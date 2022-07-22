package model;

/**
 * @author Stefan Sidlovsky
 */
public enum OrderProgress {
    IN_PROGRESS,
    FINISHED,
    CANCELED;

    public String toDaoFormat() {
        switch (this){
            case IN_PROGRESS:
                return "IP";
            case FINISHED:
                return "FI";
            case CANCELED:
                return "CA";
            default:
                return "";
        }
    }

    public static OrderProgress fromDaoFormat(String daoText){
        switch (daoText){
            case "IP":
                return IN_PROGRESS;
            case "FI":
                return FINISHED;
            case "CA":
                return CANCELED;
            default:
                throw new IllegalArgumentException("Unsupported dao text representation");
        }
    }
}

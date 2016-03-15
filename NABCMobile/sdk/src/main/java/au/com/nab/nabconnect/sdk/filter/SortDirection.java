package au.com.nab.nabconnect.sdk.filter;

/**
 * Created by quangnguyen on 13/03/2016.
 */
public enum SortDirection {
    ASC("asc"),
    DESC("desc");

    private String value;

    private SortDirection(String value) {
        this.value = value;
    }
}

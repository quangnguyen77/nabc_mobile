package au.com.nab.nabconnect.sdk.filter;

/**
 * Created by quangnguyen on 13/03/2016.
 */
public class PaymentFilter {

    private static final int DEFAULT_NUM_RECORD_PER_PAGE = 50;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private PaymentSortCriteria sortCriteria = PaymentSortCriteria.PAYMENT_ID;
    private SortDirection sortDirection = SortDirection.DESC;
    /**
     *
     */
    private int page = DEFAULT_PAGE_NUMBER;
    private int rows = DEFAULT_NUM_RECORD_PER_PAGE;
    private boolean authorisable;

    public PaymentSortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(PaymentSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public boolean isAuthorisable() {
        return authorisable;
    }

    public void setAuthorisable(boolean authorisable) {
        this.authorisable = authorisable;
    }
}

package au.com.nab.nabconnect.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by p714908 on 8/12/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentList {

    @JsonProperty("rows")
    private List<Payment> payments;

    /**
     * the page number.
     */
    private int page;

    /**
     * the total number of pages.
     */
    private int total;

    /**
     * The total number of records.
     */
    private int totalRecords;


    public PaymentList() {
    }

    /**
     * get the row data.
     *
     * @return
     */
    public List<Payment> getPayments() {
        return payments;
    }

    public int getPaymentCount() {
        if (payments != null) {
            return payments.size();
        }
        return 0;
    }
    /**
     * set the row data.
     *
     * @param payments
     *            the payments
     */
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    /**
     * get the page.
     *
     * @return
     */
    public int getPage() {
        return page;
    }

    /**
     * set the page.
     *
     * @param page
     *            the page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * get the records.
     *
     * @return the record count.
     */
    public int getRecords() {
        return totalRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * get the total page count.
     *
     * @return total page count.
     */
    public int getTotal() {
        return total;
    }

    /**
     * set the total page count.
     *
     * @param total
     *            count
     */
    public void setTotal(int total) {
        this.total = total;
    }

}

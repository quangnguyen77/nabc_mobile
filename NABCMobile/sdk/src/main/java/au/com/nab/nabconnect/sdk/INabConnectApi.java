package au.com.nab.nabconnect.sdk;

import au.com.nab.nabconnect.sdk.filter.PaymentFilter;
import au.com.nab.nabconnect.sdk.response.AccountBalancesResponse;
import au.com.nab.nabconnect.sdk.response.AccountEndOfDayBalancesResponse;
import au.com.nab.nabconnect.sdk.response.AccountTransactionsResponse;
import au.com.nab.nabconnect.sdk.response.PaymentsResponse;
import au.com.nab.nabconnect.sdk.response.SDKResponse;

/**
 * Created by nabcmacbook1 on 15/03/2016.
 */
public interface INabConnectApi {

    /**
     * Check if we have successfully authenticated with NABC
     * @return
     */
    boolean isLogin();

    /**
     * Login with the given username and password
     * @param username
     * @param password
     * @return
     */
    SDKResponse login(String username, String password);

    /**
     * Logout of the current session
     * @return
     */
    SDKResponse logout();


    /**
     * Get current balances for all accounts accessible by the current user
     * @return
     */
    AccountBalancesResponse getAccountBalances();

    /**
     * Get end of day balances for all accounts accessible by the current user
     * @return
     */
    AccountEndOfDayBalancesResponse getAccountEndOfDayBalances();

    /**
     * Get all transaction for the given account
     * @return
     */
    AccountTransactionsResponse getAccountTransactions(String accountId);

    /**
     * Get all payments accessible by the current user
     * @return
     */
    PaymentsResponse getPayments(PaymentFilter filter);
}

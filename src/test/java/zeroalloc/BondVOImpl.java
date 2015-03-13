package zeroalloc;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 12/03/2015.
 */
public class BondVOImpl implements BondVOInterface {
    private double coupon;
    private int id;
    private long issueDate;
    private long maturityDate;
    private long toAdd;
    private String symbol;
    private List<MarketPx> marketPrices = new ArrayList(24);
    private long quantity;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public long getIssueDate() {
        return 0;
    }

    @Override
    public void setIssueDate(long issueDate) {

        this.issueDate = issueDate;
    }

    @Override
    public long getMaturityDate() {
        return 0;
    }

    @Override
    public void setMaturityDate(long maturityDate) {
        this.maturityDate = maturityDate;
    }

    @Override
    public long addAtomicMaturityDate(long toAdd) {
        this.toAdd = toAdd;
        return 0;
    }

    @Override
    public boolean compareAndSwapCoupon(double expected, double value) {
        return false;
    }

    @Override
    public double getCoupon() {
        return coupon;
    }

    @Override
    public void setCoupon(double coupon) {
        this.coupon = coupon;
    }

    @Override
    public long getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public double addAtomicCoupon(double toAdd) {
        return 0;
    }

    @Override
    public String getSymbol() {
        return null;
    }

    @Override
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void setMarketPxIntraDayHistoryAt(int tradingDayHour, MarketPx mPx) {
        marketPrices.set(tradingDayHour, mPx);
    }

    @Override
    public MarketPx getMarketPxIntraDayHistoryAt(int tradingDayHour) {
        return null;
    }
}

package praktikum.stellar.model.order;

import java.util.ArrayList;

public class OrdersGetResponse {
    private ArrayList<Order> orders;
    private int total;
    private int totalToday;

    public OrdersGetResponse(ArrayList<Order> orders, int total, int totalToday) {
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }
}

package com.ecohub;

import java.math.BigDecimal;

public class result {
    BigDecimal input;
    String activity;
    String category;

    public result(String category, String activity, BigDecimal d) {
        this.category = category;
        this.activity = activity;
        this.input = d;
    }

    public BigDecimal getInput() {
        return input;
    }

    public void setInput(BigDecimal input) {
        this.input = input;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    // getter and setter for category
    public String getCategory() {
        return category;
    }

    // setter for category
    public void setCategory(String category) {
        this.category = category;
    }

}

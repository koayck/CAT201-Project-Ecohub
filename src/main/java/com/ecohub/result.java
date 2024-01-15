package com.ecohub;

import java.math.BigDecimal;

public class result {
    BigDecimal input;
    String activity;

    public result(String string, BigDecimal d) {
        this.activity = string;
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

}

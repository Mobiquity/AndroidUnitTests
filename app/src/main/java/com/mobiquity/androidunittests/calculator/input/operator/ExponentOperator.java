package com.mobiquity.androidunittests.calculator.input.operator;

import com.mobiquity.androidunittests.R;

public class ExponentOperator extends Operator {

    public ExponentOperator() {
        super(R.string.exponent_op_value);
    }

    @Override
    public double execute(double param1, double param2) {
        return pow(param1, param2);
    }

    @Override
    public int getPrecedence() {
        return Precedence.POWER_PRECENDENCE.getValue();
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }


    private double pow(double param1, double param2) {
        return Math.pow(param1, param2);
    }
}

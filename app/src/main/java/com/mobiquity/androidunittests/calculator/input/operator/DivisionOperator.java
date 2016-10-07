package com.mobiquity.androidunittests.calculator.input.operator;

import com.mobiquity.androidunittests.R;

public class DivisionOperator extends Operator {

    public DivisionOperator() {
        super(R.string.divide_op);
    }

    @Override
    public double execute(double param1, double param2) {
        return divide(param1, param2);
    }

    @Override
    public int getPrecedence() {
        return Precedence.MULTIPLICATION_PRECEDENCE.getValue();
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }

    private double divide(double firstAddend, double secondAddend) {
        return firstAddend / secondAddend;
    }
}

package com.mobiquity.androidunittests.calculator.input.operator;

import com.mobiquity.androidunittests.R;
import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.InputType;

import java.util.List;
import java.util.Map;

public class SubtractionOperator extends Operator {
    public SubtractionOperator() {
        super(R.string.substract_op);
    }

    @Override
    public double execute(double param1, double param2) {
        return subtract(param1, param2);
    }

    @Override
    public int getPrecedence() {
        return Precedence.SUBTRACTION_PRECEDENCE.getValue();
    }

    @Override
    public boolean isLeftAssociative() {
        return true;
    }


    private double subtract(double minuend, double subtrahend) {
        return minuend - subtrahend;
    }

    @Override
    public NormalizeRule getNormalizeExpressionRule(Map<String, Input> expressionInputMap, List<String> expression) {
        //Don't allow -- or +-
        if (!expression.isEmpty()) {
            String lastItem = expression.get(expression.size() - 1);
            if (expressionInputMap.containsKey(lastItem)) {
                Input lastInput = expressionInputMap.get(lastItem);
                if (lastInput instanceof AdditionOperator || lastInput instanceof SubtractionOperator) {
                    return NormalizeRule.REPLACE;
                }
            }
        }

        return super.getNormalizeExpressionRule(expressionInputMap, expression);
    }
}

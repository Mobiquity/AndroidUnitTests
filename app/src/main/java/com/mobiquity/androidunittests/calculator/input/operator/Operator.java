package com.mobiquity.androidunittests.calculator.input.operator;

import android.support.annotation.StringRes;

import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.InputType;
import com.mobiquity.androidunittests.calculator.input.LeftParenInput;

import java.util.List;
import java.util.Map;

public abstract class Operator extends Input {
    public Operator(@StringRes int operatorRes) {
        super(operatorRes, InputType.OPERATOR);
    }

    public abstract double execute(double param1, double param2);
    public abstract int getPrecedence();
    public abstract boolean isLeftAssociative();

    public enum Precedence {
        ADDITION_PRECEDENCE(2),
        SUBTRACTION_PRECEDENCE(2),
        MULTIPLICATION_PRECEDENCE(3),
        DIVISION_PRECEDENCE(3),
        POWER_PRECENDENCE(4);

        private int value;

        Precedence(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    public NormalizeRule getNormalizeExpressionRule(Map<String, Input> expressionInputMap, List<String> expression) {
        //Don't allow leading operator
        if (expression.isEmpty()) {
            return NormalizeRule.REMOVE;
        }

        //Don't allow multiple operators
        String lastItem = expression.get(expression.size() - 1);

        if (expressionInputMap.containsKey(lastItem)) {
            Input lastInput = expressionInputMap.get(lastItem);

            if (lastInput instanceof Operator) {
                for (int i = expression.size() - 1; i >= 0; i--) {
                    lastItem = expression.get(i);

                    if (expressionInputMap.containsKey(lastItem)) {
                        lastInput = expressionInputMap.get(lastItem);

                        if (!(lastInput instanceof Operator)) {
                            break;
                        }

                        if (!(this instanceof SubtractionOperator) || i != expression.size() - 1) {
                            expression.remove(i);
                        }
                    }
                }
            }

            if (lastInput instanceof LeftParenInput && !(this instanceof SubtractionOperator)) {
                return NormalizeRule.REMOVE;
            }
        }

        return NormalizeRule.ADD;
    }
}

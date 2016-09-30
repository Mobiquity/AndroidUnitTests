package com.mobiquity.androidunittests.calculator;

import android.content.Context;

import com.mobiquity.androidunittests.R;
import com.mobiquity.androidunittests.calculator.input.DecimalInput;
import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.LeftParenInput;
import com.mobiquity.androidunittests.calculator.input.NumericInput;
import com.mobiquity.androidunittests.calculator.input.RightParenInput;
import com.mobiquity.androidunittests.calculator.input.operator.AdditionOperator;
import com.mobiquity.androidunittests.calculator.input.operator.DivisionOperator;
import com.mobiquity.androidunittests.calculator.input.operator.MultiplicationOperator;
import com.mobiquity.androidunittests.calculator.input.operator.Operator;
import com.mobiquity.androidunittests.calculator.input.operator.SubtractionOperator;
import com.mobiquity.androidunittests.util.ExpressionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Class responsible for normalizing calculator expression input
 * as well as converting a string expression to a list of inputs
 * for the calculator
 */
public class ExpressionConverter {

    private final Map<String, Input> expressionInputMap;

    @Inject
    public ExpressionConverter(Context context) {
        expressionInputMap = new HashMap<String, Input>() {{
            put(context.getString(R.string.add_op), new AdditionOperator());
            put(context.getString(R.string.substract_op), new SubtractionOperator());
            put(context.getString(R.string.multiply_op), new MultiplicationOperator());
            put(context.getString(R.string.divide_op), new DivisionOperator());
            put(context.getString(R.string.left_paren), new LeftParenInput());
            put(context.getString(R.string.right_paren), new RightParenInput());
            put(context.getString(R.string.decimal_symbol), new DecimalInput());
        }};
    }

    private boolean isInputAllowed(String input) {
        return ExpressionUtil.isNumeric(input) ||
                expressionInputMap.containsKey(input);
    }

    public List<String> normalize(List<String> expression) {
        List<String> normalizedExpression = new ArrayList<>();
        for(int i = 0; i < expression.size(); i++) {
            String expressionItem = expression.get(i);

            if(isInputAllowed(expressionItem)) {
                Input.NormalizeRule normalizeExpressionRule = Input.NormalizeRule.ADD;
                if(expressionInputMap.containsKey(expressionItem)) {
                    Input calculatorInput = expressionInputMap.get(expressionItem);
                    normalizeExpressionRule = calculatorInput.getNormalizeExpressionRule(expressionInputMap, normalizedExpression);
                }

                switch (normalizeExpressionRule) {
                    case ADD:
                        normalizedExpression.add(expressionItem);
                        break;
                    case REPLACE:
                        int lastItemIndex = normalizedExpression.size()-1;
                        if(lastItemIndex > -1) {
                            normalizedExpression.remove(lastItemIndex);
                            normalizedExpression.add(expressionItem);
                        }
                        break;
                }
            }
        }

        return normalizedExpression;
    }

    public List<Input> convert(List<String> expression) {
        List<Input> calculatorInputs = new ArrayList<>();
        for(String textInput : expression) {
            Input calculatorInput;
            if(ExpressionUtil.isNumeric(textInput)) {
                calculatorInput = new NumericInput(Double.parseDouble(textInput));
            } else if(expressionInputMap.containsKey(textInput)){
                calculatorInput = expressionInputMap.get(textInput);
            } else {
                continue;
            }

            // Add * after ) if input is not an operator
            // Ex: (5+3)2 -> (5+3)*2
            if(!(calculatorInput instanceof Operator)) {
                if(!calculatorInputs.isEmpty() && calculatorInputs.get(calculatorInputs.size()-1) instanceof RightParenInput) {
                    calculatorInputs.add(new MultiplicationOperator());
                }
            }
            calculatorInputs = calculatorInput.addToCalculatorExpression(calculatorInputs);
        }
        return calculatorInputs;
    }


}

package com.mobiquity.androidunittests.ui.presenter;

import com.mobiquity.androidunittests.calculator.Calculator;
import com.mobiquity.androidunittests.calculator.CalculatorConstants;
import com.mobiquity.androidunittests.calculator.ExpressionConverter;
import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.ui.mvpview.CalculatorView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class CalculatorPresenter extends Presenter<CalculatorView> {

    private Calculator calculator;
    private ExpressionConverter expressionConverter;
    private List<String> expression;

    public CalculatorPresenter(Calculator calculator, ExpressionConverter inputsConverter) {
        this.calculator = calculator;
        this.expressionConverter = inputsConverter;
        expression = new ArrayList<>();
    }

    @Override
    public void bind(CalculatorView view) {
        super.bind(view);
        view().updateDisplayText(getDisplayString());
        evaluate(false);
    }

    public void handleCalculatorButtonPress(String displayText) {
        expression.add(displayText);
        expression = expressionConverter.normalize(expression);
        view().updateDisplayText(getDisplayString());
        evaluate(false);
    }

    public void handleDelete() {
        if (!expression.isEmpty()) {
            expression.remove(expression.size() - 1);
            view().updateDisplayText(getDisplayString());
            evaluate(false);
        }
    }

    public boolean handleClear() {
        expression.clear();
        view().showResult("");
        evaluate(false);
        return true;
    }

    public void handleEvaluate() {
        evaluate(true);
    }

    private void evaluate(boolean isResult) {
        DecimalFormat decimalFormat = CalculatorConstants.DECIMAL_FORMAT;
        try {
            List<Input> inputs = expressionConverter.convert(expression);
            double result = calculator.evaluate(inputs.toArray(new Input[inputs.size()]));
            if (isResult) {
                expression = new ArrayList<>(Arrays.asList(decimalFormat.format(result).split("")));
                expression.remove("");
                view().showResult(decimalFormat.format(result));
            } else {
                if (inputs.size() > 1) {
                    view().showPassiveCalculation(decimalFormat.format(result));
                }
            }
        } catch (Calculator.CalculatorEvaluationException e) {
            Timber.e(e, "%s: %s", e.getMessage(), expression);
            if (isResult) {
                view().showResultError();
            }
        }
    }

    private String getDisplayString() {
        String displayString = "";
        for (String item : expression) {
            displayString += item;
        }
        return displayString;
    }

}

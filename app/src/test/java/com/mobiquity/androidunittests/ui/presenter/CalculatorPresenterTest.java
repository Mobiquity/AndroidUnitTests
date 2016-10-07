package com.mobiquity.androidunittests.ui.presenter;

import com.mobiquity.androidunittests.calculator.Calculator;
import com.mobiquity.androidunittests.calculator.ExpressionConverter;
import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.NumericInput;
import com.mobiquity.androidunittests.calculator.input.operator.AdditionOperator;
import com.mobiquity.androidunittests.testutil.ReflectionUtil;
import com.mobiquity.androidunittests.ui.mvpview.CalculatorView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.mobiquity.androidunittests.testutil.InputSubject.input;
import static com.mobiquity.androidunittests.testutil.MockitoInvocationHelper.onlyLastInvocation;

public class CalculatorPresenterTest {

    @Mock
    Calculator calculator;
    @Mock
    ExpressionConverter expressionConverter;
    @Mock
    CalculatorView mockView;

    private CalculatorPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new CalculatorPresenter(calculator, expressionConverter);
    }

    @Test
    public void testHandleNumber_UpdatesDisplay() {
        configureExpressionConverterNormalization();
        presenter.bind(mockView);
        presenter.handleCalculatorButtonPress("3");

        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq("3"));
    }

    @Test
    public void testHandleOperator_UpdatesDisplay() {
        configureExpressionConverterNormalization();
        presenter.bind(mockView);
        presenter.handleCalculatorButtonPress("-");

        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq("-"));
    }

    @Test
    public void testHandleSymbol_UpdatesDisplay() {
        configureExpressionConverterNormalization();
        presenter.bind(mockView);
        presenter.handleCalculatorButtonPress("(");

        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq("("));
    }

    @Test
    public void testHandleDelete_UpdatesDisplay() {
        configureExpressionConverterNormalization();

        presenter.bind(mockView);
        presenter.handleCalculatorButtonPress("3");
        presenter.handleCalculatorButtonPress("4");
        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq("34"));

        presenter.handleDelete();
        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq("3"));
    }

    @Test
    public void testHandleClear_UpdatesDisplay() {
        configureExpressionConverterNormalization();

        presenter.bind(mockView);
        presenter.handleCalculatorButtonPress("3");
        presenter.handleCalculatorButtonPress("4");
        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq("34"));

        presenter.handleClear();
        Mockito.verify(mockView, onlyLastInvocation()).updateDisplayText(Mockito.eq(""));
    }

    @Test
    public void testHandleEvaluate_GivesCorrectInputToCalculator() throws Exception {
        // Mock that the user has entered 3 + 4 already in the calculator
        Mockito.when(expressionConverter.convert(Mockito.anyList())).thenReturn(
                Arrays.asList(
                        new NumericInput(3),
                        new AdditionOperator(),
                        new NumericInput(4)
                )
        );

        presenter.bind(mockView);
        presenter.handleEvaluate();
        ArgumentCaptor<Input[]> argumentCaptor = ArgumentCaptor.forClass(Input[].class);
        Mockito.verify(calculator, Mockito.atLeastOnce()).evaluate(argumentCaptor.capture());

        Input[] calculatorInput = argumentCaptor.getValue();
        assertThat(calculatorInput).hasLength(3);
        assertAbout(input()).that(calculatorInput[0]).valueEqualTo(new NumericInput(3));
        assertAbout(input()).that(calculatorInput[1]).valueEqualTo(new AdditionOperator());
        assertAbout(input()).that(calculatorInput[2]).valueEqualTo(new NumericInput(4));
    }

    @Test
    public void testHandleEvaluatePassive_ShowPassiveCalculation() throws Exception {
        // Have the calculator throw exception until a successful calculation
        Mockito.when(calculator.evaluate(Mockito.any())).thenThrow(
                new Calculator.CalculatorEvaluationException("Invalid Expression"));

        presenter.bind(mockView);
        Mockito.when(expressionConverter.convert(Mockito.any())).thenReturn(
                Arrays.asList(new NumericInput(3)));
        presenter.handleCalculatorButtonPress("3");

        Mockito.when(expressionConverter.convert(Mockito.any())).thenReturn(
                Arrays.asList(new NumericInput(3), new AdditionOperator()));
        presenter.handleCalculatorButtonPress("+");

        // Setup the calculator to return a valid result
        Mockito.reset(calculator);
        Mockito.when(calculator.evaluate(Mockito.any())).thenReturn(7.0);
        Mockito.when(expressionConverter.convert(Mockito.any())).thenReturn(
                Arrays.asList(new NumericInput(3), new AdditionOperator(), new NumericInput(4)));
        presenter.handleCalculatorButtonPress("4");

        Mockito.verify(mockView).showPassiveCalculation(Mockito.eq("7"));
    }

    @Test
    public void testHandleEvaluateExplicit_ShowsResult() throws Exception {
        presenter.bind(mockView);
        Mockito.when(calculator.evaluate(Mockito.any())).thenReturn(100.0);
        presenter.handleEvaluate();

        Mockito.verify(mockView).showResult(Mockito.eq("100"));
    }

    @Test
    public void testHandleEvaluateExplicit_UpdatesExpression() throws Exception {
        presenter.bind(mockView);
        Mockito.when(calculator.evaluate(Mockito.any())).thenReturn(100.0);
        presenter.handleEvaluate();

        List<String> expression = ReflectionUtil.getField(presenter, "expression");
        assertThat(expression).containsExactly("1", "0", "0").inOrder();
    }

    private void configureExpressionConverterNormalization() {
        Mockito.when(expressionConverter.normalize(Mockito.anyList())).thenAnswer(
                invocation -> invocation.getArguments()[0]
        );
    }

}
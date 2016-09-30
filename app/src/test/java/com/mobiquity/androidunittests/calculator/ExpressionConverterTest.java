package com.mobiquity.androidunittests.calculator;

import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.LeftParenInput;
import com.mobiquity.androidunittests.calculator.input.NumericInput;
import com.mobiquity.androidunittests.calculator.input.RightParenInput;
import com.mobiquity.androidunittests.calculator.input.operator.AdditionOperator;
import com.mobiquity.androidunittests.calculator.input.operator.ExponentOperator;
import com.mobiquity.androidunittests.calculator.input.operator.MultiplicationOperator;
import com.mobiquity.androidunittests.calculator.input.operator.SubtractionOperator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertAbout;
import static com.mobiquity.androidunittests.testutil.InputListSubject.inputList;

@RunWith(RobolectricTestRunner.class)
public class ExpressionConverterTest {

    private ExpressionConverter expressionConverter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        expressionConverter = new ExpressionConverter(RuntimeEnvironment.application);
    }

    @Test
    public void testConvert_SingleDigitNumber() {
        List<String> normalizedInput = Arrays.asList("3");
        List<Input> expectedConvertedInput = Arrays.asList(new NumericInput(3));
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_MultipleDigitsNumber() {
        List<String> normalizedInput = Arrays.asList("5", "4", "3", "2");
        List<Input> expectedConvertedInput = Arrays.asList(new NumericInput(5432));
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_NegativeNumber() {
        List<String> normalizedInput = Arrays.asList("-", "3");
        List<Input> expectedConvertedInput = Arrays.asList(new NumericInput(-3));
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_AdditionOperation() {
        List<String> normalizedInput = Arrays.asList("3", "+", "4");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(3),
                new AdditionOperator(),
                new NumericInput(4)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_SubtractOperation() {
        List<String> normalizedInput = Arrays.asList("3", "-", "4");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(3),
                new SubtractionOperator(),
                new NumericInput(4)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_MultiplyOperation() {
        List<String> normalizedInput = Arrays.asList("3", "*", "4");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(3),
                new MultiplicationOperator(),
                new NumericInput(4)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_ExponentOperation() {
        List<String> normalizedInput = Arrays.asList("3", "^", "4");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(3),
                new ExponentOperator(),
                new NumericInput(4)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_MultiplyNegativeNumberOperation() {
        List<String> normalizedInput = Arrays.asList("3", "*", "-", "4");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(3),
                new MultiplicationOperator(),
                new NumericInput(-4)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_InsertMultiplyAfterParens() {
        List<String> normalizedInput = Arrays.asList("(", "3", "-", "4" , ")", "5");
        List<Input> expectedConvertedInput = Arrays.asList(
                new LeftParenInput(),
                new NumericInput(3),
                new SubtractionOperator(),
                new NumericInput(4),
                new RightParenInput(),
                new MultiplicationOperator(),
                new NumericInput(5)
        );

        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_InsertMultiplyBeforeParens() {
        List<String> normalizedInput = Arrays.asList("5", "(", "-", "3", "-", "4" , ")");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(5),
                new MultiplicationOperator(),
                new LeftParenInput(),
                new NumericInput(-3),
                new SubtractionOperator(),
                new NumericInput(4),
                new RightParenInput()
        );

        List<Input> convertedInput = expressionConverter.convert(normalizedInput);

        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_DecimalNumber_LeadingNumber() {
        List<String> normalizedInput = Arrays.asList("3", ".", "5");
        List<Input> expectedConvertedInput = Arrays.asList(
            new NumericInput(3.5)
        );

        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_DecimalNumber_LeadingNumber_Negative() {
        List<String> normalizedInput = Arrays.asList("-", "3", ".", "5");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(-3.5)
        );

        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_DecimalNumber_NoLeadingNumber() {
        List<String> normalizedInput = Arrays.asList(".", "5");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(0.5)
        );

        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_DecimalNumber_NoLeadingNumber_Negative() {
        List<String> normalizedInput = Arrays.asList("-", ".", "5");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(-0.5)
        );

        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_ExpressionWithUnknownInputItem() {
        List<String> normalizedInput = Arrays.asList("3", "+", "invalid input");
        List<Input> expectedConvertedInput = Arrays.asList(
                new NumericInput(3),
                new AdditionOperator()
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvert_SubtractionExpressionBetweenParens() {
        List<String> normalizedInput = Arrays.asList("(", "3", "-", "6", ")", "*", "-", "3");
        List<Input> expectedConvertedInput = Arrays.asList(
                new LeftParenInput(),
                new NumericInput(3),
                new SubtractionOperator(),
                new NumericInput(6),
                new RightParenInput(),
                new MultiplicationOperator(),
                new NumericInput(-3)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

    @Test
    public void testConvertSubtractionAfterParens() {
        List<String> normalizedInput = Arrays.asList("(", "3", "+", "4", ")", "-", "7");
        List<Input> expectedConvertedInput = Arrays.asList(
                new LeftParenInput(),
                new NumericInput(3),
                new AdditionOperator(),
                new NumericInput(4),
                new RightParenInput(),
                new SubtractionOperator(),
                new NumericInput(7)
        );
        List<Input> convertedInput = expressionConverter.convert(normalizedInput);
        assertAbout(inputList()).that(convertedInput).containsExactlyInputValuesIn(expectedConvertedInput);
    }

}
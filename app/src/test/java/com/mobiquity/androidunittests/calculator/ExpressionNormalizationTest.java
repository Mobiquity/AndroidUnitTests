package com.mobiquity.androidunittests.calculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ExpressionNormalizationTest {


    private ExpressionConverter expressionConverter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        expressionConverter = new ExpressionConverter(RuntimeEnvironment.application);
    }

    @Test
    public void testNormalize_numbers() {
        List<String> originalInput = Arrays.asList("3", "4", "5");
        List<String> expectedNormalizedInput = Arrays.asList("3", "4", "5");

        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowLeadingAdditionOperator() {
        List<String> originalInput = Arrays.asList("+", "5");
        List<String> expectedNormalizedInput = Arrays.asList("5");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowMultipleAdditionOperator() {
        List<String> originalInput = Arrays.asList("5", "+", "+");
        List<String> expectedNormalizedInput = Arrays.asList("5", "+");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowLeadingMultiplicationOperator() {
        List<String> originalInput = Arrays.asList("*", "5");
        List<String> expectedNormalizedInput = Arrays.asList("5");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }



    @Test
    public void testNormalize_DontAllowMultipleOperators() {
        List<String> originalInput = Arrays.asList("5", "+", "+", "*");
        List<String> expectedNormalizedInput = Arrays.asList("5", "*");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowMinusMinus() {
        List<String> originalInput = Arrays.asList("5", "-", "-", "3");
        List<String> expectedNormalizedInput = Arrays.asList("5", "-", "3");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowPlusMinus() {
        List<String> originalInput = Arrays.asList("5", "+", "-", "3");
        List<String> expectedNormalizedInput = Arrays.asList("5", "-", "3");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_AllowMultiplyMinus() {
        List<String> originalInput = Arrays.asList("5", "*", "-", "3");
        List<String> expectedNormalizedInput = Arrays.asList("5", "*", "-", "3");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowLeftParenPlus() {
        List<String> originalInput = Arrays.asList("(", "+");
        List<String> expectedNormalizedInput = Arrays.asList("(");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowLeftParenTimes() {
        List<String> originalInput = Arrays.asList("(", "*");
        List<String> expectedNormalizedInput = Arrays.asList("(");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowLeftParenExponent() {
        List<String> originalInput = Arrays.asList("(", "^");
        List<String> expectedNormalizedInput = Arrays.asList("(");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

    @Test
    public void testNormalize_DontAllowMultipleDecimalPointsPerNumber() {
        List<String> originalInput = Arrays.asList("3", ".", ".");
        List<String> expectedNormalizedInput = Arrays.asList("3",".");
        List<String> normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);

        originalInput = Arrays.asList(".", "3", ".");
        expectedNormalizedInput = Arrays.asList(".","3");
        normalizedInput = expressionConverter.normalize(originalInput);
        assertThat(normalizedInput).containsExactlyElementsIn(expectedNormalizedInput);
    }

}

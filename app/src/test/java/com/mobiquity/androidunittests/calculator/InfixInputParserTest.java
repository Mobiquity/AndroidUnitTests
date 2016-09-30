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

import java.util.Queue;

import static com.google.common.truth.Truth.assertThat;

public class InfixInputParserTest {


    private InfixInputParser infixInputParser;

    private NumericInput three = new NumericInput(3);
    private NumericInput four = new NumericInput(4);
    private NumericInput five = new NumericInput(5);

    private AdditionOperator plus = new AdditionOperator();
    private SubtractionOperator minus= new SubtractionOperator();
    private MultiplicationOperator times = new MultiplicationOperator();
    private ExponentOperator exp = new ExponentOperator();

    private LeftParenInput leftParen = new LeftParenInput();
    private RightParenInput rightParen = new RightParenInput();

    @Before
    public void setUp() {
        infixInputParser = new InfixInputParser();
    }

    /**
     * Before: 3 + 4
     * After: 3 4 +
     */
    @Test
    public void testToPostfix_SimpleAdd() {
        Input[] inputs = new Input[]{
                three,
                plus,
                four
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, plus).inOrder();
    }

    /**
     * Before: 3 + 4 + 5
     * After: 3 4 + 5 +
     */
    @Test
    public void testToPostfix_MultipleAdd() {
        Input[] inputs = new Input[] {
                three,
                plus,
                four,
                plus,
                five
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, plus, five, plus)
            .inOrder();
    }

    /**
     * Before: 3 - 4
     * After: 3 4 -
     */
    @Test
    public void testToPostfix_SimpleSubtract() {
        Input[] inputs = new Input[] {
                three,
                minus,
                four
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, minus)
                .inOrder();
    }

    /**
     * Before: 3 - 4 - 5
     * After: 3 4 - 5 -
     */
    @Test
    public void testToPostfix_MultipleSubtract() {
        Input[] inputs = new Input[] {
                three,
                minus,
                four,
                minus,
                five
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, minus, five, minus)
                .inOrder();
    }

    /**
     * Before: 3 + 4 - 5
     * After: 3 4 + 5 -
     */
    @Test
    public void testToPostfix_AdditionSubtraction() {

        Input[] inputs = new Input[] {
                three,
                plus,
                four,
                minus,
                five
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, plus, five, minus)
                .inOrder();
    }

    /**
     * Before: 3 * 4
     * After: 3 4 *
     */
    @Test
    public void testToPostfix_SimpleMultiply() {
        Input[] inputs = new Input[] {
                three,
                times,
                four
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, times)
                .inOrder();
    }

    /**
     * Before: 3 ^ 4
     * After: 3 4 ^
     */
    @Test
    public void testToPostfix_SimpleExponent() {
        Input[] inputs = new Input[] {
                three,
                exp,
                four
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, exp)
                .inOrder();
    }

    /**
     * Before: 3 * 4 + 5
     * After: 3 4 * 5 +
     */
    @Test
    public void testToPostfix_MultiplicationAddition() {
        Input[] inputs = new Input[] {
                three,
                times,
                four,
                plus,
                five
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, times, five, plus)
                .inOrder();
    }

    /**
     * Before: 3 + 4 * 5
     * After: 3 4 5 * +
     */
    @Test
    public void testToPostfix_AdditionMultiplication() {
        Input[] inputs = new Input[] {
                three,
                plus,
                four,
                times,
                five
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, five, times, plus)
                .inOrder();
    }


    /**
     * Before: 3 + (4 + 5)
     * After: 3 4 5 + +
     */
    @Test
    public void testToPostfix_Addition_ParenAddition() {
        Input[] inputs = new Input[] {
                three,
                plus,
                leftParen,
                four,
                plus,
                five,
                rightParen
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, five, plus, plus).inOrder();
    }

    /**
     * Before: (3 + 4) * 5
     * After: 3 4 + 5 *
     */
    @Test
    public void testToPostfix_ParenAddition_Multiplication() {
        Input[] inputs = new Input[] {
                leftParen,
                three,
                plus,
                four,
                rightParen,
                times,
                five
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, plus, five, times).inOrder();
    }

    /**
     * Before: 3 * (4 + 5)
     * After: 3 4 5 + *
     */
    @Test
    public void testToPostfix_Multiplication_ParenAddition() {
        Input[] inputs = new Input[] {
                three,
                times,
                leftParen,
                four,
                plus,
                five,
                rightParen
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, five, plus, times).inOrder();
    }

    /**
     * Before: 3 * (4 ^ 5)
     * After: 3 4 5 ^ *
     */
    @Test
    public void testToPostfix_Multiplication_ParenExponent() {
        Input[] inputs = new Input[] {
                three,
                times,
                leftParen,
                four,
                exp,
                five,
                rightParen
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
        assertThat(postfixOutput).containsExactly(three, four, five, exp, times).inOrder();
    }

    /**
     * Before: 3 )
     * After: ?
     */
    @Test(expected = InfixInputParser.InputParserException.class)
    public void testToPostfix_Invalid_ExtraClosingParen() {
        Input[] inputs = new Input[] {
                three,
                rightParen
        };

        Queue<Input> postfixOutput = infixInputParser.toPostfix(inputs);
    }

}
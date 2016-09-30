package com.mobiquity.androidunittests.calculator.input.operator;

import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.InputType;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.mobiquity.androidunittests.testutil.InputSubject.input;

public class ExponentOperatorTest {

    private ExponentOperator exponentOperator;

    @Before
    public void setUp() {
        exponentOperator = new ExponentOperator();
    }


    @Test
    public void testEquals() {
        ExponentOperator operator1 = new ExponentOperator();
        ExponentOperator operator2 = new ExponentOperator();
        assertAbout(input()).that(operator1).valueEqualTo(operator1);
        assertAbout(input()).that(operator1).valueEqualTo(operator2);
    }

    @Test
    public void testNotEquals() {
        ExponentOperator operator1 = new ExponentOperator();
        assertAbout(input()).that(operator1).valueNotEqualTo(null);
        assertAbout(input()).that(operator1).valueNotEqualTo(new Input(InputType.DEFAULT){});
    }

    @Test
    public void testExponentOperator_ShouldExtendOperator() {
        assertThat(exponentOperator).isInstanceOf(Operator.class);
    }

    @Test
    public void testGetType_IsAnOperator() {
        assertThat(exponentOperator.getType()).isEqualTo(InputType.OPERATOR);
    }
    @Test
    public void testGetPrecedence_IsEqualToExponentPrecedence() {
        assertThat(exponentOperator.getPrecedence())
                .isEqualTo(Operator.Precedence.POWER_PRECENDENCE.getValue());
    }

    @Test
    public void testShouldBeLeftAssociative() {
        assertThat(exponentOperator.isLeftAssociative()).isTrue();
    }

    @Test
    public void testExecute_ShouldExponentOperands() {
        assertThat(exponentOperator.execute(2,3)).isWithin(8);
        assertThat(exponentOperator.execute(10,0)).isWithin(1);
        assertThat(exponentOperator.execute(10,-2)).isWithin(.01);
        assertThat(exponentOperator.execute(-2,-3)).isWithin(-.125);
    }

}
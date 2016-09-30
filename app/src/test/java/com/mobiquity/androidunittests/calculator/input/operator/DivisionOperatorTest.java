package com.mobiquity.androidunittests.calculator.input.operator;

import com.mobiquity.androidunittests.calculator.input.Input;
import com.mobiquity.androidunittests.calculator.input.InputType;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.mobiquity.androidunittests.testutil.InputSubject.input;


public class DivisionOperatorTest {

    private DivisionOperator divisionOperator;

    @Before
    public void setUp() {
        divisionOperator = new DivisionOperator();
    }


    @Test
    public void testEquals() {
        DivisionOperator operator1 = new DivisionOperator();
        DivisionOperator operator2 = new DivisionOperator();
        assertAbout(input()).that(operator1).valueEqualTo(operator1);
        assertAbout(input()).that(operator1).valueEqualTo(operator2);
    }

    @Test
    public void testNotEquals() {
        DivisionOperator operator1 = new DivisionOperator();
        assertAbout(input()).that(operator1).valueNotEqualTo(null);
        assertAbout(input()).that(operator1).valueNotEqualTo(new Input(InputType.DEFAULT){});
    }

    @Test
    public void testDivisionOperator_ShouldExtendOperator() {
        assertThat(divisionOperator).isInstanceOf(Operator.class);
    }

    @Test
    public void testGetType_IsAnOperator() {
        assertThat(divisionOperator.getType()).isEqualTo(InputType.OPERATOR);
    }
    @Test
    public void testGetPrecedence_IsEqualToMultiplicationPrecedence() {
        assertThat(divisionOperator.getPrecedence())
                .isEqualTo(Operator.Precedence.MULTIPLICATION_PRECEDENCE.getValue());
    }

    @Test
    public void testShouldBeLeftAssociative() {
        assertThat(divisionOperator.isLeftAssociative()).isTrue();
    }

    @Test
    public void testExecute_ShouldMultiplyOperands() {
        assertThat(divisionOperator.execute(6,3)).isWithin(2);
        assertThat(divisionOperator.execute(0,10)).isWithin(0);
        assertThat(divisionOperator.execute(10,-10)).isWithin(-1);
        assertThat(divisionOperator.execute(-20,-2)).isWithin(10);
    }

}
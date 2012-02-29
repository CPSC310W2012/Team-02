package tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

//import calc.Calculator;

@RunWith(Parameterized.class)

public class ParameterizedTest {
/*
	private static Calculator calculator = new Calculator();

	private int param;
	private int param2;
	
	private int result;

	@Parameters
	public static List parameters() {
		//changed 48 to 49
		return Arrays.asList(new Object[][] { { 0, 0, 0 }, { 1, 1, 1 }, { 2, 4, 8},
		{ 4, 16, 64}, { 5, 25, 125}, { 6, 36, 216}, { 7, 49, 343} });
	}

	public ParameterizedTest(int param, int result, int param2) {
		this.param2 = param2;
		this.param = param;
		this.result = result;
	}
	
	@Test
	public void square() {
		calculator.square(param);
		assertEquals(result, calculator.getResult());
	}
	
	@Test
	public void multiply() {
		calculator.reset();
		calculator.add(param);
		calculator.multiply(result);
		assertEquals(param2, calculator.getResult());
	}
*/
}

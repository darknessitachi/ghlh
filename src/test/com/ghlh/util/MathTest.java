package com.ghlh.util;

import org.junit.Test;

import com.ghlh.util.MathUtil;


public class MathTest {

	@Test
	public void testGetNSquareM() {
		try {
			assert (MathUtil.getNSquareM(2, 0) == 1);
			assert (MathUtil.getNSquareM(2, 1) == 2);
			assert (MathUtil.getNSquareM(2, 2) == 4);
			assert (MathUtil.getNSquareM(2, 3) == 8);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}

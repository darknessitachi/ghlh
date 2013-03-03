package com.ghlh.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ghlh.util.GUIUtil;

public class GUIUtilTest {

	@Test
	public void testGetScreenSize() {
		String size = GUIUtil.getScreenSize();
		assertEquals(size, "1024*768");
	}

}

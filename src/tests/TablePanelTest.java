package tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import dominoes.*;

/**
 * @author Ben Griffiths and Matt Wildman
 * MSc Computer Science 
 * 31/03/2013
 * 
 */
public class TablePanelTest {

	TablePanel t;
	int maxPips = 6;
	Bone bone;
	Bone doubleBone;
	Method addBoneIconMethod;
	Method calculateHeightMethod;
	@SuppressWarnings("rawtypes")
	Class[] paramsAddBone;
	@SuppressWarnings("rawtypes")
	Class[] paramsCalculateHeight;
	
	@Before
	public void setUp() {
		t = new TablePanel(maxPips);
		bone = new Bone(0, 1);
		doubleBone = new Bone(0, 0);
		
		paramsAddBone = new Class[5];
		paramsAddBone[0] = Bone.class;
		paramsAddBone[1] = int.class;
		paramsAddBone[2] = int.class;
		paramsAddBone[3] = int.class;
		paramsAddBone[4] = int.class;
		
		paramsCalculateHeight = new Class[1];
		paramsCalculateHeight[0] = int.class;
		
		try {
			addBoneIconMethod = t.getClass().getDeclaredMethod("addBoneIcon", paramsAddBone);
			calculateHeightMethod = t.getClass().getDeclaredMethod("calculateMaxTableHeight", paramsCalculateHeight);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		addBoneIconMethod.setAccessible(true);
		calculateHeightMethod.setAccessible(true);
	}
	
	@Test
	public void maxHeightTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object[] params = new Object[1];
		params[0] = maxPips;
		int expectedHeight = 500;
		int result = 0;
		result = (int)calculateHeightMethod.invoke(t, params);
		assertEquals("Max height incorrect", expectedHeight, result);
		
		maxPips = 9;
		params[0] = maxPips;
		result = 0;
		expectedHeight = 950;
		result = (int)calculateHeightMethod.invoke(t, params);
		
		maxPips = 4;
		params[0] = maxPips;
		result = 0;
		expectedHeight = 350;
		result = (int)calculateHeightMethod.invoke(t, params);
		assertEquals("Max height incorrect", expectedHeight, result);
	}
	
	@Test
	public void leftRegularBone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 200;
		int startY = 200;
		int startRow = 0;
		t.setLx(startX);
		t.setLy(startY);
		t.setLrow(startRow);
		int x = t.getLx();
		int y = t.getLy();
		int row = t.getLrow();
		int expectedX = startX - (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int expectedY = startY;
		int expectedRow = 0;
		Object[] params = {bone, x, y, row, Play.LEFT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", expectedX, t.getLx());
		assertEquals("y value is incorrect", expectedY, t.getLy());
		assertEquals("Row value is incorrect", expectedRow, t.getLrow());
	}
	
	@Test
	public void rightRegularBone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 200;
		int startY = 200;
		int startRow = 1;
		t.setRx(startX);
		t.setRy(startY);
		t.setRrow(startRow);
		int x = t.getRx();
		int y = t.getRy();
		int row = t.getRrow();
		int expectedX = startX + (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int expectedY = startY;
		int expectedRow = 1;
		Object[] params = {bone, x, y, row, Play.RIGHT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", expectedX, t.getRx());
		assertEquals("y value is incorrect", expectedY, t.getRy());
		assertEquals("Row value is incorrect", expectedRow, t.getRrow());
	}
	
	@Test
	public void leftDoubleBone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 200;
		int startY = 200;
		int startRow = 0;
		t.setLx(startX);
		t.setLy(startY);
		t.setLrow(startRow);
		int x = t.getLx();
		int y = t.getLy();
		int row = t.getLrow();
		int nextX = startX - (TablePanel.getHALF_BONE() + TablePanel.getE_PAD());
		int nextY = startY;
		int expectedRow = 0;
		Object[] params = {doubleBone, x, y, row, Play.LEFT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getLx());
		assertEquals("y value is incorrect", nextY, t.getLy());
		assertEquals("Row value is incorrect", expectedRow, t.getLrow());
	}
	
	@Test
	public void rightDoubleBone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 200;
		int startY = 200;
		int startRow = 1;
		t.setRx(startX);
		t.setRy(startY);
		t.setRrow(startRow);
		int x = t.getRx();
		int y = t.getRy();
		int row = t.getRrow();
		int nextX = startX + (TablePanel.getHALF_BONE() + TablePanel.getE_PAD());
		int nextY = startY;
		int expectedRow = 1;
		Object[] params = {doubleBone, x, y, row, Play.RIGHT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getRx());
		assertEquals("y value is incorrect", nextY, t.getRy());
		assertEquals("Row value is incorrect", expectedRow, t.getRrow());
	}
	
	@Test
	public void leftInEndzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = TablePanel.getMAX_WIDTH() + 1;
		int startY = 200;
		int startRow = 1;
		assertTrue("x not in endzone", t.isInEndZone(startX));
		t.setLx(startX);
		t.setLy(startY);
		t.setLrow(startRow);
		int x = t.getLx();
		int y = t.getLy();
		int row = t.getLrow();
		int nextX = startX - (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY - (TablePanel.getWHOLE_BONE() + TablePanel.getHALF_BONE() + TablePanel.getE_PAD() * 2);
		int expectedRow = 2;
		Object[] params = {bone, x, y, row, Play.LEFT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getLx());
		assertEquals("y value is incorrect", nextY, t.getLy());
		assertEquals("Row value is incorrect", expectedRow, t.getLrow());
		assertFalse("x still in endzone", t.isInEndZone(t.getLx()));
	}
	
	@Test
	public void rightInEndzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = TablePanel.getMAX_WIDTH() + 1;
		int startY = 200;
		int startRow = 1;
		assertTrue("x not in endzone", t.isInEndZone(startX));
		t.setRx(startX);
		t.setRy(startY);
		t.setRrow(startRow);
		int x = t.getRx();
		int y = t.getRy();
		int row = t.getRrow();
		int nextX = startX - (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY + (TablePanel.getWHOLE_BONE() + TablePanel.getHALF_BONE() + TablePanel.getE_PAD() * 2);
		int expectedRow = 2;
		Object[] params = {bone, x, y, row, Play.RIGHT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getRx());
		assertEquals("y value is incorrect", nextY, t.getRy());
		assertEquals("Row value is incorrect", expectedRow, t.getRrow());
		assertFalse("x still in endzone", t.isInEndZone(t.getRx()));
	}
	
	@Test
	public void leftInStartzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 5;
		int startY = 200;
		int startRow = 0;
		assertTrue("x not in start zone", t.isInStartZone(startX));
		t.setLx(startX);
		t.setLy(startY);
		t.setLrow(startRow);
		int x = t.getLx();
		int y = t.getLy();
		int row = t.getLrow();
		int nextX = startX + (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY - (TablePanel.getWHOLE_BONE() + TablePanel.getHALF_BONE() + TablePanel.getE_PAD() * 2);
		int expectedRow = 1;
		Object[] params = {bone, x, y, row, Play.LEFT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getLx());
		assertEquals("y value is incorrect", nextY, t.getLy());
		assertEquals("Row value is incorrect", expectedRow, t.getLrow());
		assertFalse("x still in endzone", t.isInEndZone(t.getLx()));
	}
	
	@Test
	public void rightInStartzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 5;
		int startY = 200;
		int startRow = 2;
		assertTrue("x not in start zone", t.isInStartZone(startX));
		t.setRx(startX);
		t.setRy(startY);
		t.setRrow(startRow);
		int x = t.getRx();
		int y = t.getRy();
		int row = t.getRrow();
		int nextX = startX + (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY + (TablePanel.getWHOLE_BONE() + TablePanel.getHALF_BONE() + TablePanel.getE_PAD() * 2);
		int expectedRow = 3;
		Object[] params = {bone, x, y, row, Play.RIGHT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getRx());
		assertEquals("y value is incorrect", nextY, t.getRy());
		assertEquals("Row value is incorrect", expectedRow, t.getRrow());
		assertFalse("x still in endzone", t.isInEndZone(t.getRx()));
	}
	
	@Test
	public void leftDoubleNearStartzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 30;
		int startY = 200;
		int startRow = 0;
		assertFalse("x erroneously in start zone", t.isInStartZone(startX));
		t.setLx(startX);
		t.setLy(startY);
		t.setLrow(startRow);
		int x = t.getLx();
		int y = t.getLy();
		int row = t.getLrow();
		int nextX = startX - (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY;
		int expectedRow = 0;
		Object[] params = {doubleBone, x, y, row, Play.LEFT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getLx());
		assertEquals("y value is incorrect", nextY, t.getLy());
		assertEquals("Row value is incorrect", expectedRow, t.getLrow());
		assertTrue("x not in start zone", t.isInStartZone(t.getLx()));
	}
	
	@Test
	public void rightDoubleNearStartzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = 30;
		int startY = 200;
		int startRow = 2;
		assertFalse("x erroneously in start zone", t.isInStartZone(startX));
		t.setRx(startX);
		t.setRy(startY);
		t.setRrow(startRow);
		int x = t.getRx();
		int y = t.getRy();
		int row = t.getRrow();
		int nextX = startX - (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY;
		int expectedRow = 2;
		Object[] params = {doubleBone, x, y, row, Play.RIGHT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getRx());
		assertEquals("y value is incorrect", nextY, t.getRy());
		assertEquals("Row value is incorrect", expectedRow, t.getRrow());
		assertTrue("x not in start zone", t.isInStartZone(t.getRx()));
	}
	
	@Test
	public void leftDoubleNearEndzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = TablePanel.getMAX_WIDTH() - 30;
		int startY = 200;
		int startRow = 1;
		assertFalse("x erroneously in endzone", t.isInEndZone(startX));
		t.setLx(startX);
		t.setLy(startY);
		t.setLrow(startRow);
		int x = t.getLx();
		int y = t.getLy();
		int row = t.getLrow();
		int nextX = startX + (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY;
		int expectedRow = 1;
		Object[] params = {doubleBone, x, y, row, Play.LEFT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getLx());
		assertEquals("y value is incorrect", nextY, t.getLy());
		assertEquals("Row value is incorrect", expectedRow, t.getLrow());
		assertTrue("x not in endzone", t.isInEndZone(t.getLx()));
	}
	
	@Test
	public void rightDoubleNearEndzone() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int startX = TablePanel.getMAX_WIDTH() - 30;
		int startY = 200;
		int startRow = 1;
		assertFalse("x erroneously in endzone", t.isInEndZone(startX));
		t.setRx(startX);
		t.setRy(startY);
		t.setRrow(startRow);
		int x = t.getRx();
		int y = t.getRy();
		int row = t.getRrow();
		int nextX = startX + (TablePanel.getWHOLE_BONE() + TablePanel.getE_PAD());
		int nextY = startY;
		int expectedRow = 1;
		Object[] params = {doubleBone, x, y, row, Play.RIGHT};
		addBoneIconMethod.invoke(t, params);
		assertEquals("x value is incorrect", nextX, t.getRx());
		assertEquals("y value is incorrect", nextY, t.getRy());
		assertEquals("Row value is incorrect", expectedRow, t.getRrow());
		assertTrue("x not in endzone", t.isInEndZone(t.getRx()));
	}

}

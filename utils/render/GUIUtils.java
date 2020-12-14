package elixe.utils.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class GUIUtils {
	public static void pre2D() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1);
	}

	public static void post2D() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawGradient(int paramXStart, int paramYStart, int paramXEnd, int paramYEnd, float[] colorStart,
			float[] colorEnd) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(colorStart[0], colorStart[1], colorStart[2], colorStart[3]);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glColor4f(colorEnd[0], colorEnd[1], colorEnd[2], colorEnd[3]);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glEnd();
	}

	public static void drawGradient90(int paramXStart, int paramYStart, int paramXEnd, int paramYEnd,
			float[] colorStart, float[] colorEnd) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(colorStart[0], colorStart[1], colorStart[2], colorStart[3]);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glColor4f(colorEnd[0], colorEnd[1], colorEnd[2], colorEnd[3]);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glEnd();
	}

	public static void drawRainbow(int paramXStart, int paramYStart, int paramXEnd, int paramYEnd) {
		float colorsRainbow[][] = { { 1f, 0f, 0f }, { 1f, 1f, 0f }, { 0f, 1f, 0f }, { 0f, 1f, 1f }, { 0f, 0f, 1f },
				{ 1f, 0f, 1f }, { 1f, 0f, 0f } };
		double ySpacing = (paramYEnd - paramYStart) / 6d;
		GL11.glBegin(GL11.GL_QUADS);
		for (int i = 0; i < 6; i++) {
			int fixedIndex = i + 1;
			float[] bounds = { (float) (paramYStart + ySpacing * i), (float) (paramYStart + ySpacing * fixedIndex) };
			GL11.glColor4f(colorsRainbow[i][0], colorsRainbow[i][1], colorsRainbow[i][2], 1);
			GL11.glVertex2d(paramXEnd, bounds[0]);
			GL11.glVertex2d(paramXStart, bounds[0]);
			GL11.glColor4f(colorsRainbow[fixedIndex][0], colorsRainbow[fixedIndex][1], colorsRainbow[fixedIndex][2], 1);
			GL11.glVertex2d(paramXStart, bounds[1]);
			GL11.glVertex2d(paramXEnd, bounds[1]);
		}
		GL11.glEnd();

	}

	// multi color
	public static void drawPolygon(double[][] points, float red, float green, float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_POLYGON);

		for (double[] point : points) {
			GL11.glVertex2d(point[0], point[1]);
		}
		GL11.glEnd();
	}

	// one color
	public static void drawPolygon(double[][] points, float c, float alpha) {
		drawPolygon(points, c, c, c, alpha);
	}

	// multi color
	public static void drawRect(int paramXStart, int paramYStart, int paramXEnd, int paramYEnd, float red, float green,
			float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glEnd();
	}

	// one color
	public static void drawRect(int paramXStart, int paramYStart, int paramXEnd, int paramYEnd, float c, float alpha) {
		drawRect(paramXStart, paramYStart, paramXEnd, paramYEnd, c, c, c, alpha);
	}

	// get points of polygon
	public static double[][] getRoundedRect(int paramXStart, int paramYStart, int paramXEnd, int paramYEnd,
			int radius) {
		ArrayList<double[]> rectPointsArray = new ArrayList<double[]>();

		int x1 = paramXStart + radius;
		int y1 = paramYStart + radius;
		int x2 = paramXEnd - radius;
		int y2 = paramYEnd - radius;

		float degree = (float) (Math.PI / 180);
		for (int i = 0; i <= 90; i++) {
			double x = x2 + Math.sin(i * degree) * radius;
			if ((x1 + Math.sin((360 - i) * degree) * radius) <= x) {
				double[] points = { x, y2 + Math.cos(i * degree) * radius };
				rectPointsArray.add(points);
			}
		}
		for (int i = 90; i <= 180; i++) {
			double x = x2 + Math.sin(i * degree) * radius;
			if ((x1 + Math.sin((270 - (i - 90)) * degree) * radius) <= x) {
				double[] points = { x, y1 + Math.cos(i * degree) * radius };
				rectPointsArray.add(points);
			}
		}
		for (int i = 180; i <= 270; i++) {
			double x = x1 + Math.sin(i * degree) * radius;
			if ((x2 + Math.sin((i - 180) * degree) * radius) >= x) {
				double[] points = { x, y1 + Math.cos(i * degree) * radius };
				rectPointsArray.add(points);
			}

		}
		for (int i = 270; i <= 360; i++) {
			double x = x1 + Math.sin(i * degree) * radius;
			if ((x2 + Math.sin((90 + (i - 270)) * degree) * radius) >= x) {
				double[] points = { x, y2 + Math.cos(i * degree) * radius };
				rectPointsArray.add(points);
			}
		}

		double[][] rectPoints = new double[rectPointsArray.size()][];

		for (int i = 0; i < rectPointsArray.size(); i++) {
			rectPoints[i] = rectPointsArray.get(i).clone();
		}

		return rectPoints;
	}

	// combobox polygons
	// 0 = first, 1 = mid, 2 = last
	public static double[][] getItemRect(int type, int paramXStart, int paramYStart, int paramXEnd, int paramYEnd,
			int radius) {
		ArrayList<double[]> rectPointsArray = new ArrayList<double[]>();

		int x1 = paramXStart + radius;
		int y1 = paramYStart + radius;
		int x2 = paramXEnd - radius;
		int y2 = paramYEnd - radius;

		float degree = (float) (Math.PI / 180);

		switch (type) {
		case 0:
			rectPointsArray.add(new double[] { paramXEnd, paramYEnd });

			for (int i = 90; i <= 180; i++) {
				double x = x2 + Math.sin(i * degree) * radius;
				if ((x1 + Math.sin((270 - (i - 90)) * degree) * radius) <= x) {
					double[] points = { x, y1 + Math.cos(i * degree) * radius };
					rectPointsArray.add(points);
				}
			}
			for (int i = 180; i <= 270; i++) {
				double x = x1 + Math.sin(i * degree) * radius;
				if ((x2 + Math.sin((i - 180) * degree) * radius) >= x) {
					double[] points = { x, y1 + Math.cos(i * degree) * radius };
					rectPointsArray.add(points);
				}
			}

			rectPointsArray.add(new double[] { paramXStart, paramYEnd });
			break;
		case 1:

			rectPointsArray.add(new double[] { paramXStart, paramYStart });
			rectPointsArray.add(new double[] { paramXStart, paramYEnd });
			rectPointsArray.add(new double[] { paramXEnd, paramYEnd });
			rectPointsArray.add(new double[] { paramXEnd, paramYStart });

			break;
		case 2:
			rectPointsArray.add(new double[] { paramXStart, paramYStart });

			for (int i = 270; i <= 360; i++) {
				double x = x1 + Math.sin(i * degree) * radius;
				double[] points = { x, y2 + Math.cos(i * degree) * radius };
				rectPointsArray.add(points);
			}

			for (int i = 0; i <= 90; i++) {
				double x = x2 + Math.sin(i * degree) * radius;
				double[] points = { x, y2 + Math.cos(i * degree) * radius };
				rectPointsArray.add(points);
			}

			rectPointsArray.add(new double[] { paramXEnd, paramYStart });
			break;

		default:
			break;
		}

		double[][] rectPoints = new double[rectPointsArray.size()][];

		for (int i = 0; i < rectPointsArray.size(); i++) {
			rectPoints[i] = rectPointsArray.get(i).clone();
		}

		return rectPoints;
	}

	// get points of polygon
	public static double[][] getCircle(int paramX, int paramY, int radius) {
		ArrayList<double[]> circlePointsArray = new ArrayList<double[]>();

		float degree = (float) (Math.PI / 180);
		for (int i = 0; i <= 360; i++) {
			double[] points = { paramX + Math.sin(i * degree) * radius, paramY + Math.cos(i * degree) * radius };
			circlePointsArray.add(points);
		}

		double[][] circlePoints = new double[circlePointsArray.size()][];

		for (int i = 0; i < circlePointsArray.size(); i++) {
			circlePoints[i] = circlePointsArray.get(i).clone();
		}

		return circlePoints;
	}

}

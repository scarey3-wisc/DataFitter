import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DataFitter{
	public static String coordinates;
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.white);
		frame.add(panel);
		LinkedList<int[]> points = new LinkedList<int[]>();
		PointAdder p = new PointAdder(points, panel);
		panel.addMouseListener(p);
		panel.addMouseMotionListener(p);
		panel.updateUI();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BufferedImage paint = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = paint.getGraphics();
		while(true){
			g.setColor(Color.white);
			g.fillRect(0, 0, paint.getWidth(), paint.getHeight());
			g.setColor(Color.black);
			Equation[] nova = new Equation[points.size()];
			for(int i = 0; i < points.size(); i++){
				int x = points.get(i)[0];
				int y = points.get(i)[1];
				double[] face = new double[points.size() + 1];
				for(int j = 0; j < points.size(); j++){
					face[j] = Math.pow(x, j * 1);
				}
				face[face.length - 1] = y;
				nova[i] = new Equation(face);
			}
			double[] terms = Matrix.solveMatrix(nova);
			String equation = "f(x) = ";
			for(int i = terms.length - 1; i >= 0; i--){
				equation += trimDecimals((terms[i]* 1), 3) + "x^" + i;
				if(i != 0){
					equation += " + ";
				}
			}
			frame.setTitle(equation);
			for(int x = panel.getWidth()/-2; x < panel.getWidth()/2; x++){
				double y = 0;
				for(int i = 0; i < terms.length; i++){
					y += terms[i] * Math.pow(x, i * 1);
				}
				int otherX = x - 1;
				double otherY = 0;
				for(int i = 0; i < terms.length; i++){
					otherY += terms[i] * Math.pow(otherX, i * 1);
				}
				g.drawLine(x + panel.getWidth()/2, (int) (y + panel.getHeight()/2), otherX + panel.getWidth()/2, (int) (otherY + panel.getHeight()/2));
			}
			g.drawLine(0, panel.getHeight()/2, panel.getWidth(), panel.getHeight()/2);
			g.drawLine(panel.getWidth()/2, 0, panel.getWidth()/2, panel.getHeight());
			if(coordinates != null){
				int height = g.getFontMetrics().getHeight();
				g.drawString(coordinates, 0, height);
			}
			g.setColor(Color.red);
			for(int[] bob: points){
				g.fillOval(bob[0] + panel.getWidth()/2 - 5, bob[1] + panel.getHeight()/2 - 5, 10, 10);
			}
			panel.getGraphics().drawImage(paint, 0, 0, null);
		}
	}
	public static double trimDecimals(double d, int remaining){
		boolean skip = true;
		if(skip){
			return d;
		}
		int temp = (int) (d * Math.pow(10, remaining));
		return temp / Math.pow(10, remaining);
	}
	public static class PointAdder implements MouseListener, MouseMotionListener{
		LinkedList<int[]> ps;
		JPanel panel; 
		int[] selected;
		public PointAdder(LinkedList<int[]> points, JPanel pan){
			ps = points;
			panel = pan;
			selected = null;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {
			if(arg0.getButton() == 3){
				int foundIndex = -1;
				for(int[] bob: ps){
					if(Math.abs(bob[0] + panel.getWidth()/2 - arg0.getX()) < 6 && Math.abs(bob[1] + panel.getHeight()/2 - arg0.getY()) < 6){
						foundIndex = ps.indexOf(bob);
					}
				}
				if(foundIndex != -1){
					ps.remove(foundIndex);
				}else{
					ps.add(new int[]{arg0.getX() - panel.getWidth()/2, arg0.getY() - panel.getHeight()/2});
				}
			}else if(arg0.getButton() == 1){
				int foundIndex = -1;
				for(int[] bob: ps){
					if(Math.abs(bob[0] + panel.getWidth()/2 - arg0.getX()) < 6 && Math.abs(bob[1] + panel.getHeight()/2 - arg0.getY()) < 6){
						foundIndex = ps.indexOf(bob);
					}
				}
				if(foundIndex == -1){
					selected = null;
				}else{
					selected = ps.get(foundIndex);
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		@Override
		public void mouseDragged(MouseEvent arg0) {
			if(selected != null){
				selected[0] = arg0.getX() - panel.getWidth()/2;
				selected[1] = arg0.getY() - panel.getHeight()/2;
			}
			coordinates = (arg0.getX() - panel.getWidth()/2) + ", " + -1 * (arg0.getY() - panel.getHeight()/2);
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {
			coordinates = (arg0.getX() - panel.getWidth()/2) + ", " + -1 * (arg0.getY() - panel.getHeight()/2);
		}	
	}
}
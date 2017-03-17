//package signaturesdk.acquisition;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.util.LinkedList;
//import javax.swing.BorderFactory;
//import javax.swing.JPanel;
//
//import signaturesdk.beans.AcquisitionSignWord;
//import signaturesdk.beans.PairList;
//import signaturesdk.beans.SignWord;
//import signaturesdk.beans.SignatureData;
//import signaturesdk.features.utils.Copier;
//import jpen.PLevel;
//import jpen.PLevelEvent;
//import jpen.PenManager;
//import jpen.event.PenAdapter;
//import jpen.owner.multiAwt.AwtPenToolkit;
//
//
///**
// * Drawable JPanel
// * @author Luciano Giuseppe - April 2013
// * 
// *
// */
//public class SignatureJPanel extends JPanel {
//
//	private static final long serialVersionUID = 1L;
//
//	//ZOOM to apply on normalized signature when draw it
//	
//	private boolean clear;
//	private LinkedList<Double> xr, yr, press;
//	private LinkedList<Long> tr;
//	private LinkedList<AcquisitionSignWord> signature = new LinkedList<AcquisitionSignWord>();
//	private LinkedList<SignWord> normalizedSignature;
//	//private double minX, maxX, minY, maxY; commented to optimize velocity acquisition
//	private AcquisitionSignWord word;
//	private boolean showCriticalPoints;
//	private PenAdapter penAdp;
//
//	private boolean quality = false;
//
//	public SignatureJPanel() {
//		super();
//		this.setDoubleBuffered(false);
//
//		this.clear = true;
//		this.setBackground(Color.WHITE);
//		this.setBorder(BorderFactory.createLineBorder(Color.black));
//		//Dimension d = new Dimension(750, 200);
//		Dimension d = new Dimension(350, 200);
//		this.setPreferredSize(d);
//		this.setSize(d);
//	}
//
//	public void setListeners() {
//				
//		// setup the mouse to cause a pressure level event when the left button is pressed:
//		PenManager pm=AwtPenToolkit.getPenManager();
//		pm.pen.levelEmulator.setPressureTriggerForLeftCursorButton(0.5f);
//		
//		this.penAdp = new MyPenAdp();
//		AwtPenToolkit.addPenListener(this, this.penAdp);
//	}
//
//	
//	@Override
//	public synchronized void paintComponent(Graphics g) {
//		// draw the background
//		super.paintComponent(g);
//		if(clear) {
//			clear = false;
//			return;
//		}
//		
//		Graphics2D g2D = (Graphics2D)g;
//		// draw all words
//		if(this.normalizedSignature == null) {
//			drawSignature(g2D);
//		} else {
//			drawNormalizedSignature(g2D);
//		}
//
//	}
//	
//	/**
//	 * Render the input points
//	 * @param g2D
//	 */
//	private void drawSignature(Graphics2D g2D) {
//		
//		goodRenderingQuality(g2D);
//		
//		for(AcquisitionSignWord w : signature) {
//			LinkedList<Double> x = w.getX();
//			LinkedList<Double> y = w.getY();
//			
//			//draw by line
//			for(int i = 0; i < (x.size()-1); i++) {
//				g2D.drawLine(x.get(i).intValue(), y.get(i).intValue(), x.get(i+1).intValue(), y.get(i+1).intValue());			
//			}
//			
//			//draw by point
//			/*for(int i = 0; i < x.size(); i++) {
//				g2D.drawOval(x.get(i).intValue(), y.get(i).intValue(), 1, 1);				
//			}*/
//		}
//	}
//
//	/**
//	 * Set a good rendering quality
//	 * @param g2D
//	 */
//	private void goodRenderingQuality(Graphics2D g2D) {
//		if( this.quality ) {
//			g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
//			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		}
//	}
//	
//	/**
//	 * Draw imported project
//	 * @param g2D
//	 */
//	private void drawNormalizedSignature(Graphics2D g2D) {
//		
//		goodRenderingQuality(g2D);
//		
//		for(SignWord w : normalizedSignature) {
//			LinkedList<Double> x = w.getX();
//			LinkedList<Double> y = w.getY();
//			
//			//draw by line
//			int first = 0;
//			for(int last : w.getPartsSize()) {
//				for(int i = first; i < (last-1); i++)
//					g2D.drawLine(x.get(i).intValue(), y.get(i).intValue(), x.get(i+1).intValue(), y.get(i+1).intValue());
//				first = last;
//			}
//			
//			//draw by point
//			/*for(int i = 0; i < x.size(); i++) {
//				g2D.drawOval(x.get(i).intValue(), y.get(i).intValue(), 1, 1);				
//			}*/
//			
//			if(showCriticalPoints) {
//				 PairList pc = w.getCriticalPoints();
//				 x = pc.getValues1();
//				 y = pc.getValues2();
//				 for(int i = 0; i < x.size(); i++)
//				 g2D.drawOval(x.get(i).intValue()-4, y.get(i).intValue()-4, 10, 10);	
//			 
//			} 
//		}
//	}
//
// 	
//
//	/**
//	 * Clear the Panel
//	 */
//	public void clear() {
//		this.clear = true;
//		signature.clear();
//		this.repaint();
//	}
//	
//	/**
//	 * Disable the JPanel
//	 */
//	public void setDisabled() {
//		this.setEnabled(false);
//		AwtPenToolkit.removePenListener(this, this.penAdp);
//	}
//
//	/**
//	 * Return the signature acquired
//	 * @return
//	 */
//	public SignatureData getSignature() {
//		return new SignatureData(Copier.acquisitionSignature(signature));
//	}
//
//	/**
//	 * Draw a normalized signature
//	 * @param signature
//	 */
//	public void drawNormalizedSignature(LinkedList<SignWord> signature) {
//		this.makeNormSignDrawable(signature);
//		this.repaint();
//	}
//	
//	/**
//	 * Draw the original signature, not normalized
//	 * @param signature
//	 */
//	public void drawSignature(LinkedList<AcquisitionSignWord> signature) {
//		this.signature = signature;
//		this.repaint();
//	}
//	
//	public void showCriticalPoints() {	
//		this.showCriticalPoints = true;
//	}
//	
//	
//	/**
//	 * Make drawable the normalized signature
//	 */
//	private void makeNormSignDrawable(LinkedList<SignWord> normSignature) {
//		if(normSignature == null) {
//			this.normalizedSignature = null;
//			return;
//		}
//		
//		//to show the signature drawable
//		this.normalizedSignature = new LinkedList<SignWord>();
//		
//		for(SignWord sw: normSignature) {
//			SignWord aSign = new SignWord();
//			aSign.setPartsSize(Copier.intList(sw.getPartsSize()));
//			
//			LinkedList<Double> x = sw.getX(), newX = aSign.getX();
//			LinkedList<Double> y = sw.getY(), newY = aSign.getY();
//			
//			double minX = Math.abs(min(x));
//			double minY = Math.abs(min(y));
//			
//			int zoom = 100;
//			
//			//the entire signWord
//			for(int i = 0; i < x.size(); i++) {
//				double xx = ((x.get(i)+minX)*zoom)+10;
//				double yy = ((y.get(i)+minY)*zoom)+10;
//				newX.add(xx);
//				newY.add(yy);
//			}
//			
//			//the critical point
//			x = sw.getCriticalPoints().getValues1();
//			newX = aSign.getCriticalPoints().getValues1();
//			
//			y = sw.getCriticalPoints().getValues2();
//			newY = aSign.getCriticalPoints().getValues2();
//			
//			for(int i = 0; i < x.size(); i++) {
//				newX.add(((x.get(i)+minX)*zoom)+10);
//				newY.add(((y.get(i)+minY)*zoom)+10);
//			}
//			
//			this.normalizedSignature.add(aSign);
//		}
//	}
//	
//	private double min(LinkedList<Double> l) {
//		double min = l.getFirst();
//		for(Double d: l) {
//			if(d < min) min = d;
//		}
//		return min;
//	}
//	
//	public void hightQuality(boolean enable) {
//		this.setDoubleBuffered(true);
//		this.quality = enable;
//	}
//	
//	/**
//	 * Pen Adapter to get pen (or mouse) strokes
//	 *
//	 */
//	private class MyPenAdp extends PenAdapter {
//		private double prevPressure = 0;
//		
//		@Override
//		public void penLevelEvent(PLevelEvent ev) {
//			double currPressure;
//
//			if(!ev.isMovement())
//				return;
//			
//			try {
//				currPressure = ev.pen.getLevelValue(PLevel.Type.PRESSURE);
//				
//				if(currPressure != 0) {
//					
//					//Pen on tablet
//					if(prevPressure == 0) {
//						word = new AcquisitionSignWord();
//						xr = word.getX();
//						yr = word.getY();
//						press = word.getPressure();
//						tr = word.getTime();
//						signature.add(word);
//					}
//					tr.add(System.nanoTime()); 
//					xr.add((double) ev.pen.getLevelValue(PLevel.Type.X));
//					yr.add((double) ev.pen.getLevelValue(PLevel.Type.Y));
//					press.add(currPressure);
//				}
//				prevPressure = currPressure;
//			} catch(Exception e) {
//				e.printStackTrace();
//			} finally {
//				repaint();
//			}
//		}
//	}
//}
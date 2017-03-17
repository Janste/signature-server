//package main;
//
//
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.Vector;
//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.ListSelectionModel;
//import javax.swing.filechooser.FileFilter;
//
//import signaturesdk.acquisition.SignatureJPanel;
//import signaturesdk.beans.AcquisitionSignWord;
//import signaturesdk.beans.SignWord;
//import signaturesdk.beans.SignatureData;
//import signaturesdk.features.Extract;
//import signaturesdk.features.Normalize;
//import signaturesdk.features.Sample;
//import signaturesdk.features.utils.GMath;
//import signaturesdk.hash.Directional;
//import signaturesdk.storage.CSV;
//import signaturesdk.storage.Disk;
//import signaturesdk.verification.Verification;
//
//public class Finestra extends JFrame {
//
//	private static final long serialVersionUID = 1L;
//
//	private SignatureJPanel inputSign, testSign;
//	private JButton bClear, bElaborate;
//	private JList<String> outputList;
//	private Vector<String> msgOutput = new Vector<String>();
//	private SignatureJPanel jpNormSign;
//
//	private SignatureJPanel jpNormSignTest;
//	
//	//private static Logger logger = Logger.getGlobal();
//	
//	public Finestra() {
//		super("Test the signature!");
//		this.setLayout(new GridLayout(3,1));
//
//		/*this.setSize(new Dimension(700, 600));
//		this.setPreferredSize(new Dimension(700, 600));*/
//		
//		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
//			JLabel label = new JLabel("Original Signature");
//			p.add(label);
//			
//			this.inputSign = new SignatureJPanel();
//			this.inputSign.setListeners();
//			p.add(this.inputSign);
//			
//			JButton bS1Clear = new JButton("Clear");
//			bS1Clear.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					inputSign.clear();
//					jpNormSign.clear();
//				}
//			});
//			p.add(bS1Clear);
//
//			this.jpNormSign = new SignatureJPanel();
//			this.jpNormSign.hightQuality(true);
//			p.add(this.jpNormSign);
//
//		this.add(p);
//		
//		p = new JPanel(new FlowLayout());
//			label = new JLabel("Signature to test");
//			p.add(label);
//			this.testSign = new SignatureJPanel();
//			this.testSign.setListeners();
//			p.add(this.testSign);
//			
//			bS1Clear = new JButton("Clear");
//			bS1Clear.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					testSign.clear();
//					jpNormSignTest.clear();
//				}
//			});
//			p.add(bS1Clear);
//		
//			
//			this.jpNormSignTest = new SignatureJPanel();
//			this.jpNormSignTest.hightQuality(true);
//			p.add(this.jpNormSignTest);
//
//		this.add(p);
//		
//		p = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		this.bClear = new JButton("Clear");
//		this.bClear.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				inputSign.clear();
//				testSign.clear();
//				jpNormSign.clear();
//				jpNormSignTest.clear();
//				msgOutput.clear();
//				outputList.setListData(msgOutput);
//			}
//		});
//		
//		p.add(this.bClear);
//		
//		JButton salvaFirma = new JButton("Save signature");
//		salvaFirma.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				LinkedList<AcquisitionSignWord> temp = inputSign.getSignature().getSignature();
//				if(temp.size() == 0) {
//					JOptionPane.showMessageDialog(null, "Draw something into original signature", 
//							"Warning", JOptionPane.WARNING_MESSAGE);
//					return;
//				}
//				SignatureData sd = new SignatureData(temp);
//				
//				JFileChooser fc = new JFileChooser();
//				fc.setFileFilter(new FileFilter() {
//					
//					@Override
//					public String getDescription() {
//						return "Signature file";
//					}
//					
//					@Override
//					public boolean accept(File f) {
//						return(f.isDirectory() || f.getName().endsWith(Disk.FILE_EXTENSION));
//					}
//				});
//				
//				int returnVal = fc.showSaveDialog(null);
//				if(returnVal == JFileChooser.APPROVE_OPTION) {
//					if(Disk.save( sd, fc.getSelectedFile().getAbsolutePath()) == null)
//						JOptionPane.showMessageDialog(null,"File saved");
//					else
//						JOptionPane.showMessageDialog(null,"File not saved");
//				}
//			}
//		});
//		
//		p.add(salvaFirma);
//		
//		JButton apriFirma = new JButton("Open signature");
//		apriFirma.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				JFileChooser fc = new JFileChooser();
//				fc.setFileFilter(new FileFilter() {
//					
//					@Override
//					public String getDescription() {
//						return "Signature file";
//					}
//					
//					@Override
//					public boolean accept(File f) {
//						return(f.isDirectory() || f.getName().endsWith(Disk.FILE_EXTENSION));
//					}
//				});
//				
//				int returnVal = fc.showDialog(null, "Open");
//				if(returnVal == JFileChooser.APPROVE_OPTION) {
//					SignatureData  opened = (SignatureData) Disk.open(fc.getSelectedFile().getAbsolutePath());
//					if(opened == null) 
//						JOptionPane.showMessageDialog(null,"Impossible open the file");
//					else 
//						inputSign.drawSignature(opened.getSignature());
//				}
//			}
//		});
//		
//		p.add(apriFirma);
//		
//		this.bElaborate = new JButton("Process");
//		this.bElaborate.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				
//				Vector<Double> 	ret;
//				
//				msgOutput.clear();
//				LinkedList<AcquisitionSignWord> sign1 = inputSign.getSignature().getSignature();
//				if(sign1.size() == 0) {
//					JOptionPane.showMessageDialog(null, "Draw something into original signature");
//					return;
//				}
//				
//				LinkedList<AcquisitionSignWord> sign2 = testSign.getSignature().getSignature();
//				if(sign2.size() == 0) {
//					JOptionPane.showMessageDialog(null, "Draw something into test signature");
//					return;
//				}
//				
//				
//				/*
//				 * Save the last signatures in case of exception
//				 * to reproduce  it.
//				 */
//				/*Disk.save(sign1, "lastSign1");
//				Disk.save(sign2, "lastSign2");*/
//				try {
//					CSV.exportRawSignature(sign1, "lastSign1.CSV");
//					CSV.exportRawSignature(sign2, "lastSign2.CSV");
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					System.out.println("Impossible exports signature to xml");
//					e1.printStackTrace();
//				}
//				
//				msgOutput.add("Normalize the size:");
//				LinkedList<SignWord> f1 = (new Normalize(sign1)).size();
//				LinkedList<SignWord> f2 = (new Normalize(sign2)).size();
//				sign1 = null;
//				sign2 = null;
//				
//				int npS1 = Extract.pointsNumber(f1);
//				int npS2 = Extract.pointsNumber(f2);
//				msgOutput.add("Points number:");
//				msgOutput.add("S1:" + npS1 + " - S2:" + npS2);
//				msgOutput.add(" ");
//				
//				msgOutput.add("Total time:");
//				msgOutput.add("S1:" + Extract.totalTime(f1) + " - S2:" + Extract.totalTime(f2));
//				msgOutput.add(" ");
//				
//				
//				msgOutput.add("Len");
//				int min = Math.min(f1.size(), f2.size());
//				for(int i = 0; i < min; i++) {
//					msgOutput.add("S1Len:"+ GMath.polyLen(f1.get(i).getX(), f1.get(i).getY()));
//					msgOutput.add("S2Len:"+ GMath.polyLen(f2.get(i).getX(), f2.get(i).getY()));
//					msgOutput.add(" ");
//				}
//		
//				
//				msgOutput.add("DTW coords");
//				ret = Verification.coordsDTW(f1, f2);
//				int i = 1;
//				for(double v : ret) {
//					msgOutput.add("Word " + i + ": " + v);
//					i++;
//				}
//				msgOutput.add(" ");
//				
//				
//				msgOutput.add("Dtw velocities");
//				try {
//					int sign1CP = Extract.calculateVelocities(f1);
//					int sign2CP = Extract.calculateVelocities(f2);
//					ret = Verification.velDTW(f1, f2);
//					i = 1;
//					for(double v : ret) {
//						msgOutput.add("Word " + i + ": " + v);
//						i++;
//					}
//					msgOutput.add(" ");
//				
//					msgOutput.add("Dtw on critical points");
//					msgOutput.add("Numero s1:"+ sign1CP+" s2:"+sign2CP);
//					ret = Verification.criticalPointsDTW(f1, f2);
//					i = 1;
//					for(double v : ret) {
//						msgOutput.add("Word " + i + ": " + v);
//						i++;
//					}
//					msgOutput.add(" ");
//				} catch(Exception e) {
//					msgOutput.add(e.getMessage());
//					e.printStackTrace();
//				}
//				
//				
//				Extract.angles(f1);
//				Extract.angles(f2);
//				msgOutput.add("Dtw on internal angles");
//				ret = Verification.internalAnglesDTW(f1, f2);
//				i = 1;
//				for(double v : ret) {
//					msgOutput.add("Word " + i + ": " + v);
//					i++;
//				}
//				msgOutput.add(" ");
//				
//				
//				msgOutput.add("Dtw on external angles");
//				ret = Verification.externalAnglesDTW(f1, f2);
//				i = 1;
//				for(double v : ret) {
//					msgOutput.add("Word " + i + ": " + v);
//					i++;
//				}
//				msgOutput.add(" ");			
//				
//				msgOutput.add("Dtw on pressure");
//				ret = signaturesdk.verification.Verification.pressionDTW(f1, f2);
//				i = 1;
//				for(double v : ret) {
//					msgOutput.add("Word " + i + ": " + v);
//					i++;
//				}
//				msgOutput.add(" ");
//				
//				Sample s = new Sample();
//				s.sample(f1, f2);
//				msgOutput.add("ER2 on normalized signatures");
//				ret = Verification.coordsER2(s.getSignature1(), s.getSignature2());
//				i = 1;
//				for(double v: ret) {
//					msgOutput.add("Word " + i + ": " + (v*100) + "%");
//					i++;
//				}
//				msgOutput.add(" ");
//				s = null;
//
//				Sample s1 = new Sample();
//				String h1 = (new Directional(s1.sample(f1, Sample.Method.STANDARD, false))).compute();
//				//s1 = null;
//				Sample s2 = new Sample();
//				String h2 = (new Directional(s2.sample(f2, Sample.Method.STANDARD, false)).compute());
//				//s2 = null;
//				
//				msgOutput.add("Hash");	
//				msgOutput.add("Sign1: " + h1);
//				msgOutput.add("Sign2: " + h2);
//				msgOutput.add(" ");
//				
//				f1 = s1.getSignature();
//				f2 = s2.getSignature();
//				jpNormSign.showCriticalPoints();
//				jpNormSign.drawNormalizedSignature(f1);
//				
//				jpNormSignTest.showCriticalPoints();
//				jpNormSignTest.drawNormalizedSignature(f2);				
//				
//				//show the output
//				outputList.setListData(msgOutput);
//			}
//		});
//		p.add(this.bElaborate);
//		
//		this.outputList = new JList<String>();
//		this.outputList.setVisibleRowCount(5);
//		this.outputList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//		this.outputList.setLayoutOrientation(JList.VERTICAL);
//		JScrollPane optionPane = new JScrollPane(this.outputList);
//		optionPane.setPreferredSize(new Dimension(360, 120));
//		p.add(optionPane);
//		this.add(p);
//		
//		this.pack();
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		this.setVisible(true);
//	}
//	
//}

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;





public class MainWindow implements ActionListener {
	private JFrame frame;
	private JButton btnAjouter;
	private JButton btnRetirer;
	private JButton btnResultats;
	private JSpinner spinnerX;
	private JSpinner spinnerY;
	private JComboBox comboBoxfichiercharger;
	private JComboBox comboBoxfonctionsDistance;
	private JList<String> listPointsInitiaux;
	DefaultListModel listModel;
	private JLabel lblResultats;
	
	private ArrayList<Point> centresInitiaux;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		centresInitiaux=new ArrayList<Point>();


		frame = new JFrame();
		frame.setBounds(100, 100, 400, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		String[] fichiercharger={"Exemple1","Exemple2"};
		comboBoxfichiercharger = new JComboBox(fichiercharger);
		comboBoxfichiercharger.setBounds(12, 39, 227, 22);
		frame.getContentPane().add(comboBoxfichiercharger);


		JLabel lblFichierCharger = new JLabel("Fichier \u00E0 charger");
		lblFichierCharger.setBounds(12, 10, 146, 16);
		frame.getContentPane().add(lblFichierCharger);

		JLabel lblFonctionDistance = new JLabel("Fonction distance");
		lblFonctionDistance.setBounds(12, 74, 165, 16);
		frame.getContentPane().add(lblFonctionDistance);

		String[] fonctionsDistance={"Norme 1","Norme2"};
		comboBoxfonctionsDistance = new JComboBox(fonctionsDistance);
		comboBoxfonctionsDistance.setBounds(12, 103, 227, 22);
		frame.getContentPane().add(comboBoxfonctionsDistance);


		btnResultats = new JButton("Lancer l'algorithme");
		btnResultats.setBounds(40, 478, 160, 25);
		frame.getContentPane().add(btnResultats);
		btnResultats.addActionListener(this);

		spinnerX = new JSpinner();
		spinnerX.setBounds(29, 170, 60, 22);
		frame.getContentPane().add(spinnerX);

		JLabel lblCentresInitiaux = new JLabel("Centres initiaux");
		lblCentresInitiaux.setBounds(29, 138, 106, 16);
		frame.getContentPane().add(lblCentresInitiaux);

		JLabel lblX = new JLabel("x");
		lblX.setBounds(29, 152, 56, 16);
		frame.getContentPane().add(lblX);

		btnAjouter = new JButton("Ajouter");
		btnAjouter.setBounds(205, 148, 97, 25);
		frame.getContentPane().add(btnAjouter);
		btnAjouter.addActionListener(this);


		btnRetirer = new JButton("Retirer");
		btnRetirer.setBounds(205, 182, 97, 25);
		frame.getContentPane().add(btnRetirer);

		btnRetirer.addActionListener(this);

		spinnerY = new JSpinner();
		spinnerY.setBounds(101, 170, 56, 22);
		frame.getContentPane().add(spinnerY);

		JLabel lblY = new JLabel("y");
		lblY.setBounds(102, 152, 56, 16);
		frame.getContentPane().add(lblY);

		listModel=new DefaultListModel<String>();
		listPointsInitiaux = new JList(listModel);
		listPointsInitiaux.setVisibleRowCount(10);
		listPointsInitiaux.setBounds(43, 220, 182, 245);
		frame.getContentPane().add(listPointsInitiaux);
		
		lblResultats = new JLabel("");
		lblResultats.setBounds(29, 516, 338, 26);
		frame.getContentPane().add(lblResultats);

		

	}


	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(btnAjouter)){
			int x=(Integer)spinnerX.getValue();
			int y=(Integer)spinnerY.getValue();
			Point point=search(x,y,centresInitiaux);
			if(point==null)centresInitiaux.add(new Point(x,y));
			printPointsInitiaux();
		}

		if(event.getSource().equals(btnRetirer)){
			int x=(Integer)spinnerX.getValue();
			int y=(Integer)spinnerY.getValue();
			Point point=search(x,y,centresInitiaux);
			if(point!=null)centresInitiaux.remove(point);
			printPointsInitiaux();
		}

		if(event.getSource().equals(btnResultats)){
		
			if(centresInitiaux.size()>1){
				lblResultats.setText("Resultats sauvés dans TP_ANREC.png");
				int fichierChargé=comboBoxfichiercharger.getSelectedIndex();
				int fonctionDistance=comboBoxfonctionsDistance.getSelectedIndex();
				//TODO Algortihme K-Means
				
				//Création du graphe 2D sous forme de .png
				saveScatterPlot();
			}else{
				lblResultats.setText("L'algorithme démarre avec au moins 2 points initiaux!");
			}
		}
	}

	public void saveScatterPlot(){
		//titre et légende
		Plot plot = Plot.plot(Plot.plotOpts().
		        title("Algorithme K-means").
		        legend(Plot.LegendFormat.BOTTOM)).
		    xAxis("x", Plot.axisOpts()).
		    yAxis("y", Plot.axisOpts());
		
		 //Création des données: centre initiaux
		    plot.series("Points initiaux",createSeriesData(centresInitiaux) ,
		        Plot.seriesOpts().
		            marker(Plot.Marker.DIAMOND).
		            markerColor(Color.GREEN).line(Plot.Line.NONE));
		  //Création des données des points finaux
		
		try {
			plot.save("TP_ANREC", "png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Plot.Data createSeriesData(ArrayList<Point> points){
		Plot.Data data=Plot.data();
		for (Point point : points) {
			data.xy(point.x, point.y);
		}
		return data;
	}
	
	public Point search(int x,int y, ArrayList<Point> list) {
		for (Point point : list) {
			if (point.x==x&&point.y==y) {
				return point;
			}
		}
		return null;
	}

	private void printPointsInitiaux(){
		listModel.clear();
		for(int i=0;i<centresInitiaux.size();i++){
			listModel.addElement(centresInitiaux.get(i).x+","+centresInitiaux.get(i).y);
		}
	}
}



package vista;


import javax.swing.*;

import controlador.ConexionBaseDatosJDBC;
import controlador.ConexionConBaseDeDatos;
import controlador.Controlador;
import controlador.ControladorResponsables;
import controlador.ControladorResponsablesExamen;
import controlador.ControladorSede;
import modelo.Alumno;
import modelo.Aula;
import modelo.Materia;
import modelo.Responsable;
import modelo.ResponsableExamen;
import modelo.Sede;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VistaResponsablesExamen extends JFrame implements ActionListener{
	private JButton bCargarDatos, bBorrarDatos;
	private JButton bAlumnos;
	private Controlador controlador;
	private ControladorResponsablesExamen controladorResponsablesExamen = new ControladorResponsablesExamen();
	private ConexionConBaseDeDatos conexionBD = ConexionBaseDatosJDBC.getInstance();
	private JList<String> listaResponsables = new JList<String>();
	private JList<String> listaResponsables2 = new JList<String>();

    private DefaultListModel listModel = new DefaultListModel();
	private DefaultListModel listModel2 = new DefaultListModel();
	JButton bQuitar= new JButton("Quitar");
	private JButton bInstitutos;
	private JButton bGenerar,bAulas,bResponsablesExamen;
	private JPanel panel;
	private Panel menu;
	//private JButton bAlumnos;
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JPanel panel_1;
	private Panel menu_2;
	private JButton bHome;
	private JButton bVicerrector;
	private JButton bGestorSede;
	JButton bAnadir;
	private JPanel panel_2;
	private Panel menu_1 = new Panel();
	//private JButton bAlumnos;
	private JButton bAsignaturas;
	private JButton bExamenes;
	//private JButton bInstitutos;
	private JButton bResponsablesSedes;
	private JButton bSedes;
	private JComboBox comboBoxAsignaturas = new JComboBox<>();
	private JComboBox comboBoxAulas = new JComboBox<>();
	private int index=-1;
	private int index2=-1;
	java.util.List<String> lista = new ArrayList<String>();
	java.util.List<String> lista2 = new ArrayList<String>();;	
	java.util.List<Aula> listaAulas = new ArrayList<Aula>();
	java.util.List<Materia> listaMaterias = new ArrayList<Materia>();
	private JList<String> listaResponsablesExamenPorAnadir;
	private JList<String> listaResponsablesExamenAnadidos;


	String cargo=null;
	String nombreResponsableExamen=null;
	String nombreExamen=null;
	String nombreAsingnado=null;


	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public VistaResponsablesExamen() {
		refresh();
		refresh2();
		comboBoxAulas.setSelectedIndex(-1);
		
		listaMaterias = conexionBD.listaMaterias();
		comboBoxAsignaturas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel2.clear();
				nombreExamen = (String) comboBoxAsignaturas.getSelectedItem();
				System.out.println(nombreExamen);
				refresh();
				refresh2();
				scrollPane_1.repaint();
			}
		});
		comboBoxAsignaturas.addItem("");
		for(Materia m : listaMaterias) {
			if(((DefaultComboBoxModel)comboBoxAsignaturas.getModel()).getIndexOf(m.getIdMateria())==-1) {
				comboBoxAsignaturas.addItem(m.getIdMateria());
				refresh2();
				
			}
		}

		comboBoxAsignaturas.setSelectedIndex(0);
			
		
		initialize();
	}

	private void refresh() {
		lista.clear();
		lista = conexionBD.listaResponsablesExamenPorAnadir();
		listModel.clear();
		//listModel = new DefaultListModel();
		for(String r : lista) {
			listModel.addElement(r);
	    }		
		listaResponsablesExamenPorAnadir = new JList<String>(listModel);

		
	}
	private void refresh2() {
		if(!lista2.isEmpty() ) {
			lista2.clear();
		}

		lista2 = conexionBD.listaResponsablesExamenAnadidosAExamen(nombreExamen);
		listModel2.clear();
		//listModel = new DefaultListModel();

		System.out.println("listaResponsablesExamenAnadidosAExamen");

		for(String r : lista2) {
			listModel2.addElement(r + " - " + controladorResponsablesExamen.conseguirCargoResponsableExamen(r, nombreExamen));
			System.out.println(r);
	    }		
		listaResponsablesExamenAnadidos = new JList<String>(listModel2);
		//scrollPane_1.repaint();

		
	}
	
	private void initialize() {
		setSize(960, 540);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("PEVAU");
		
		JPanel maingrid = new JPanel();
		getContentPane().add(maingrid, BorderLayout.CENTER);
		GridBagLayout gbl_maingrid = new GridBagLayout();
		gbl_maingrid.columnWidths = new int[]{0, 0, 164, -21, 0, 0};
		gbl_maingrid.rowHeights = new int[]{49, 14, 64, 30, 30, 110, 40, 120, 31, 0, 97, 0};
		gbl_maingrid.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_maingrid.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		maingrid.setLayout(gbl_maingrid);
		
		Panel menu = new Panel();
		menu.setFont(new Font("Dialog", Font.PLAIN, 14));
		menu.setBackground(new Color(0, 64, 128));
		GridBagConstraints gbc_menu = new GridBagConstraints();
		gbc_menu.fill = GridBagConstraints.BOTH;
		gbc_menu.gridwidth = 5;
		gbc_menu.insets = new Insets(0, 0, 5, 0);
		gbc_menu.gridx = 0;
		gbc_menu.gridy = 0;
		maingrid.add(menu, gbc_menu);
		
		bHome = new JButton("Home");
		menu.add(bHome);
		bHome.addActionListener(this) ;
		
		bVicerrector = new JButton("VICERRECTOR");
		bVicerrector.addActionListener(this) ;
		menu.add(bVicerrector);
		
		bGestorSede = new JButton("GESTOR SEDE");
		menu.add(bGestorSede);
		bGestorSede.addActionListener(this) ;
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBorder(new TitledBorder(null, "GESTOR SEDE", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1_1 = new GridBagConstraints();
		gbc_panel_1_1.gridheight = 2;
		gbc_panel_1_1.gridwidth = 5;
		gbc_panel_1_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1_1.gridx = 0;
		gbc_panel_1_1.gridy = 1;
		maingrid.add(panel_1_1, gbc_panel_1_1);
		GridBagLayout gbl_panel_1_1 = new GridBagLayout();
		gbl_panel_1_1.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1_1.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1_1.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_1_1.setLayout(gbl_panel_1_1);
		
		Panel menu_1_1 = new Panel();
		menu_1_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		menu_1_1.setBackground(new Color(0, 64, 128));
		GridBagConstraints gbc_menu_1_1 = new GridBagConstraints();
		gbc_menu_1_1.gridheight = 2;
		gbc_menu_1_1.fill = GridBagConstraints.BOTH;
		gbc_menu_1_1.gridwidth = 4;
		gbc_menu_1_1.gridx = 0;
		gbc_menu_1_1.gridy = 0;
		panel_1_1.add(menu_1_1, gbc_menu_1_1);
		
		bAulas = new JButton("Aulas");
		menu_1_1.add(bAulas);
		bAulas.addActionListener(this);
		
		 bExamenes = new JButton("Examenes");
		 menu_1_1.add(bExamenes);
		bExamenes.addActionListener(this);
		
		bResponsablesExamen = new JButton("Responsables Examen");
		menu_1_1.add(bResponsablesExamen);
		bResponsablesExamen.addActionListener(this);
		
		
		
		JLabel lblResponsables = new JLabel("Responsables por asignar");
		lblResponsables.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblResponsables = new GridBagConstraints();
		gbc_lblResponsables.insets = new Insets(0, 0, 5, 5);
		gbc_lblResponsables.gridx = 1;
		gbc_lblResponsables.gridy = 3;
		maingrid.add(lblResponsables, gbc_lblResponsables);
		
		JLabel lblNewLabel = new JLabel("Aulario Severo Ochoa");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 3;
		maingrid.add(lblNewLabel, gbc_lblNewLabel);
		
		//JComboBox comboBoxAsignaturas = new JComboBox();
		//comboBoxAsignaturas.setModel(new DefaultComboBoxModel());
		GridBagConstraints gbc_comboBoxAsignaturas = new GridBagConstraints();
		gbc_comboBoxAsignaturas.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxAsignaturas.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxAsignaturas.gridx = 4;
		gbc_comboBoxAsignaturas.gridy = 3;
		maingrid.add(comboBoxAsignaturas, gbc_comboBoxAsignaturas);
		
		//scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		maingrid.add(scrollPane, gbc_scrollPane);
		
		//List listNombre = new List();
		listaResponsables2 = new JList<String>(listModel2);
		listaResponsables2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					try {
						String[] cadena;
						try {
							cadena = ((String) listaResponsables2.getSelectedValue()).split(" -");
							nombreAsingnado = cadena[0];
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							System.err.println("Error");
							e1.printStackTrace();
						}
						
						System.out.println(nombreAsingnado);
						index2 = listaResponsables.getSelectedIndex();
						System.out.println(index2);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
			}
		});
		listaResponsables = new JList<String>(listModel);
		scrollPane.setViewportView(listaResponsables);
		listaResponsables.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					try {
						String[] cadena;
						try {
							cadena = ((String) listaResponsables.getSelectedValue()).split(" -");
							nombreResponsableExamen = cadena[0];
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							System.err.println("Error");
							e1.printStackTrace();
						}
						
						System.out.println(nombreResponsableExamen);
						index = listaResponsables.getSelectedIndex();
						System.out.println(index);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
			}
		});
		//JComboBox comboBoxAulas = new JComboBox();
		//comboBoxAulas.setModel(new DefaultComboBoxModel());
		GridBagConstraints gbc_comboBoxAulas = new GridBagConstraints();
		gbc_comboBoxAulas.gridwidth = 2;
		gbc_comboBoxAulas.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxAulas.gridx = 2;
		gbc_comboBoxAulas.gridy = 4;
		maingrid.add(comboBoxAulas, gbc_comboBoxAulas);
		
		JComboBox cbCargos = new JComboBox();
		
		cbCargos.setModel(new DefaultComboBoxModel(new String[] {"Vocal", "Vigilante"}));
		cbCargos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargo = (String) cbCargos.getSelectedItem();
				System.out.println(cargo);


			}
		});
		cbCargos.setSelectedIndex(-1);
		scrollPane_1 = new JScrollPane();
		
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridheight = 4;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 4;
		gbc_scrollPane_1.gridy = 4;
		maingrid.add(scrollPane_1, gbc_scrollPane_1);
		
		//JList listAulas = new JList();
		scrollPane_1.setViewportView(listaResponsables2);
		scrollPane_1.setColumnHeaderView(listaResponsables2);
		
		GridBagConstraints gbc_cbCargos = new GridBagConstraints();
		gbc_cbCargos.gridwidth = 2;
		gbc_cbCargos.insets = new Insets(0, 0, 5, 5);
		gbc_cbCargos.gridx = 2;
		gbc_cbCargos.gridy = 5;
		maingrid.add(cbCargos, gbc_cbCargos);
		
		bAnadir = new JButton("Anadir");
		bAnadir.addActionListener(this);
		GridBagConstraints gbc_bAnadir = new GridBagConstraints();
		gbc_bAnadir.gridwidth = 2;
		gbc_bAnadir.insets = new Insets(0, 0, 5, 5);
		gbc_bAnadir.gridx = 2;
		gbc_bAnadir.gridy = 6;
		maingrid.add(bAnadir, gbc_bAnadir);
		
		bQuitar = new JButton("Quitar");
		bQuitar.addActionListener(this);
		GridBagConstraints gbc_bQuitar = new GridBagConstraints();
		gbc_bQuitar.gridwidth = 2;
		gbc_bQuitar.insets = new Insets(0, 0, 5, 5);
		gbc_bQuitar.gridx = 2;
		gbc_bQuitar.gridy = 7;
		maingrid.add(bQuitar, gbc_bQuitar);
		
		
		
		
		
		bCargarDatos = new JButton("CargarDatos");
		GridBagConstraints gbc_bCargarDatos = new GridBagConstraints();
		gbc_bCargarDatos.insets = new Insets(0, 0, 5, 5);
		gbc_bCargarDatos.gridx = 1;
		gbc_bCargarDatos.gridy = 8;
		bCargarDatos.addActionListener(this);
		maingrid.add(bCargarDatos, gbc_bCargarDatos);
		
		bBorrarDatos = new JButton("BorrarDatos");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 9;
		bBorrarDatos.addActionListener(this);

		maingrid.add(bBorrarDatos, gbc_btnNewButton);
		
		
		
		
		JButton bDescargarLog = new JButton("DescargarLog");
		GridBagConstraints gbc_bDescargarLog = new GridBagConstraints();
		gbc_bDescargarLog.insets = new Insets(0, 0, 0, 5);
		gbc_bDescargarLog.gridx = 1;
		gbc_bDescargarLog.gridy = 10;
		maingrid.add(bDescargarLog, gbc_bDescargarLog);
		
		TextArea logText = new TextArea();
		logText.setText("Log indicando fallos");
		GridBagConstraints gbc_logText = new GridBagConstraints();
		gbc_logText.anchor = GridBagConstraints.NORTH;
		gbc_logText.gridwidth = 2;
		gbc_logText.gridx = 3;
		gbc_logText.gridy = 10;
		maingrid.add(logText, gbc_logText);
		logText.setEditable(false);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==bHome) {
			controlador.mostrarHome();
        } else if (e.getSource()==bVicerrector) {
            controlador.mostrarAlumnos();
        } else if (e.getSource()==bGestorSede) {
            controlador.mostrarAulas();
		} else if (e.getSource()==bAulas) {
			controlador.mostrarAulas();
		}else if (e.getSource()==bResponsablesExamen) {
			controlador.mostrarResponsablesExamenes();
		}else if(e.getSource()==bExamenes) {
			controlador.mostrarExamenes();
		}else if(e.getSource()==bCargarDatos) {
			
			System.out.println("llego al boton");
			try {
				controladorResponsablesExamen.abrirArchivo();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			refresh();
		}else if(e.getSource()==bBorrarDatos) {
			controladorResponsablesExamen.borrarDatos();
			listModel.clear();
		}else if(e.getSource()==bAnadir) {
			System.out.println(cargo.equals("Vocal"));
			System.out.println((!controladorResponsablesExamen.vocalAsignado(cargo,nombreExamen)));
			if(cargo.equals("Vocal") && !controladorResponsablesExamen.vocalAsignado(cargo,nombreExamen)) {
				JOptionPane.showMessageDialog(null, "El responsable de examen ya existe");
			}else{
				controladorResponsablesExamen.anadirResponsableExamen(nombreResponsableExamen,cargo, nombreExamen);
				System.out.println(listModel.elementAt(index));
				//listModel.removeElementAt(index);
				//listModel.clear();
				refresh();
				refresh2();
				scrollPane.repaint();
				scrollPane_1.repaint();
			}
			System.out.println(cargo.equals("Vocal"));
			System.out.println((!controladorResponsablesExamen.vocalAsignado(cargo,nombreExamen)));
			
		}else if(e.getSource()==bQuitar){
			controladorResponsablesExamen.quitarResponsableExamen(nombreAsingnado);
			refresh();
			refresh2();
			scrollPane.repaint();
			scrollPane_1.repaint();

		}
	}
	
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
	}
}

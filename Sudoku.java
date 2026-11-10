import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Sudoku extends JFrame implements ActionListener{
	//Iniciamos los atributos
	private int[][] tableroInt = new int[9][9]; //Tablero de sudoku para el jugador
	private int[][] tableroSolucion = new int[9][9];	//Tablero de sudoku con solucion 
	private boolean[][] esOriginal = new boolean[9][9]; //Saber que numero vienes de la matriz solucion
	private JButton[][] tableroBotones = new JButton[9][9]; //Botones del tablero
	private JButton btnJugar, btnReiniciar, btnDarPista; //Botones para el sudoku
	private JComboBox<String> dificultadTablero; //Seleccionar la dificultad del sudoku
	private JPopupMenu numeroParaMeter; //El menu que se desplega al tocar un botón y querer asignar un numero
	private JMenuItem itemUno, itemDos, itemTres, itemCuatro, itemCinco, itemSeis, itemSiete, itemOcho, itemNueve, itemCero; //Las opciones del menu
	private int dificultad, pistasParaJugador; //La dificultad y las pistas para el jugador
	//Esto es para cambiar entre pantallas en vez de tener 2 JFrame en 2 clases diferentes, usaremos cardLayout
	private CardLayout cardLayout; //Componente que nos ayuda a switchear de pantalla
	private JPanel contenedor; //El contenedor de todo
	private JPanel panelMenu; //Pantallas, pantalla del menu
	private JPanel panelJuego; //Pantallas tipo, pantalla del juego
	private JLabel bienvidoAlJuego, pistasQueTeQuedan, dificultadSeleccionada; //Etiquetas

	//Variables adicionales para la selección de casillas
	private int filaSeleccionada = -1;
	private int columnaSeleccionada = -1;

	//Colores de la interfaz
	private static final Color COLOR_FONDO = new Color(245, 247, 250);
	private static final Color COLOR_PRIMARIO = new Color(52, 101, 164);
	private static final Color COLOR_TEXTO = new Color(40, 44, 52);
	private static final Color COLOR_BLOQUE_CLARO = new Color(255, 255, 255);
	private static final Color COLOR_BLOQUE_OSCURO = new Color(232, 238, 247);

	//Inicia mis tableros de sudoku a 0 o false
	public void IniciarTableroSudoku(){
		for(int i = 0; i < tableroInt.length; i++){
			for(int j = 0; j < tableroInt[i].length; j++){
				esOriginal[i][j] = false;
				tableroInt[i][j] = 0;
				tableroSolucion[i][j] = 0;
			}
		}
	}

	//El generador de Sudokus unicos!
	public boolean BackTrackingSolverSudoku(){
		//Iniciamos una lista de manera random para asegurar de que no sea siempre la mimsa matriz solucion
		ArrayList<Integer> numPosSudoku = new ArrayList<>();
		for(int k = 0; k < 9; k++){
			numPosSudoku.add(k + 1);
		}
		Collections.shuffle(numPosSudoku);

		//Primer paso el doble bucle para viajar por la matriz
		for(int i = 0; i < tableroSolucion.length; i++){
			for(int j = 0; j < tableroSolucion[i].length; j++){

				//Si tenemos una casilla vacía probamos numeros
				if(tableroSolucion[i][j] == 0){
					for(int k = 0; k < numPosSudoku.size(); k++){

						//Si realmente el numero se puede poner, lo colocamos
						if (VerificarCasilla(tableroSolucion,i, j, numPosSudoku.get(k))){
							tableroSolucion[i][j] = numPosSudoku.get(k);
							
						//Aqui llamamos el BackTracking para resolver el resto
							if(BackTrackingSolverSudoku()){
								return true;
							}
							//El programa se para en true, pero si no encuentra nada pone 0
							tableroSolucion[i][j] = 0;
						}
					}
				//Aqui ponemos un false pora advertir de que no pudimos poner nada ya que los numeros que se probaron no sirvieron
				//Aqui lo que hacemos es hacer una vuelta atras para decir que el numero de atras está mal
				return false;
				}
			}
		}
		//Aqui es para decir que la matriz ya esta resuelta
		return true;
	}

	//Verificador de casillas!
	public boolean VerificarCasilla(int[][] tablero,int fila, int columna, int num){
		return VerificarFila(tablero, fila, num) && VerificarColumna(tablero, columna, num) && VerificarBloque(tablero,fila, columna, num);
	}

	//Verificador de linea
	public boolean VerificarFila(int[][] tablero, int fila, int num){
		//Vamos a visitar toda la linea
		int occ = 0; //Las veces que aparece ese numero en nuestra fila
		for(int j = 0; j < tablero[fila].length; j++){
			if(tablero[fila][j] == num){
				occ++;
			}
		}
		if(occ == 0){
			return true;
		}else{
			return false;
		}
	}

	//Verificador de columna
	public boolean VerificarColumna(int[][] tablero,int columna, int num){
		//Vamos a visitar toda la columna
		int occ = 0; //Las veces que aparece ese numero en nuestra columna
		for(int i = 0; i < tablero.length; i++){
			if(tablero[i][columna] == num){
				occ++;
			}
		}
		if(occ == 0){
			return true;
		}else{
			return false;
		}
	}

	//Verificador correspondiente de su 3x3
	public boolean VerificarBloque(int[][] tablero,int fila, int columna, int num){
		//Sabemos muchas cosas, primero, que un bloque es de 3x3 osea el primer bloque es 0 2 etc etc
		int inicioFila, inicioColumna;
		//Es para saber donde empieza, usa
		inicioFila = (fila / 3) * 3;
		inicioColumna = (columna / 3) * 3;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(tablero[inicioFila + i][inicioColumna + j] == num){
					return false;
				}
			}
		}
		return true;
	}

	//Vamos a verificar de que solo existe una solución en el sudoku generado
	public int ContarSolucion(int[][] tablero){
		//Vamos a parcourir la matriz
		for(int i = 0; i < tablero.length; i++){
			for(int j = 0; j < tablero[i].length; j++){

				//Verificamos si hay una casilla libre
				if(tablero[i][j] == 0){
					int contador = 0;

					//Probaremos todos los numeros possibles en esa matriz
					for(int l = 1; l <= 9; l++){
						if(VerificarCasilla(tablero,i,j,l)){
							tablero[i][j] = l;
							contador += ContarSolucion(tablero);
							tablero[i][j] = 0;
							//Si hay mas de una solucion pum, salimos del bucle
							if (contador >= 2){
								return 2;
							}
						}
					}
					return contador; //Nos dira cuantas soluciones ha encontrado, y salimos directamente
				}	
			}
		}
		return 1; //Por si no habian casillas libres, nos devuelve 1
	}

	//Generador de Sudoku para nuestro querido user
	public void GeneradorDeSudokus(int dificultad){
		int pistasUser, f, c, casillasEliminadas, respaldo; //Las pistas para nuestro user + filas y columnas y el respaldo por si no se puede borrar el num
		switch(dificultad){
			case 1: //Nivel facil
				pistasUser = 40;
				break;
			case 2: //Nivel medio
				pistasUser = 33;
				break;
			case 3: //Nivel dificil
				pistasUser = 28;
				break;
			default:
				pistasUser = 40;
				break;
		}
		//Array para las posiciones aleatorias del tablero de sudoku
		ArrayList<Integer> casillasPosicion = new ArrayList<>();
		for(int i = 0; i < 81; i++){
			casillasPosicion.add(i);
		}
		Collections.shuffle(casillasPosicion);

		//Para saber cuantas casillas debemos de poner
		int casillasAEliminar = 81 - pistasUser;
		casillasEliminadas = 0; //Simplemente me doy cuenta que podriamos haber hecho casillasAEliminar-- xd
		//Primero copiamos la matriz solucion a tablero para jugador
		for(int i = 0; i < tableroSolucion.length; i++){
			for(int j = 0; j < tableroSolucion[i].length; j++){
				tableroInt[i][j] = tableroSolucion[i][j];
			}
		}

		//Para eliminar en funcion del nivel
		for(int k = 0; casillasEliminadas < casillasAEliminar && k < casillasPosicion.size();k++){
			f = casillasPosicion.get(k) / 9;
			c = casillasPosicion.get(k) % 9;
			respaldo = tableroInt[f][c];
			tableroInt[f][c] = 0;
			if(ContarSolucion(tableroInt) == 1){
				casillasEliminadas++;
			}else{
				tableroInt[f][c] = respaldo;
			}
		}

		//Para saber que numeros son nuestros
		for(int i = 0; i < tableroSolucion.length; i++){
			for(int j = 0; j < tableroSolucion[i].length; j++){
				if(tableroInt[i][j] != 0){
					esOriginal[i][j] = true;
				}
			}
		}
	}

	//Funcion de reiniciar sudoku
	public void Reiniciar(){
		cardLayout.show(contenedor, "MENU");
	}
	
	//Sistema que da pistas
	public void SistemaDePistas(){

		//Si no quedan pistas, no fotem res
		if(pistasParaJugador <= 0){
			return;
		}

		//Creamos una lista con las posiciones de la matriz
		ArrayList<Integer> casillasPosicion = new ArrayList<>();
		for(int i = 0; i < 81; i++){
			casillasPosicion.add(i);
		}
		Collections.shuffle(casillasPosicion);

		//Buscamos una casilla que el jugador aun no tiene bien
		for(int k = 0; k < casillasPosicion.size(); k++){
			int filaInicio = casillasPosicion.get(k) / 9;
			int columnaInicio = casillasPosicion.get(k) % 9;

			if(tableroInt[filaInicio][columnaInicio] != tableroSolucion[filaInicio][columnaInicio]){
				//Ponemos el numero y modificamos todo lo que le concierne
				tableroInt[filaInicio][columnaInicio] = tableroSolucion[filaInicio][columnaInicio];
				esOriginal[filaInicio][columnaInicio] = true;
				tableroBotones[filaInicio][columnaInicio].setText(String.valueOf(tableroSolucion[filaInicio][columnaInicio]));
				tableroBotones[filaInicio][columnaInicio].setEnabled(false);
				pistasParaJugador--;
				pistasQueTeQuedan.setText("Pistas: " + pistasParaJugador);
				return; //Una sola pista por llamada
			}
		}
	}

	//Comprueba si hay victoria o no
	public boolean ComprobarVictoria(){
		//Verificamos si coincide cada item de cada matriz, era tan facil
		for(int i = 0; i < tableroInt.length; i++){
			for(int j = 0; j < tableroInt[i].length; j++){
				if(tableroInt[i][j] != tableroSolucion[i][j]){
					return false;
				}
			}
		}
		btnReiniciar.setVisible(true);
		return true;
	}
	
	//Crea una opcion del menu desplegable de numeros
	private JMenuItem CrearItemNumero(String texto){
		JMenuItem item = new JMenuItem(texto);
		item.setFont(new Font("SansSerif",Font.BOLD,18));
		item.setHorizontalAlignment(SwingConstants.CENTER);
		item.addActionListener(this);
		return item;
	}

	public Sudoku(){
		//Ventana
		super("Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(620, 770);
		setResizable(false);
		setLocationRelativeTo(null);

		//Creacion de la interfaz grafica
		cardLayout = new CardLayout();
		contenedor = new JPanel(cardLayout);
		panelMenu = new JPanel(null);
		panelMenu.setBackground(COLOR_FONDO);
		panelJuego = new JPanel(null);
		panelJuego.setBackground(COLOR_FONDO);
		contenedor.add(panelMenu, "MENU");
		contenedor.add(panelJuego, "JUEGO");
		this.add(contenedor);

		// ================= PANTALLA DEL MENU =================
		bienvidoAlJuego = new JLabel("SUDOKU");
		bienvidoAlJuego.setFont(new Font("SansSerif", Font.BOLD, 45));
		bienvidoAlJuego.setBounds(140, 130, 330, 60);
		bienvidoAlJuego.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenu.add(bienvidoAlJuego);

		JLabel subtitulo = new JLabel("Elige un nivel y empieza a jugar");
		subtitulo.setBounds(130, 195, 360, 30);
		subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenu.add(subtitulo);

		dificultadTablero = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"});
		dificultadTablero.setBounds(150, 280, 300, 35);
		panelMenu.add(dificultadTablero);

		btnJugar = new JButton("Jugar");
		btnJugar.setBounds(170, 350, 260, 45);
		btnJugar.addActionListener(this);
		panelMenu.add(btnJugar);

		// ================= PANTALLA DEL JUEGO =================
		dificultadSeleccionada = new JLabel("Nivel: -");
		dificultadSeleccionada.setBounds(30, 15, 200, 25);
		panelJuego.add(dificultadSeleccionada);

		pistasQueTeQuedan = new JLabel("Pistas: 0");
		pistasQueTeQuedan.setBounds(440, 15, 150, 25);
		panelJuego.add(pistasQueTeQuedan);

		//Menu desplegable con los numeros del 1 al 9
		numeroParaMeter = new JPopupMenu();
		itemUno = CrearItemNumero("1");
		itemDos = CrearItemNumero("2");
		itemTres = CrearItemNumero("3");
		itemCuatro = CrearItemNumero("4");
		itemCinco = CrearItemNumero("5");
		itemSeis = CrearItemNumero("6");
		itemSiete = CrearItemNumero("7");
		itemOcho = CrearItemNumero("8");
		itemNueve = CrearItemNumero("9");
		itemCero = CrearItemNumero("0");
		numeroParaMeter.add(itemUno);
		numeroParaMeter.add(itemDos);
		numeroParaMeter.add(itemTres);
		numeroParaMeter.add(itemCuatro);
		numeroParaMeter.add(itemCinco);
		numeroParaMeter.add(itemSeis);
		numeroParaMeter.add(itemSiete);
		numeroParaMeter.add(itemOcho);
		numeroParaMeter.add(itemNueve);
		numeroParaMeter.add(itemCero);

		//Tablero 9x9, los bloques de 3x3 se distinguen por el color y por las lineas gruesas
		JPanel panelTablero = new JPanel(new GridLayout(9, 9));
		panelTablero.setBounds(25, 55, 550, 550);
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				JButton casilla = new JButton("");
				casilla.setFont(new Font("SansSerif", Font.BOLD, 22));
				casilla.setForeground(COLOR_PRIMARIO);
				casilla.setFocusPainted(false);
				casilla.setMargin(new Insets(0, 0, 0, 0));
				casilla.addActionListener(this);
				tableroBotones[i][j] = casilla;
				panelTablero.add(casilla);
				//------------------------
				// Aqui van los bordes!
				//------------------------
				int top;
				if (i % 3 == 0) {
					top = 3;
				} else {
					top = 1;
				}

				int left;
				if (j % 3 == 0) {
					left = 3;
				} else {
					left = 1;
				}

				int bottom;
				if (i == 8) {
					bottom = 3;
				} else {
					bottom = 1;
				}

				int right;
				if (j == 8) {
					right = 3;
				} else {
					right = 1;
				}
				casilla.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, COLOR_PRIMARIO));
				
				//--------------------------
				//Fin logica bordes
				//--------------------------
			}
		}
		
		
		panelJuego.add(panelTablero);

		//Parte de abajo: pista y reiniciar
		btnDarPista = new JButton("Pista");
		btnDarPista.setBounds(75, 635, 200, 40);
		btnDarPista.addActionListener(this);
		panelJuego.add(btnDarPista);

		btnReiniciar = new JButton("Reiniciar");
		btnReiniciar.setBounds(345, 635, 200, 40);
		btnReiniciar.addActionListener(this);
		btnReiniciar.setVisible(false);
		panelJuego.add(btnReiniciar);

		//Empezamos en el menu
		cardLayout.show(contenedor, "MENU");
	}

	@Override // A partir de ahora hay que empezar a usarlo 
	public void actionPerformed(ActionEvent e){

		//Cuando el usuario elige el nivel, inicializamos dificultad y pistas una sola vez
		if(e.getSource() == btnJugar){
			dificultad = dificultadTablero.getSelectedIndex() + 1;
			String nombreDificultad = (String) dificultadTablero.getSelectedItem();
			dificultadSeleccionada.setText("Nivel: " + nombreDificultad);
			switch(dificultad){
				case 1: //Nivel facil
					pistasParaJugador = 10;
					break;
				case 2: //Nivel medio
					pistasParaJugador = 5;
					break;
				case 3: //Nivel dificil
					pistasParaJugador = 0;
					break;
				default:
					pistasParaJugador = 10;
					break;
			}
			pistasQueTeQuedan.setText("Pistas: " + pistasParaJugador);
			IniciarTableroSudoku();
			BackTrackingSolverSudoku();
			GeneradorDeSudokus(dificultad);
			btnReiniciar.setVisible(false);

			// Actualizar visualmente los botones del tablero
			for(int i = 0; i < 9; i++){
				for(int j = 0; j < 9; j++){
					if(tableroInt[i][j] != 0){
						tableroBotones[i][j].setText(String.valueOf(tableroInt[i][j]));
						if(esOriginal[i][j]){
							tableroBotones[i][j].setForeground(Color.BLACK);
							tableroBotones[i][j].setEnabled(false);
						} else {
							tableroBotones[i][j].setForeground(COLOR_PRIMARIO);
							tableroBotones[i][j].setEnabled(true);
						}
					} else {
						tableroBotones[i][j].setText("");
						tableroBotones[i][j].setForeground(COLOR_PRIMARIO);
						tableroBotones[i][j].setEnabled(true);
					}
				}
			}

			cardLayout.show(contenedor, "JUEGO");
		}

		//Reiniciar juego
		if(e.getSource() == btnReiniciar){
			Reiniciar();
		}

		//Sistema de pistas
		if(e.getSource() == btnDarPista){
			SistemaDePistas();
			if(ComprobarVictoria()){
				JOptionPane.showMessageDialog(this, "¡Felicidades, has completado el Sudoku!");
				Reiniciar();
			}
		}

		//Selección de número del menú desplegable para colocar en el tablero
		if(e.getSource() == itemUno || e.getSource() == itemDos || e.getSource() == itemTres ||
		   e.getSource() == itemCuatro || e.getSource() == itemCinco || e.getSource() == itemSeis ||
		   e.getSource() == itemSiete || e.getSource() == itemOcho || e.getSource() == itemNueve || e.getSource() == itemCero){
			
			if(filaSeleccionada != -1 && columnaSeleccionada != -1){
				JMenuItem itemSeleccionado = (JMenuItem) e.getSource();
				int numeroElegido = Integer.parseInt(itemSeleccionado.getText());
				if (numeroElegido != 0){
					tableroInt[filaSeleccionada][columnaSeleccionada] = numeroElegido;
					tableroBotones[filaSeleccionada][columnaSeleccionada].setText(String.valueOf(numeroElegido));
					tableroBotones[filaSeleccionada][columnaSeleccionada].setForeground(COLOR_PRIMARIO);
				}else{
					tableroInt[filaSeleccionada][columnaSeleccionada] = numeroElegido;
					tableroBotones[filaSeleccionada][columnaSeleccionada].setText(" ");
					tableroBotones[filaSeleccionada][columnaSeleccionada].setForeground(COLOR_PRIMARIO);
				}
				if(ComprobarVictoria()){
					JOptionPane.showMessageDialog(this, "¡Felicidades, has completado el Sudoku!");
				}
			}
		}

		//Comprobar clics en las casillas del tablero
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(e.getSource() == tableroBotones[i][j]){
					if(!esOriginal[i][j]){
						filaSeleccionada = i;
						columnaSeleccionada = j;
						numeroParaMeter.show(tableroBotones[i][j], tableroBotones[i][j].getWidth() / 2, tableroBotones[i][j].getHeight() / 2);
					}
				}
			}
		}
	}

	public static void main(String args[]){
		Sudoku interfaz = new Sudoku();
		interfaz.setVisible(true);
	}
}
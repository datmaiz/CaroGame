import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class View extends JFrame {

	String player = "X";
	int XTicked = 1, YTicked = 1;
	private Algorithm algorithm;
	int column, row;
	private ButtonModel caroItem[][];
	private boolean isTicked[][];
	private JLabel turn;
	private JPanel toolBarGame;
	private JPanel contentPanel;
	private JPanel cardPanel;
	private final CardLayout cardLayout = new CardLayout();
	private Stack<PointStep> step;
	private JButton undoButton;
	private JButton waitButton;
	private JButton newGame;
	private Vector<ButtonModel> listWin;
	private Status direction = Status.NONE_DIRECTION;

	private Client client;
	private boolean state = true;

	public View(int row, int column) {
		this.column = column;
		this.row = row;
		setSize(column * 80, row * 80);
		caroItem = new ButtonModel[row][column];
		isTicked = new boolean[row][column];
		setDefaultStatusButton();
		algorithm = new Algorithm();
		initDisplay();
		initComponents();
		revalidate();
		repaint();

		networking();
	}

	public void networking() {
		// new Thread() {
		// @Override
		// public void run() {
		// Scanner sc = new Scanner(System.in);
		// while (true) {
		// int row = sc.nextInt();
		// int col = sc.nextInt();

		// ButtonModel jButton = caroItem[row][col];
		// if (!isTicked[jButton.row][jButton.column]) {
		// PointStep coordinated = new PointStep(jButton.row, jButton.column);
		// step.push(coordinated);
		// jButton.setText(player);
		// algorithm.drawTick(jButton);
		// boolean playerWin = checkWinner(jButton.column, jButton.row);
		// if (playerWin) {
		// JOptionPane.showMessageDialog(null, "Trời ơi bạn " + player
		// + " chơi hay quá!!!");
		// JOptionPane.showMessageDialog(null, "Sao mày chơi ngu vậy hả " +
		// algorithm.checkPlayer(player) + "????");
		// newGame();
		// }
		// player = algorithm.checkPlayer(player);
		// showTurn();
		// isTicked[jButton.row][jButton.column] = true;
		// }
		// }
		// }
		// }.start();

		client = new Client() {
			@Override
			public void applyNewUpdate() {
				if (state == false) {
					state = true;
					return;
				}

				int row = point.row;
				int col = point.column;

				ButtonModel jButton = caroItem[row][col];
				if (!isTicked[jButton.row][jButton.column]) {
					PointStep coordinated = new PointStep(jButton.row, jButton.column);
					step.push(coordinated);
					jButton.setText(player);
					algorithm.drawTick(jButton);
					boolean playerWin = checkWinner(jButton.column, jButton.row);
					if (playerWin) {
						JOptionPane.showMessageDialog(null, "Trời ơi bạn " + player
								+ " chơi hay quá!!!");
						JOptionPane.showMessageDialog(null, "Sao mày chơi ngu vậy hả " +
								algorithm.checkPlayer(player) + "????");
						newGame();
					}
					player = algorithm.checkPlayer(player);
					showTurn();
					isTicked[jButton.row][jButton.column] = true;
				}
			}
		};
	}

	private void setDefaultStatusButton() {
		for (int i = 0; i < column; i++) {
			for (int j = 0; j < row; j++) {
				isTicked[i][j] = false;
			}
		}
	}

	private void initDisplay() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponents() {
		cardPanel = new JPanel(cardLayout);
		listWin = new Vector<>();
		contentPanel = new JPanel(new GridLayout(column, row));
		turn = new JLabel("Lượt của: " + player.toUpperCase());
		toolBarGame = new JPanel();
		undoButton = createButton("Undo");
		waitButton = createButton("Wait");
		newGame = createButton("New Game");

		turn.setFont(new Font("Arial", Font.BOLD, 20));
		toolBarGame.setLayout(new BoxLayout(toolBarGame, BoxLayout.X_AXIS));
		toolBarGame.setBackground(Color.WHITE);
		toolBarGame.setPreferredSize(new Dimension(0, 50));
		toolBarGame.add(turn);
		toolBarGame.add(new JSeparator());
		toolBarGame.add(newGame);
		toolBarGame.add(waitButton);
		toolBarGame.add(undoButton);
		step = new Stack<>();

		createChessboard();

		cardPanel.add(contentPanel, "game");
		cardPanel.add(new WaitPanel(), "wait");

		cardLayout.show(cardPanel, "game");

		add(toolBarGame, BorderLayout.NORTH);
		add(cardPanel, BorderLayout.CENTER);

		initEvent();
	}

	private void newGame() {
		System.out.println(step.size());
		int index = 1;
		while (!step.isEmpty()) {
			PointStep point = step.peek();
			caroItem[point.row][point.column].setText(" ");
			isTicked[point.row][point.column] = false;
			step.pop();
			System.out.println("pop " + index++);
		}
		player = "X";
		showTurn();
	}

	private boolean checkWinner(int c, int r) {
		// check column
		int indexColumn = c;
		int indexRow = r;
		XTicked = 0;
		YTicked = 0;
		// check current index to the right
		for (int i = 0; i < 5; i++) {
			if (indexColumn > this.column - 1)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				++indexColumn;
			}
		}

		System.out.println("Column 1 - X: " + XTicked + " || Y: " + YTicked);
		if (XTicked >= 5 || YTicked >= 5) {
			return true;
		}

		indexColumn = c - 1;
		// check current index to the left
		for (int i = 0; i < 5; i++) {
			if (indexColumn <= 0)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				--indexColumn;
			}
		}

		System.out.println("Column 2 - X: " + XTicked + " || Y: " + YTicked);
		if (XTicked >= 5 || YTicked >= 5) {

			return true;
		}
		indexColumn = c;
		indexRow = r;
		// check row
		XTicked = 0;
		YTicked = 0;
		// check current index to bottom
		for (int i = 0; i < 5; i++) {
			if (indexRow >= this.row - 1)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				++indexRow;
			}
		}

		System.out.println("Row 1 - X: " + XTicked + " || Y: " + YTicked);
		if (XTicked >= 5 || YTicked >= 5)
			return true;

		indexRow = r - 1;
		// check current index to top
		for (int i = 0; i < 5; i++) {
			if (indexRow <= 0)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				--indexRow;
			}
		}

		System.out.println("Row 2 - X: " + XTicked + " || Y: " + YTicked);
		if (XTicked >= 5 || YTicked >= 5)
			return true;

		XTicked = 0;
		YTicked = 0;

		// check left diagonal line
		// reset "count" variable and start check,
		// check to bottom
		indexRow = r;
		indexColumn = c;
		for (int i = 0; i < 5; i++) {
			if (indexColumn >= this.column - 1 || indexRow >= this.row - 1)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				++indexColumn;
				++indexRow;
			}
		}
		//
		if (XTicked >= 5 || YTicked >= 5)
			return true;
		System.out.println("LeftDiagonal 1 - X: " + XTicked + " || Y: " + YTicked);
		indexColumn = c - 1;
		indexRow = r - 1;
		// check to top
		for (int i = 0; i < 5; i++) {
			if (indexColumn <= 0 || indexRow <= 0)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				--indexColumn;
				--indexRow;
			}
		}
		//
		if (XTicked >= 5 || YTicked >= 5)
			return true;
		System.out.println("LeftDiagonal 2 - X: " + XTicked + " || Y: " + YTicked);

		// // check right diagonal line
		// // reset "count"
		// check to bottom
		XTicked = 0;
		YTicked = 0;
		indexRow = r;
		indexColumn = c;
		for (int i = 0; i < 5; i++) {
			if (indexColumn <= 0 || indexRow >= this.row - 1)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				--indexColumn;
				++indexRow;
			}
		}

		// check to top
		if (XTicked >= 5 || YTicked >= 5)
			return true;
		System.out.println("RightDiagonal 1 - X: " + XTicked + " || Y: " + YTicked);
		indexRow = r - 1;
		indexColumn = c + 1;

		for (int i = 0; i < 5; i++) {
			if (indexColumn >= this.column - 1 || indexRow <= 0)
				break;
			else {
				if(caroItem[indexRow][indexColumn].getText().equals(player)) {
					if(player.equals("X")) ++XTicked;
					else if(player.equals("O")) ++YTicked;
				}
				else break;
				++indexColumn;
				--indexRow;
			}
		}
		System.out.println("RightDiagonal 2 - X: " + XTicked + " || Y: " + YTicked);

		return XTicked >= 5 || YTicked >= 5;

	}

	private void removeList() {
		while (listWin.size() != 0) {
			listWin.remove(listWin.get(listWin.size() - 1));
		}
	}

	private void createChessboard() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				caroItem[i][j] = new ButtonModel(" ", i, j);
				caroItem[i][j].addActionListener(new ActionClicked(caroItem[i][j]));
				caroItem[i][j].setFont(new Font("Ink Free", 1, 50));
				contentPanel.add(caroItem[i][j]);
			}
		}
	}

	private void initEvent() {
		undoButton.addActionListener((e) -> {

			try {
				doUndo();
			} catch (EmptyStackException st) {
				JOptionPane.showMessageDialog(null, "Các nước đi trống!!");
			}
		});

		waitButton.addActionListener((e) -> {

			if (waitButton.getText().equals("Wait")) {
				cardLayout.show(cardPanel, "wait");
				waitButton.setText("Continue");
			} else {
				cardLayout.show(cardPanel, "game");
				waitButton.setText("Wait");
			}
		});

		newGame.addActionListener((e) -> {

			while (!step.empty()) {
				newGame();
//				step.pop();
			}
			XTicked = 1;
			YTicked = 1;
			player = "X";
		});
	}

	private void doUndo() {
		PointStep point = step.peek();
		step.pop();
		caroItem[point.row][point.column].setText("");
		isTicked[point.row][point.column] = false;
		player = algorithm.checkPlayer(player);
		showTurn();
	}

	private JButton createButton(String text) {
		JButton jButton = new JButton(text);
		jButton.setFocusable(false);
		jButton.setBackground(Color.WHITE);
		Font font = jButton.getFont();
		jButton.setFont(new Font("Arial", Font.BOLD, font.getSize() + 3));
		return jButton;
	}

	class ActionClicked implements ActionListener {

		private ButtonModel jButton;

		public ActionClicked(ButtonModel jButton) {
			this.jButton = jButton;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isTicked[jButton.row][jButton.column]) {
				PointStep coordinated = new PointStep(jButton.row, jButton.column);
				step.push(coordinated);
				jButton.setText(player);
				algorithm.drawTick(this.jButton);
				boolean playerWin = checkWinner(jButton.column, jButton.row);
				if (playerWin) {
					JOptionPane.showMessageDialog(null, "Trời ơi bạn " + player
							+ " chơi hay quá!!!");
					JOptionPane.showMessageDialog(null, "Sao mày chơi ngu vậy hả " +
					algorithm.checkPlayer(player) + "????");
//					newGame();
				}
				player = algorithm.checkPlayer(player);
				showTurn();
				isTicked[jButton.row][jButton.column] = true;
				
				// networking part
				state = false;
				client.sendNewUpdate(coordinated);
			}
		}
	}

	private void showTurn() {
		turn.setText("Lượt của: " + player.toUpperCase());
	}

	public static void main(String[] args) {
		new View(10, 10);
	}
}

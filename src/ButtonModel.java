import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonModel extends JButton {

	int row, column;
	Status status = Status.NO_WIN;

	public ButtonModel(String text, int row, int column) {
		setText(text);
	    setFocusable(false);
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK));
		setFont(new Font("Poppins", Font.BOLD, 25));
		this.row = row;
		this.column = column;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		if(status == Status.X_WIN) {
			setBackground(Color.RED);
			setForeground(Color.WHITE);
		}
		else if(status == Status.Y_WIN) {
			setBackground(Color.BLUE);
			setForeground(Color.WHITE);
		}


	}

}

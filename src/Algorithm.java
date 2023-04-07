import javax.swing.*;
import java.awt.*;

public class Algorithm {

	public String checkPlayer(String player) {
		return (player.equals("X")) ? "O" : "X";
	}

	public void drawTick(JButton button) {
		if(button.getText().equals("X")) {
			button.setForeground(Color.RED);
		}
		else {
			button.setForeground(Color.BLUE);
		}
	}


}

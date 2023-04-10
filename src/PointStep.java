import java.io.Serializable;

public class PointStep implements Serializable{

	int row, column;

	PointStep(int row, int column) {
		this.row = row;
		this.column = column;
	}

}

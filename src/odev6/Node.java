package odev6;

public class Node {
	private String txt;
	private int value;

	public Node(String txt, int value) {
		this.txt = txt;
		this.value = value;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
package javafiler.graphs;

public class Edge<X> {

	private X dest;
	private String namn;
	private int vikt;

	public Edge(X dest, String namn, int vikt) {
		if (vikt < 0)
			throw new IllegalArgumentException("Negativ vikt");
		this.dest = dest;
		this.namn = namn;
		this.vikt = vikt;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public int getVikt() {
		return vikt;
	}

	public void setVikt(int vikt) {
		this.vikt = vikt;
	}
	public X getDest(){
		return this.dest;
	}
	public void setDest(X dest){
		this.dest = dest;
	}


	public String toString() {
		return namn + " till " + dest + " (" + vikt + ")" + "\n";
	}
}

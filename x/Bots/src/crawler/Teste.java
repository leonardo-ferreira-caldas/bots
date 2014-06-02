package crawler;

public class Teste extends Thread {

	int i;
	
	public Teste(int i) {
		this.i = i;
	}
	
	public void run() {
		System.out.print("Thread" + this.i);	
	}
	
}

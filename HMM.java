import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.util.ArrayList;

public class HMM{
	double k;
	double EgivenS;
	double FgivenS;
	double EgivenT;
	double FgivenT;
	char[] sequence;
	int num;
	ArrayList<String> toCompute;

	ArrayList<Double> S;
	ArrayList<Double> T;
	double SgivenS;
	double TgivenS;
	double TgivenT;
	double SgivenT;
	
	ArrayList<Double> E;
	ArrayList<Double> F;
	ArrayList<Double> ans;

	public HMM(){
		this.k = 0;
		this.EgivenS = 0;
		this.FgivenS = 0;
		this.EgivenT = 0;
		this.FgivenT = 0;
		this.num = 0;
		this.toCompute = new ArrayList<String>();

		this.S = new ArrayList<Double>();
		this.T = new ArrayList<Double>();
		this.SgivenS = 0;
		this.TgivenS = 0;
		this.TgivenT = 0;
		this.SgivenT = 0;
	
		this.E = new ArrayList<Double>();
		this.F = new ArrayList<Double>();
		this.ans = new ArrayList<Double>();
	}

	void readFile(){
		try{	
			String line;
			String[] sub;
			
			File file = new File("hmm_1.in");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			this.k = Double.parseDouble(br.readLine());
			System.out.println(this.k);
			System.out.println(br.readLine());
			System.out.println(br.readLine());

			line = br.readLine();
			sub = line.split(" ");
			this.EgivenS = Double.parseDouble(sub[0]);
			this.FgivenS = Double.parseDouble(sub[1]);

			line = br.readLine();
			sub = line.split(" ");
			this.EgivenT = Double.parseDouble(sub[0]);
			this.FgivenT = Double.parseDouble(sub[1]);

			this.sequence = (br.readLine()).toCharArray();
			this.num = Integer.parseInt(br.readLine());

			for (int i = 0; i < num; i++){
				this.toCompute.add(br.readLine());
			}

			for (int i = 0; i < this.toCompute.size(); i++){
				System.out.println(this.toCompute.get(i));
			}
		}catch(Exception e){
			e.getMessage();
		}	
	}

	void writeFile(){
		String line = "";
		for (int i=0; i<this.num; i++){
			line += "Probability of " + this.toCompute.get(i)+ " "+ this.ans.get(i) + "\n";
		}
		try{    
        	FileWriter fw=new FileWriter("hmm.out");    
        	fw.write(line);    
        	fw.close();    
        }catch(Exception e){System.out.println(e);}  

	}

	void proper(){
		readFile();
		computeTransitionalProb();
		for (String s: toCompute){
			System.out.println("ans " + computeCase(s));
			this.ans.add(computeCase(s));
		}

		writeFile();
		for (Double a: this.ans){
			System.out.println(a);
		}

		System.out.println("S" + this.S);
		System.out.println("T" + this.T);
		System.out.println("E" + this.E);
		System.out.println("F" + this.F);


	}
	void computeTransitionalProb(){
		double denomGivenS = 0;
		double denomGivenT = 0;
		if (this.sequence[0] == 'S'){
			this.S.add(1.0);
			this.T.add(0.0);
		}
		if (this.sequence[0] == 'T'){
			this.S.add(0.0);
			this.T.add(1.0);
		}

		denomGivenS += this.k * 2;
		denomGivenT += this.k * 2;

		this.SgivenS += this.k;
		this.TgivenS += this.k;
		this.TgivenT += this.k;
		this.SgivenT += this.k;

		for (int i = 0; i < this.sequence.length-1; i++){
			switch (this.sequence[i]){
				case 'S':
					denomGivenS++;
					switch (this.sequence[i+1]) {
						case 'S':
							this.SgivenS++;
							break;
						case 'T':	
							this.TgivenS++;					
							break;
					}
					break;
				case 'T':
					denomGivenT++;
					switch (this.sequence[i+1]) {
						case 'S':
							this.SgivenT++;
							break;
						case 'T':	
							this.TgivenT++;					
							break;
					}
			}
		}
		System.out.println(this.sequence);
		System.out.println("SgivenS: " + this.SgivenS);
		System.out.println("TgivenS: " + this.TgivenS);
		System.out.println("TgivenT: " + this.TgivenT);
		System.out.println("SgivenT: " + this.SgivenT);
		System.out.println("denomGivenS: " + denomGivenS);
		System.out.println("denomGivenT: " + denomGivenT);
		this.SgivenS /= denomGivenS;
		this.TgivenS /= denomGivenS;
		this.TgivenT /= denomGivenT;
		this.SgivenT /= denomGivenT;
		System.out.println("SgivenS: " + this.SgivenS);
		System.out.println("TgivenS: " + this.TgivenS);
		System.out.println("TgivenT: " + this.TgivenT);
		System.out.println("SgivenT: " + this.SgivenT);
		
		System.out.println("EgivenS: " + this.EgivenS);
		System.out.println("FgivenS: " + this.FgivenS);
		System.out.println("EgivenT: " + this.EgivenT);
		System.out.println("FgivenT: " + this.FgivenT);
	}

	double computeCase(String s){
		char firstParam = s.charAt(0);
		int firstN = s.charAt(1)-48;
		char secondParam = s.charAt(9);
		int secondN = s.charAt(10)-48;
		
		computeTotalProbS(firstN);
		computeTotalProbS(secondN);
		computeTotalProbE(firstN);
		computeTotalProbE(secondN);

		switch(firstParam){
			case 'S':
				switch(secondParam){
					case 'E':
						System.out.println("num " + this.EgivenS + "*" + this.S.get(firstN));
						System.out.println("denom " + this.E.get(firstN));
						System.out.println((this.EgivenS*this.S.get(firstN))/this.E.get(firstN));
						return (this.EgivenS*this.S.get(firstN))/this.E.get(firstN);
					case 'F':
						return (this.FgivenS*this.S.get(firstN))/this.F.get(firstN);
				}
			case 'T':
				switch(secondParam){
					case 'E':
						return (this.EgivenT*this.T.get(firstN))/this.E.get(secondN);
					case 'F':
						return (this.FgivenT*this.T.get(firstN))/this.F.get(secondN);					
				}			
		}
		return 0;
	}

	void computeTotalProbS(int N){
		double prob;
		while (N >= this.S.size()){
			prob = SgivenS*this.S.get(this.S.size()-1) + SgivenT*(this.T.get(this.T.size()-1));
			this.S.add(prob);
			this.T.add(1-prob);
		}
	}

	void computeTotalProbE(int N){
		double prob;
		if (this.E.size() == 0){
			this.E.add(0.0);
			this.F.add(0.0);
		}
		while (N >= this.E.size()){
			prob = EgivenS*this.S.get(this.S.size()-1) + EgivenT*(1-this.T.get(this.T.size()-1));
			this.E.add(prob);
			this.F.add(1-prob);
		}
	}
}
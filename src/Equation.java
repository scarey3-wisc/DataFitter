public class Equation{
	private double[] terms;
	public Equation(double[] terms){
		this.terms = terms;
	}
	public double[] getTerms(){
		return terms;
	}
	public double getTerm(int which){
		return terms[which];
	}
	public void add(double multiplyer, Equation addition){
		for(int i = 0; i < terms.length; i++){
			terms[i] += addition.getTerm(i) * multiplyer;
		}
	}
	public String print(){
		if(terms.length != 0){
			String nova = "";
			for(int i = 0; i < terms.length - 1; i++){
				nova += terms[i] + " ";
			}
			nova += " = " + terms[terms.length - 1];
			return nova;
		}
		return "";
	}
	public void round(){
		for(int i = 0; i < terms.length; i++){
			if(terms[i] < 0.00000000000001 && terms[i] > -0.00000000000001){
				terms[i] = 0;
			}
		}
	}
	public void divideAll(double divider){
		for(int i = 0; i < terms.length; i++){
			terms[i] /= divider;
		}
	}
	public void eliminate(Equation destroy, int which){
		double thisTerm = getTerm(which);
		double thatTerm = destroy.getTerm(which);
		double addition = -1 * thatTerm / thisTerm;
		destroy.add(addition, this);
	}
	public int numTerms(){
		return terms.length - 1;
	}
}
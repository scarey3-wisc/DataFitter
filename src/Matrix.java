public class Matrix{
	public static double[] solveMatrix(Equation[] given){
		for(int i = 0; i < given.length; i++){
			Equation penguin = given[i];
			for(int j = 0; j < given.length; j++){
				Equation penguin2 = given[j];
				if(penguin != penguin2){
					penguin.eliminate(penguin2, i);
				}
			}
			penguin.divideAll(penguin.getTerm(i));
		}
		double[] terms = new double[given.length];
		for(int i = 0; i < given.length; i++){
			terms[i] = given[i].getTerm(given.length);
		}
		return terms;
	}
	public static void printMatrix(Equation[] given){
		
		for(int i = 0; i < given.length; i++){
			System.out.println(given[i].print());
		}
		System.out.println();
		System.out.println();
	}
	public static void round(Equation[] given){
		for(int i = 0; i < given.length; i++){
			given[i].round();
		}
	}
}
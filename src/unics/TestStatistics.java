package unics;

import java.util.concurrent.ThreadLocalRandom;

import unics.Enum.CardType;
import unics.Enum.Keyword;

public class TestStatistics {

	public TestStatistics() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		//Valeur moyenne des Motclé
		int total=0;
		for(Keyword kw : Keyword.values()) {
			total += kw.getWeight();
		}
		System.out.println("KW : "+total/Keyword.values().length);
		//Valeur moyenne des effets
		total=0;
		int min=1000;
		int max=0;
	for(int cout=1;cout<7;cout++) {
		for(int i=0;i<1000;i++) {
			int rp=(int)CardEffect.generateRandomEffect(CardType.UNIT,cout,ThreadLocalRandom.current(),FactionProfileRegistry.neutral()).computeRawPower();
			if (rp<min)min=rp;
			if (rp>max)max=rp;
			
			total+= rp;
			}	
			System.out.println(cout+" : moy("+total/1000+") min("+min+") max("+max+")");
		total=0;
		min=1000;
		max=0;
	}
	}

}

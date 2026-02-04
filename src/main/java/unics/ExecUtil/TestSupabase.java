package unics.ExecUtil;

import dbPG18.JdbcCardSnapshotDao;
import unics.snapshot.CardSnapshot;

public class TestSupabase {
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
        try {

            JdbcCardSnapshotDao dao =
                new JdbcCardSnapshotDao();

            try {
                CardSnapshot row =
                    dao.findBySignature("V=1;TYPE=UNIT;FACTION=NOMAD;COST=1;ATK=2;DEF=2;KEYWORDS=mobile;EFFECTS=ON_MOVE:NONE:DAMAGE_UNIT_ENEMY:1:ENEMY:COST_1_OR_LESS,FACTION_MECHANICAL");

                if (row == null) {
                    System.out.println("Carte non trouvée");
                    return;
                }
                else {
                	System.out.println("Carte  trouvée");
                }
                
            } finally {
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

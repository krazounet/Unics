package unics.ExecUtil;

import dbPG18.CardDbRow;
import dbPG18.DbUtil;
import dbPG18.JdbcCardDao;
import unics.Card;
public class CardReadTest {

    public static void main(String[] args) {

        try {

            JdbcCardDao dao =
                new JdbcCardDao(DbUtil.getConnection());

            try {
                CardDbRow row =
                    dao.findRowByPublicId("6M6TNA");

                if (row == null) {
                    System.out.println("Carte non trouvée");
                    return;
                }

                Card rebuilt =
                    dao.rebuildCard(row);

                System.out.println("Rebuilt identity = " + rebuilt.getIdentity());
                System.out.println("DB identity      = " + row.identityHash);

                if (!rebuilt.getIdentity().toString().equals(row.identityHash)) {
                    throw new IllegalStateException("IDENTITY MISMATCH !");
                }

                System.out.println("✅ Identity match !");

                System.out.println("=== CARD FROM DB ===");
                System.out.println(rebuilt.getIdentity().debugSignature());
                System.out.println("id           = " + row.id);
                System.out.println("identityHash = " + row.identityHash);
                System.out.println("publicId     = " + row.publicId);
                System.out.println("name         = " + row.name);
                System.out.println("type         = " + row.cardType);
                System.out.println("faction      = " + row.faction);
                System.out.println("energyCost   = " + row.energyCost);
                System.out.println("attack       = " + row.attack);
                System.out.println("defense      = " + row.defense);
                System.out.println("powerScore   = " + row.powerScore);
                System.out.println("kw           = " + rebuilt.getKeywords());
                System.out.println("eff          = " + rebuilt.getEffects());

            } finally {
                // 🔒 fermeture explicite (soft change)
                dao.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
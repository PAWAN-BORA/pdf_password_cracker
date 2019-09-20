import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class PasswordCracker {
    private static Boolean done = false;
    // For all possible combination add alphabet in lowercase and uppercase and all possible symbols in the character string.
    private static String[] character ={"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private static int totalCheck = 0;
    private static String[] tempPass;
    private static File file;
    private static String correctPassword = "";
    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();
        System.out.println("Trying to get the password");
        file = new File("path of your pdf file");
        for(int i=1; i<=8; i++) {
            if(done) {
                break;
            }
            tempPass  = new String[i];
            makeLoop(i-1, 0);
        }
        System.out.println("total Password checked: " +totalCheck);
        if(done) {
            System.out.println("Password found: "+ correctPassword);
        } else {
            System.out.println("Password not found!");
        }
        Instant finish = Instant.now();
        System.out.println("total Time: "+Duration.between(start, finish).toSeconds() +" seconds");
    }

    private static void makeLoop(int size, int index)  {
        int totalLength = character.length;
        if(size==index) {
            for (String s : character) {
                tempPass[index] = s;
                StringBuilder pass = new StringBuilder();
                for (String value : tempPass) {
                    pass.append(value);
                }
                try {
                    PDDocument document = PDDocument.load(file, pass.toString());
                    done = true;
                    System.out.println("correct password: " + pass);
                    document.close();
                    correctPassword = pass.toString();
                    return;
                } catch (IOException e) {
                    System.out.println("wrong password: " + pass);
                }

                totalCheck++;
            }
            return;
        }
        for (String s : character) {
            if(done) {
                return;
            }
            tempPass[index] = s;
            makeLoop(size, index + 1);
        }
    }
    private static void lockFile(File file) throws IOException {
        String fileName = "newPdf.pdf";
        PDDocument document = PDDocument.load(file, "1234");
        AccessPermission accessPermission = new AccessPermission();
        StandardProtectionPolicy spp = new StandardProtectionPolicy("1052","1043",accessPermission);
        spp.setEncryptionKeyLength(128);
        spp.setPermissions(accessPermission);
        document.protect(spp);
        document.save(fileName);
        document.close();
    }

}


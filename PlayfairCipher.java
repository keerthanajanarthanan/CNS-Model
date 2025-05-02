import java.util.Scanner;
public class PlayfairCipher {

    private char[][] keyMatrix;

    public PlayfairCipher(String key) {
        keyMatrix = generateKeyMatrix(key);
    }

    private char[][] generateKeyMatrix(String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder keyBuilder = new StringBuilder(key);
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c != 'J' && keyBuilder.indexOf(String.valueOf(c)) == -1) {
                keyBuilder.append(c);
            }
        }
        String preparedKey = keyBuilder.toString();
        char[][] matrix = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = preparedKey.charAt(i * 5 + j);
            }
        }
        return matrix;
    }

    private int[] findChar(char c) {
        c = (c == 'J') ? 'I' : Character.toUpperCase(c);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyMatrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null; 
    }

    public String encrypt(String plaintext) {
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder preparedText = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            char first = plaintext.charAt(i);
            char second = (i + 1 < plaintext.length()) ? plaintext.charAt(i + 1) : 'X';
            if (first == second) {
                second = 'X';
                i--; // Adjust index for next pair
            }
            preparedText.append(first).append(second);
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < preparedText.length(); i += 2) {
            int[] firstPos = findChar(preparedText.charAt(i));
            int[] secondPos = findChar(preparedText.charAt(i + 1));
            if (firstPos[0] == secondPos[0]) { // Same row
                ciphertext.append(keyMatrix[firstPos[0]][(firstPos[1] + 1) % 5]);
                ciphertext.append(keyMatrix[secondPos[0]][(secondPos[1] + 1) % 5]);
            } else if (firstPos[1] == secondPos[1]) { // Same column
                ciphertext.append(keyMatrix[(firstPos[0] + 1) % 5][firstPos[1]]);
                ciphertext.append(keyMatrix[(secondPos[0] + 1) % 5][secondPos[1]]);
            } else { // Rectangle
                ciphertext.append(keyMatrix[firstPos[0]][secondPos[1]]);
                ciphertext.append(keyMatrix[secondPos[0]][firstPos[1]]);
            }
        }
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            int[] firstPos = findChar(ciphertext.charAt(i));
            int[] secondPos = findChar(ciphertext.charAt(i + 1));
            if (firstPos[0] == secondPos[0]) { // Same row
                plaintext.append(keyMatrix[firstPos[0]][(firstPos[1] + 4) % 5]);
                plaintext.append(keyMatrix[secondPos[0]][(secondPos[1] + 4) % 5]);
            } else if (firstPos[1] == secondPos[1]) { // Same column
                plaintext.append(keyMatrix[(firstPos[0] + 4) % 5][firstPos[1]]);
                plaintext.append(keyMatrix[(secondPos[0] + 4) % 5][secondPos[1]]);
            } else { // Rectangle
                plaintext.append(keyMatrix[firstPos[0]][secondPos[1]]);
                plaintext.append(keyMatrix[secondPos[0]][firstPos[1]]);
            }
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the key: ");
        String key = scanner.nextLine();
        PlayfairCipher playfair = new PlayfairCipher(key);

        System.out.print("Enter plaintext: ");
        String plaintext = scanner.nextLine();
        String ciphertext = playfair.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);

        System.out.print("Enter ciphertext to decrypt: ");
        String decryptText = scanner.nextLine();
        String decryptedPlaintext = playfair.decrypt(decryptText);
        System.out.println("Decrypted plaintext: " + decryptedPlaintext);

        scanner.close();
    }
}


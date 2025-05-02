import java.util.Scanner;

public class HillCipher {

    private int[][] keyMatrix;
    private int matrixSize;

    public HillCipher(int[][] keyMatrix) {
        this.keyMatrix = keyMatrix;
        this.matrixSize = keyMatrix.length;
    }

    public String encrypt(String plaintext) {
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");
        if (plaintext.length() % matrixSize != 0) {
            int padding = matrixSize - (plaintext.length() % matrixSize);
            for (int i = 0; i < padding; i++) {
                plaintext += 'X'; // Pad with 'X'
            }
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += matrixSize) {
            int[] plaintextVector = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                plaintextVector[j] = plaintext.charAt(i + j) - 'A';
            }

            int[] ciphertextVector = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    ciphertextVector[j] += keyMatrix[j][k] * plaintextVector[k];
                }
                ciphertextVector[j] %= 26;
            }

            for (int j = 0; j < matrixSize; j++) {
                ciphertext.append((char) (ciphertextVector[j] + 'A'));
            }
        }
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext) {

        int[][] inverseKey = matrixInverse(keyMatrix);
        if (inverseKey == null) {
            return "Key matrix is not invertible.";
        }

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += matrixSize) {
            int[] ciphertextVector = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                ciphertextVector[j] = ciphertext.charAt(i + j) - 'A';
            }

            int[] plaintextVector = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    plaintextVector[j] += inverseKey[j][k] * ciphertextVector[k];
                }
                plaintextVector[j] = (plaintextVector[j] % 26 + 26) % 26; // Ensure positive modulo
            }

            for (int j = 0; j < matrixSize; j++) {
                plaintext.append((char) (plaintextVector[j] + 'A'));
            }
        }
        return plaintext.toString();
    }

    private int[][] matrixInverse(int[][] matrix) {
        int determinant = determinant(matrix);
        int detInverse = modularInverse(determinant, 26);

        if (detInverse == 0) {
            return null; // Matrix not invertible
        }

        int[][] adjugate = adjugate(matrix);
        int[][] inverse = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                inverse[i][j] = (adjugate[i][j] * detInverse) % 26;
                if(inverse[i][j] <0) inverse[i][j] +=26; //ensure positive modulo
            }
        }

        return inverse;
    }

    private int determinant(int[][] matrix) {
        if (matrixSize == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        int det = 0;
        for (int i = 0; i < matrixSize; i++) {
            det += matrix[0][i] * cofactor(matrix, 0, i);
        }
        return det;
    }

    private int cofactor(int[][] matrix, int row, int col) {
        int[][] subMatrix = new int[matrixSize - 1][matrixSize - 1];
        int subRow = 0, subCol;
        for (int i = 0; i < matrixSize; i++) {
            if (i == row) continue;
            subCol = 0;
            for (int j = 0; j < matrixSize; j++) {
                if (j == col) continue;
                subMatrix[subRow][subCol] = matrix[i][j];
                subCol++;
            }
            subRow++;
        }
        return (int) (Math.pow(-1, row + col) * determinant(subMatrix));
    }

    private int[][] adjugate(int[][] matrix) {
        int[][] adj = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                adj[i][j] = cofactor(matrix, j, i);
            }
        }
        return adj;
    }

    private int modularInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 0; // No modular inverse
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the size of the key matrix (e.g., 2 for 2x2): ");
        int matrixSize = scanner.nextInt();
        int[][] keyMatrix = new int[matrixSize][matrixSize];

        System.out.println("Enter the key matrix (numbers, space-separated):");
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                keyMatrix[i][j] = scanner.nextInt();
            }
        }

        scanner.nextLine(); // Consume newline

        HillCipher hillCipher = new HillCipher(keyMatrix);

        System.out.print("Enter the plaintext: ");
        String plaintext = scanner.nextLine();
        String ciphertext = hillCipher.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);

        System.out.print("Enter the ciphertext to decrypt: ");
        String decryptText = scanner.nextLine();
        String decryptedPlaintext = hillCipher.decrypt(decryptText);
        System.out.println("Decrypted plaintext: " + decryptedPlaintext);

        scanner.close();
    }
}
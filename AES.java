import java.io.*;

public class AES {

    /*Our program encrypts files using AES-128 (10 rounds)*/
    private static int[] sbox = {
            0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
            0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
            0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
            0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
            0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
            0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
            0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
            0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
            0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
            0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
            0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
            0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
            0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
            0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
            0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
            0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16};

    private static int[] inv_sbox = {
            0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB,
            0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB,
            0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E,
            0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25,
            0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92,
            0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84,
            0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06,
            0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
            0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73,
            0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E,
            0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B,
            0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4,
            0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F,
            0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF,
            0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61,
            0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D};

    private static int[][] roundkey;

    public static void main(String[] args) {
/*        if (!aes.cmdLine_valid(args)) {
            System.out.println("Please rerun program with a valid command");
            return;
        }

        String process = args[0].toLowerCase();
        if(process.equals("e"))
        {*/

            AES aes = new AES();
            int [][] input = {{0x32, 0x88, 0x31, 0xE0},
                              {0x43, 0x5A, 0x31, 0x37},
                              {0xF6, 0x30, 0x98, 0x07},
                              {0xA8, 0x8D, 0xA2, 0x34}};

            int[][] cipherkey ={{0x2B, 0x28, 0xAB, 0x09},
                                {0x7E, 0xAE, 0xF7, 0xCF},
                                {0x15, 0xD2, 0x15, 0x4F},
                                {0x16, 0xA6, 0x88, 0x3C}};
            roundkey = cipherkey;


            //ONLY FOR ONE BLOCK OF MEMORY, MUST PROCESS EVERY CHUNK

            //initial round 0
            int[][] state = aes.addRoundKey(input, cipherkey);
            printState(state);
            //rounds 1-9
            for(int round = 1; round < 10; round++)
            {
                        System.out.println("Round " + round+ " starting");

                aes.keyExpansion(roundkey, round);
                state = aes.subBytes(state);
                printState(state);
                state = aes.shiftRows(state);
                                printState(state);

                state = aes.mixColumns(state);
                                printState(state);

                state = aes.addRoundKey(state, roundkey);
                                printState(state);

            }

            //final round
            aes.keyExpansion(roundkey, 10);
            state = aes.subBytes(state);
                            printState(state);

            state = aes.shiftRows(state);
                            printState(state);

            state = aes.addRoundKey(state, roundkey); 
                            printState(state);
           
      //  }

    }

    private static void printState(int[][] state)
    {
        for(int row = 0; row < state.length; row++)
            {
                for(int col = 0; col < state[0].length; col++)
                {
                    System.out.print(Integer.toHexString(state[row][col])+" ");
                }
                System.out.println();
            }
            System.out.println();
    }


    /* Used the following as reference: 
    https://engineering.purdue.edu/kak/compsec/NewLectures/Lecture8.pdf
    Key is 128 bits or 32 bytes -> 32 hex values in the form -> 16 k values
    k0  k4 k8  k12
    k1  k5 k9  k13
    k2  k6 k10 k14 
    k3  k7 k11 k15
    |   |   |   |
    V   V   V   V
    w0  w1  w2  w3
    */
    private void keyExpansion(int[][] key, int round)
    {
        //XOR the first column in key with g(last column)
        for(int col = 0; col < 4; col++)
        {
            int[] newcolumn;
            if(col == 0)
                newcolumn = arrXOR(g(key, 3, round), getColumn(key, 0));
            else
                newcolumn = arrXOR(getColumn(key, col-1), getColumn(key, col));

            for(int row = 0; row < 4; row++)
            {
                key[row][col] = newcolumn[row];
            }               
        }


    }


    private int[] getColumn(int[][] matrix, int col)
    {
        int[] column = new int[matrix.length];
        for(int row = 0; row < matrix.length; row++)
        {
            column[row] = matrix[row][col];
        }
        return column;
    }

    /*XORs all the elements with each other, assumes a size 4 array*/
    private int[] arrXOR(int[] arr1, int[] arr2)
    {
        for(int index = 0; index < arr1.length; index++)
        {
            arr1[index] = arr1[index] ^ arr2[index];
        }
        return arr1;
    }
    /*input the key, column to be processed, and the round number*/
    private int[] g(int[][] key, int col, int round)
    {
        int[] output = new int[4];

        /*Rotate word by left circular rotation*/
        for(int row = 0; row < key.length; row++)
        {
            if(row < key.length-1)
                output[row] = key[row+1][col];
            else
                output[row] = key[0][col];
        }

        /*Perform byte susbstitution the exact same manner done in subBytes*/
        for(int row = 0; row < output.length; row++)
        {
            output[row] = accessSbox(output[row]);
        }

        /* Perform round constant XOR */
        int[] rcon = new int[4];
        rcon[0] = roundConstant(round);
        rcon[1] = 0;
        rcon[2] = 0;
        rcon[3] = 0;

        for(int row = 0; row < 4; row++)
        {
            output[row] = output[row] ^ rcon[row];
        }


        return output;
    }

    private int roundConstant(int round)
    {
        if(round == 1)
            return 1;
        return 0x02*roundConstant(round-1);
    }

    /*For each byte in the array, use its value as an index into a fixed 256-element lookup table, 
      and replace its value in the state by the byte value stored at that location in the table.*/  
    private int[][] subBytes(int[][] input)
    {
        for(int row = 0; row < input.length; row++)
        {
            for(int col = 0; col < input[0].length; col++)
            {
                input[row][col] = accessSbox(input[row][col]);
            }
        }
        return input;
    } 

    /* Let Ri denote the ith row in state. Shift R0 in the state left 0 bytes (i.e., no change); 
       shift R1 left 1 byte; shift R2 left 2 bytes; shift R3 left 3 bytes. These are circular shifts. 
       They do not affect the individual byte values themselves.*/
    private int[][] shiftRows(int[][] input)
    {
        //Create a new 4x4 array with the same size as the input array
        int[][] result = new int[input.length][input[0].length];

        for(int row = 0; row < input.length; row++)
        {
            for(int col = 0; col < input[0].length; col++)
            {
                /*We shift all values in a row n times where n is the row number.*/
                    int newcol = col - row;
                    if(newcol<0)
                        newcol += input.length;
                    result[row][newcol] = input[row][col];
            }
        }

        return result;
    } 
    
    /*For each column of the state, replace the column by its value multiplied 
      by a fixed 4 x 4 matrix of integers (in a particular Galois Field). This is 
      the most complex step. Find details at any of several websites. Note that 
      the inverse operation multiplies by a different matrix.*/
    private int[][] mixColumns(int[][] input)
    {
        //input should be a 4x1 matrix
        int[][] result = new int[input.length][input[0].length];
        for(int col = 0; col < input[0].length; col++)
        {
            result[0][col] = compute(2, input[0][col]) + compute(3,input[1][col]) + compute(input[2][col],1) + compute(input[3][col],1);
            result[1][col] = compute(1, input[0][col]) + compute(2, input[1][col]) + compute(3,input[2][col]) + compute(input[3][col],1);
            result[2][col] = compute(input[0][col],1) + compute(input[1][col],1) + compute(2,input[2][col]) + compute(3,input[3][col]);
            result[3][col] = compute(3,input[0][col]) + compute(input[1][col],1) + compute(input[2][col],1) + compute(2,input[3][col]);

        }
        return result; 
    }

    private int compute(int x, int y)
    {
        int result = x*y;
        if(result > 0xFF)
            result ^= 0x1B;
        return result;
    }
    /*XOR the state with a 128-bit round key derived from the original key K by
      a recursive process.*/
    private int[][] addRoundKey(int[][] input, int[][] key)
    {
        int[][] result = new int[input.length][input[0].length];
        for(int row = 0; row < input.length; row++)
        {
            for(int col = 0; col < input[0].length; col++)
            {
                result[row][col] = input[row][col] ^ key[row][col];
            }
        }
        return result;
    }


    private boolean cmdLine_valid(String[] args) {
        String process = args[0].toLowerCase();
        if (args.length != 3) {
            System.out.println("Too many arguments");
            return false;
        }

        if (!process.equals("d") && !process.equals("e")) {
            System.out.printf("%s is not a valid option\n",args[0]);
            return false;
        }

        if (process.equals("d")) {
            String[] file = args[2].split(".");
            if (file.length != 2) {
                System.out.printf("%s is an invalid encrypted file\n", args[2]);
                return false;
            }
            if (!file[1].equals(".enc")) {
                System.out.printf("%s is not a valid file extension\n", file[1]);
                return false;
            }
        }

        if (process.equals("e")) {
            String[] file = args[2].split(".");
            if (file.length > 1) {
                System.out.println("Please remove file extension from plaintext file");
                return false;
            }
        }

        return true;

    }

    private int accessSbox(int hex)
    {
            int leftdigit = (hex & 0x00F0)>>>4;
            int rightdigit = hex & 0x000F;
            return sbox[leftdigit*16+rightdigit];
    }

}
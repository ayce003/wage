package com.ycjd.payslip.util;

import java.io.*;

public class BASE64Encoder extends CharacterEncoder{



    public BASE64Encoder() {
    }


    protected int bytesPerAtom() {
        return 3;
    }


    protected int bytesPerLine() {
        return 57;
    }


    protected void encodeAtom(OutputStream outputstream, byte abyte0[], int i, int j) throws IOException {
        if (j == 1) {
            byte byte0 = abyte0[i];
            int k = 0;
            outputstream.write(PEM_ARRAY[byte0 >>> 2 & 0x3f]);
            outputstream.write(PEM_ARRAY[(byte0 << 4 & 0x30) + (k >>> 4 & 0xf)]);
            outputstream.write(61);
            outputstream.write(61);
        } else if (j == 2) {
            byte byte1 = abyte0[i];
            byte byte3 = abyte0[i + 1];
            int l = 0;
            outputstream.write(PEM_ARRAY[byte1 >>> 2 & 0x3f]);
            outputstream.write(PEM_ARRAY[(byte1 << 4 & 0x30) + (byte3 >>> 4 & 0xf)]);
            outputstream.write(PEM_ARRAY[(byte3 << 2 & 0x3c) + (l >>> 6 & 3)]);
            outputstream.write(61);
        } else {
            byte byte2 = abyte0[i];
            byte byte4 = abyte0[i + 1];
            byte byte5 = abyte0[i + 2];
            outputstream.write(PEM_ARRAY[byte2 >>> 2 & 0x3f]);
            outputstream.write(PEM_ARRAY[(byte2 << 4 & 0x30) + (byte4 >>> 4 & 0xf)]);
            outputstream.write(PEM_ARRAY[(byte4 << 2 & 0x3c) + (byte5 >>> 6 & 3)]);
            outputstream.write(PEM_ARRAY[byte5 & 0x3f]);
        }
    }


    private final static char PEM_ARRAY[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/' };


}






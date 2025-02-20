/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers;

import org.opencv.core.CvType;
import org.opencv.core.MatOfByte;

/**
 * Wrapps {@link org.opencv.core.MatOfByte}
 *
 * @author Bianca Leopoldo Ramos
 */
public final class MatOfBytesWrapper extends MatOfByte {

    // 8UC(x)
    private static final int DEPTH = CvType.CV_8U;
    private static final int CHANNELS = 1;
    private byte[] arrayMat;

    public MatOfBytesWrapper() {
    }

    public MatOfBytesWrapper(byte[] arrayMat) {
        this.arrayMat = arrayMat;
    }

    public byte[] getArrayMat() {
        return arrayMat;
    }

    public void setArrayMat(byte[] arrayMat) {
        this.arrayMat = arrayMat;
    }

    /**
     * Transforms a Mat of bytes in an array.
     *
     * @return
     */
    @Override
    public byte[] toArray() {
        int value = checkVector(CHANNELS, DEPTH);
        if (value < 0) {
            throw new RuntimeException("Native Mat has unexpected type or size: " + toString());
        }
        byte[] b = new byte[value * CHANNELS];
        if (value == 0) {
            return b;
        }
        get(0, 0, b);
        this.arrayMat = b;
        return arrayMat;
    }

}

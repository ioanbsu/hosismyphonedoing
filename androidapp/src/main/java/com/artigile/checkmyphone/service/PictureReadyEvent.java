package com.artigile.checkmyphone.service;

/**
 * User: ioanbsu
 * Date: 7/17/13
 * Time: 12:41 PM
 */
public class PictureReadyEvent {

    private byte[] pictureBytes;

    public PictureReadyEvent(byte[] pictureBytes) {
        this.pictureBytes = pictureBytes;
    }

    public byte[] getPictureBytes() {
        return pictureBytes;
    }
}

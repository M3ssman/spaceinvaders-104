package org.newdawn.spaceinvaders;


/**
 * A wrapper class that provides timing methods.
 *
 * @author Kevin Glass
 */
public class SystemTimer {

    /**
     * Get the time in milliseconds
     *
     * @return Time in milliseconds
     */
    public static long getTime() {
        return (System.nanoTime() / 1_000_000);
    }

}
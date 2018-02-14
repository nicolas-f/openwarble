package org.noise_planet.openwarble;

import org.junit.Test;

import org.renjin.gcc.runtime.FloatPtr;
import org.renjin.gcc.runtime.Ptr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example of using a compiled C function from Java
 */
public class YinACF_Test {

    @Test
    public void core1khzTest() {
        Ptr yin = yinacf.create();


        // Make 1000 Hz signal
        final int sampleRate = 44100;
        final float minimalFrequency = 100;
        final int window = (int)Math.ceil(sampleRate / minimalFrequency);
        final int signalFrequency = 1000;
        double powerRMS = 2500; // 90 dBspl
        double powerPeak = powerRMS * Math.sqrt(2);
        float[] signal = new float[sampleRate];
        for (int s = 0; s < signal.length; s++) {
            double t = s * (1 / (double) sampleRate);
            signal[s] = (float)(Math.sin(2 * Math.PI * signalFrequency * t) * (powerPeak));
        }

        assertTrue(yinacf.build(yin, window, window));

        int latency = yinacf.getLatency(yin);
        for(int i=0; i < latency; i++) {
            yinacf.tick(yin, signal[i]);
        }
        assertEquals(signalFrequency, yinacf.getFrequency(yin, 0) * sampleRate, 1);
    }

}

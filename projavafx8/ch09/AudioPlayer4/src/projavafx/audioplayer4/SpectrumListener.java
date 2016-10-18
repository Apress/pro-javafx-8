/*
 * Copyright (c) 2011, Pro JavaFX Authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JFXtras nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *  Class.java - An example from the Pro JavaFX Platform book.
 */
package projavafx.audioplayer4;

import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;

/**
 * @author dean
 */
class SpectrumListener implements AudioSpectrumListener {
  private final SpectrumBar[] bars;
  private double minValue;
  private double[] norms;
  private int[] spectrumBucketCounts;

  SpectrumListener(double startFreq, MediaPlayer mp, SpectrumBar[] bars) {
    this.bars = bars;
    this.minValue = mp.getAudioSpectrumThreshold();
    this.norms = createNormArray();
    
    int bandCount = mp.getAudioSpectrumNumBands();
    this.spectrumBucketCounts = createBucketCounts(startFreq, bandCount);
  }

  @Override
  public void spectrumDataUpdate(double timestamp, double duration, 
                                 float[] magnitudes, float[] phases) {
    int index = 0;
    int bucketIndex = 0;
    int currentBucketCount = 0;
    double sum = 0.0;
    
    while (index < magnitudes.length) {
      sum += magnitudes[index] - minValue;
      ++currentBucketCount;
      
      if (currentBucketCount >= spectrumBucketCounts[bucketIndex]) {
        bars[bucketIndex].setValue(sum / norms[bucketIndex]);
        currentBucketCount = 0;
        sum = 0.0;
        ++bucketIndex;
      }
      
      ++index;
    }
  }

  private double[] createNormArray() {
    double[] normArray = new double[bars.length];
    double currentNorm = 0.05;
    for (int i = 0; i < normArray.length; i++) {
      normArray[i] = 1 + currentNorm;
      currentNorm *= 2;
    }
    return normArray;
  }

  private int[] createBucketCounts(double startFreq, int bandCount) {
    int[] bucketCounts = new int[bars.length];
    
    double bandwidth = 22050.0 / bandCount;
    double centerFreq = bandwidth / 2;
    double currentSpectrumFreq = centerFreq;
    double currentEQFreq = startFreq / 2;
    double currentCutoff = 0;
    int currentBucketIndex = -1;
    
    for (int i = 0; i < bandCount; i++) {
      if (currentSpectrumFreq > currentCutoff) {
        currentEQFreq *= 2;
        currentCutoff = currentEQFreq + currentEQFreq / 2;
        ++currentBucketIndex;
        if (currentBucketIndex == bucketCounts.length) {
          break;
        }
      }
      
      ++bucketCounts[currentBucketIndex];
      currentSpectrumFreq += bandwidth;
    }
    
    return bucketCounts;
  }
}

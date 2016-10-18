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
 */
package projavafx.reversi.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class ReversiModel {
  public static int BOARD_SIZE = 8;
  
  public ObjectProperty<Owner> turn = new SimpleObjectProperty<Owner>(Owner.BLACK);
  
  public ObjectProperty<Owner>[][] board = new ObjectProperty[BOARD_SIZE][BOARD_SIZE];
  
  private ReversiModel() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        board[i][j] = new SimpleObjectProperty<Owner>(Owner.NONE);
      }
    }
    initBoard();
  }
  
  private void initBoard() {
    int center1 = BOARD_SIZE / 2 - 1;
    int center2 = BOARD_SIZE /2;
    board[center1][center1].setValue(Owner.WHITE);
    board[center1][center2].setValue(Owner.BLACK);
    board[center2][center1].setValue(Owner.BLACK);
    board[center2][center2].setValue(Owner.WHITE);
  }
  
  public NumberExpression getScore(Owner owner) {
    NumberExpression score = new SimpleIntegerProperty();
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        score = score.add(Bindings.when(board[i][j].isEqualTo(owner)).then(1).otherwise(0));
      }
    }
    return score;
  }
  
  public NumberBinding getTurnsRemaining(Owner owner) {
    NumberExpression emptyCellCount = getScore(Owner.NONE);
    return Bindings.when(turn.isEqualTo(owner))
      .then(emptyCellCount.add(1).divide(2))
      .otherwise(emptyCellCount.divide(2));
  }
  
  public static ReversiModel getInstance() {
    return ReversiModelHolder.INSTANCE;
  }
  
  private static class ReversiModelHolder {
    private static final ReversiModel INSTANCE = new ReversiModel();
  }
}

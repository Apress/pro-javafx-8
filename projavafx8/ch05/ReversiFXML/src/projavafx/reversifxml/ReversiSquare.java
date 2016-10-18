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
package projavafx.reversifxml;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import projavafx.reversi.model.ReversiModel;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class ReversiSquare extends Region {

    private final Region highlight;
    private final FadeTransition highlightTransition;

    private static final ReversiModel model = ReversiModel.getInstance();

    public ReversiSquare(final int x, final int y) {
        highlight = new Region();
        highlight.setOpacity(0);
        highlight.setStyle("-fx-border-width: 3; -fx-border-color: dodgerblue");

        highlightTransition = new FadeTransition(Duration.millis(200), highlight);
        highlightTransition.setFromValue(0);
        highlightTransition.setToValue(1);

        styleProperty().bind(Bindings.when(model.legalMove(x, y))
                .then("-fx-background-color: derive(dodgerblue, -60%)")
                .otherwise("-fx-background-color: burlywood"));
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135);
        light.setElevation(30);
        setEffect(new Lighting(light));
        setPrefSize(200, 200);
        getChildren().add(highlight);
        addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, t -> {
            if (model.legalMove(x, y).get()) {
                highlightTransition.setRate(1);
                highlightTransition.play();
            }
        });
        addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, t -> {
            highlightTransition.setRate(-1);
            highlightTransition.play();
        });
        setOnMouseClicked(t -> {
            model.play(x, y);
            highlightTransition.setRate(-1);
            highlightTransition.play();
        });
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(highlight, 0, 0, getWidth(), getHeight(), getBaselineOffset(), HPos.CENTER, VPos.CENTER);
    }
}

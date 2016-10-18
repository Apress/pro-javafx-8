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

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Region;
import projavafx.reversi.model.Owner;

/**
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class ReversiPiece extends Region {

    private final ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(this, "owner", Owner.NONE);

    public ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    public Owner getOwner() {
        return ownerProperty.get();
    }

    public void setOwner(Owner owner) {
        ownerProperty.set(owner);
    }

    public ReversiPiece() {
        styleProperty().bind(Bindings.when(ownerProperty.isEqualTo(Owner.NONE))
                .then("radius: 0")
                .otherwise(Bindings.when(ownerProperty.isEqualTo(Owner.WHITE))
                        .then("-fx-background-color: radial-gradient(radius 100%, white .4, gray .9, darkgray 1)")
                        .otherwise("-fx-background-color: radial-gradient(radius 100%, white 0, black .6)"))
                .concat("; -fx-background-radius: 1000em; -fx-background-insets: 5"));
        Reflection reflection = new Reflection();
        reflection.setFraction(1);
        reflection.topOffsetProperty().bind(heightProperty().multiply(-.75));
        setEffect(reflection);
        setPrefSize(180, 180);
        setMouseTransparent(true);
    }

    public ReversiPiece(Owner owner) {
        this();
        ownerProperty.setValue(owner);
    }
}

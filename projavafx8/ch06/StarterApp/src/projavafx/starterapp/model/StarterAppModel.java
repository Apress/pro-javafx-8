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
 * StarterAppModel.java - The model class behind the StarterApp JavaFX example 
 * program that demonstrates UI controls in the javafx.scene.controls package.
 *
 *  Developed 2011 by James L. Weaver jim.weaver [at] javafxpert.com
 *  as a JavaFX SDK 2.0 example for the Pro JavaFX book.
 */
package projavafx.starterapp.model;

import java.util.Random;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class StarterAppModel {

    public ObservableList getTeamMembers() {
        ObservableList teamMembers = FXCollections.observableArrayList();
        for (int i = 1; i <= 10000; i++) {
            teamMembers.add(new Person("FirstName" + i,
                    "LastName" + i,
                    "Phone" + i));
        }
        return teamMembers;
    }

    public TreeItem<Person> getFamilyTree() {
        Random random = new Random();
        TreeItem<Person> root = new TreeItem();
        for (int i = 0; i < 5; i++) {
            Person parent = new Person("Parent " + i, "LastName" + i, "Phone" + i);
            TreeItem<Person> parentItem = new TreeItem(parent);
            for (int j = 0; j < random.nextInt(4); j++) {
                Person child = new Person("Child " + i + "-" + j, "LastName" + i, "Phone" + j);
                TreeItem<Person> childItem = new TreeItem(child);
                parentItem.getChildren().add(childItem);
                for (int k = 0; k < random.nextInt(4); k++) {
                    Person grandChild = new Person("Grandchild " + i + "-" + j + "-" + k, "LastName" + i, "Phone" + k);
                    TreeItem<Person> grandChildItem = new TreeItem(grandChild);
                    childItem.getChildren().add(grandChildItem);
                }
            }
            root.getChildren().add(parentItem);
        }
        return root;
    }

    public String getRandomWebSite() {
        String[] webSites = {
            "http://javafx.com",
            "http://fxexperience.com",
            "http://steveonjava.com",
            "http://javafxpert.com",
            "http://pleasingsoftware.blogspot.com",
            "http://www.weiqigao.com/blog",
            "http://blogs.lodgon.com/johan",
            "http://google.com"
        };
        int randomIdx = (int) (Math.random() * webSites.length);
        return webSites[randomIdx];
    }

    public ObservableList listViewItems = FXCollections.observableArrayList();

    public ObservableList choiceBoxItems = FXCollections.observableArrayList(
            "Choice A",
            "Choice B",
            "Choice C",
            "Choice D"
    );

    public double maxRpm = 8000.0;
    public DoubleProperty rpm = new SimpleDoubleProperty(0);

    public double maxKph = 300.0;
    public DoubleProperty kph = new SimpleDoubleProperty(0);
}

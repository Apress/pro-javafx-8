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
 * Person.java - The class whose instances hold a Person for the TableView.
 *
 *  Developed 2011 by James L. Weaver jim.weaver [at] javafxpert.com
 *  as a JavaFX SDK 2.0 example for the Pro JavaFX book.
 */
package projavafx.starterapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Person {
  private StringProperty firstName;
  public void setFirstName(String value) { firstNameProperty().set(value); }
  public String getFirstName() { return firstNameProperty().get(); }
  public StringProperty firstNameProperty() { 
    if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
    return firstName; 
  }

  private StringProperty lastName;
  public void setLastName(String value) { lastNameProperty().set(value); }
  public String getLastName() { return lastNameProperty().get(); }
  public StringProperty lastNameProperty() { 
    if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
    return lastName; 
  } 

  private StringProperty phone;
  public void setPhone(String value) { phoneProperty().set(value); }
  public String getPhone() { return phoneProperty().get(); }
  public StringProperty phoneProperty() { 
    if (phone == null) phone = new SimpleStringProperty(this, "phone");
    return phone; 
  } 

  public Person(String firstName, String lastName, String phone) {
    setFirstName(firstName);
    setLastName(lastName);
    setPhone(phone);
  }

  @Override
  public String toString() {
    return "Person: " + firstName.getValue() + " " + lastName.getValue();
  }
}
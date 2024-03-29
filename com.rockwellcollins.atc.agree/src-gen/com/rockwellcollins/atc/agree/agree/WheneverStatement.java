/**
 * Copyright (c) 2023, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
 * including any software or models in source or binary form, as well as any drawings, specifications, 
 * and documentation (collectively "the Data"), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Data.
 * 
 * THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 * 
 * Generated by Xtext version 2.30.0.
 */
package com.rockwellcollins.atc.agree.agree;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Whenever Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.rockwellcollins.atc.agree.agree.WheneverStatement#getCause <em>Cause</em>}</li>
 *   <li>{@link com.rockwellcollins.atc.agree.agree.WheneverStatement#getExcl <em>Excl</em>}</li>
 *   <li>{@link com.rockwellcollins.atc.agree.agree.WheneverStatement#getInterval <em>Interval</em>}</li>
 * </ul>
 *
 * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getWheneverStatement()
 * @model
 * @generated
 */
public interface WheneverStatement extends PatternStatement
{
  /**
   * Returns the value of the '<em><b>Cause</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Cause</em>' containment reference.
   * @see #setCause(Expr)
   * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getWheneverStatement_Cause()
   * @model containment="true"
   * @generated
   */
  Expr getCause();

  /**
   * Sets the value of the '{@link com.rockwellcollins.atc.agree.agree.WheneverStatement#getCause <em>Cause</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Cause</em>' containment reference.
   * @see #getCause()
   * @generated
   */
  void setCause(Expr value);

  /**
   * Returns the value of the '<em><b>Excl</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Excl</em>' attribute.
   * @see #setExcl(String)
   * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getWheneverStatement_Excl()
   * @model
   * @generated
   */
  String getExcl();

  /**
   * Sets the value of the '{@link com.rockwellcollins.atc.agree.agree.WheneverStatement#getExcl <em>Excl</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Excl</em>' attribute.
   * @see #getExcl()
   * @generated
   */
  void setExcl(String value);

  /**
   * Returns the value of the '<em><b>Interval</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Interval</em>' containment reference.
   * @see #setInterval(TimeInterval)
   * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getWheneverStatement_Interval()
   * @model containment="true"
   * @generated
   */
  TimeInterval getInterval();

  /**
   * Sets the value of the '{@link com.rockwellcollins.atc.agree.agree.WheneverStatement#getInterval <em>Interval</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Interval</em>' containment reference.
   * @see #getInterval()
   * @generated
   */
  void setInterval(TimeInterval value);

} // WheneverStatement

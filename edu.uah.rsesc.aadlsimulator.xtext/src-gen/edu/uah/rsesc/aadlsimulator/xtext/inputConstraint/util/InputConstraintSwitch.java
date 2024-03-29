/**
 * Copyright (c) 2021, Collins Aerospace.
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
 *  *
 * Generated by Xtext version 2.20.0.
 */
package edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.util;

import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintPackage
 * @generated
 */
public class InputConstraintSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static InputConstraintPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InputConstraintSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = InputConstraintPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case InputConstraintPackage.INPUT_CONSTRAINT:
      {
        InputConstraint inputConstraint = (InputConstraint)theEObject;
        T result = caseInputConstraint(inputConstraint);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.EXPRESSION:
      {
        Expression expression = (Expression)theEObject;
        T result = caseExpression(expression);
        if (result == null) result = caseInputConstraint(expression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.SCALAR_EXPRESSION:
      {
        ScalarExpression scalarExpression = (ScalarExpression)theEObject;
        T result = caseScalarExpression(scalarExpression);
        if (result == null) result = caseExpression(scalarExpression);
        if (result == null) result = caseInputConstraint(scalarExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.RANDOM_EXPRESSION:
      {
        RandomExpression randomExpression = (RandomExpression)theEObject;
        T result = caseRandomExpression(randomExpression);
        if (result == null) result = caseScalarExpression(randomExpression);
        if (result == null) result = caseExpression(randomExpression);
        if (result == null) result = caseInputConstraint(randomExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.REF_EXPRESSION:
      {
        RefExpression refExpression = (RefExpression)theEObject;
        T result = caseRefExpression(refExpression);
        if (result == null) result = caseScalarExpression(refExpression);
        if (result == null) result = caseExpression(refExpression);
        if (result == null) result = caseInputConstraint(refExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.ELEMENT_REF_EXPRESSION:
      {
        ElementRefExpression elementRefExpression = (ElementRefExpression)theEObject;
        T result = caseElementRefExpression(elementRefExpression);
        if (result == null) result = caseRefExpression(elementRefExpression);
        if (result == null) result = caseScalarExpression(elementRefExpression);
        if (result == null) result = caseExpression(elementRefExpression);
        if (result == null) result = caseInputConstraint(elementRefExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.INTERVAL_EXPRESSION:
      {
        IntervalExpression intervalExpression = (IntervalExpression)theEObject;
        T result = caseIntervalExpression(intervalExpression);
        if (result == null) result = caseExpression(intervalExpression);
        if (result == null) result = caseInputConstraint(intervalExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.SET_EXPRESSION:
      {
        SetExpression setExpression = (SetExpression)theEObject;
        T result = caseSetExpression(setExpression);
        if (result == null) result = caseExpression(setExpression);
        if (result == null) result = caseInputConstraint(setExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.INTEGER_LITERAL:
      {
        IntegerLiteral integerLiteral = (IntegerLiteral)theEObject;
        T result = caseIntegerLiteral(integerLiteral);
        if (result == null) result = caseScalarExpression(integerLiteral);
        if (result == null) result = caseExpression(integerLiteral);
        if (result == null) result = caseInputConstraint(integerLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.REAL_LITERAL:
      {
        RealLiteral realLiteral = (RealLiteral)theEObject;
        T result = caseRealLiteral(realLiteral);
        if (result == null) result = caseScalarExpression(realLiteral);
        if (result == null) result = caseExpression(realLiteral);
        if (result == null) result = caseInputConstraint(realLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.BOOLEAN_LITERAL:
      {
        BooleanLiteral booleanLiteral = (BooleanLiteral)theEObject;
        T result = caseBooleanLiteral(booleanLiteral);
        if (result == null) result = caseScalarExpression(booleanLiteral);
        if (result == null) result = caseExpression(booleanLiteral);
        if (result == null) result = caseInputConstraint(booleanLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.BINARY_EXPRESSION:
      {
        BinaryExpression binaryExpression = (BinaryExpression)theEObject;
        T result = caseBinaryExpression(binaryExpression);
        if (result == null) result = caseScalarExpression(binaryExpression);
        if (result == null) result = caseExpression(binaryExpression);
        if (result == null) result = caseInputConstraint(binaryExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.PRE_EXPRESSION:
      {
        PreExpression preExpression = (PreExpression)theEObject;
        T result = casePreExpression(preExpression);
        if (result == null) result = caseScalarExpression(preExpression);
        if (result == null) result = caseExpression(preExpression);
        if (result == null) result = caseInputConstraint(preExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.RANDOM_INTEGER_EXPRESSION:
      {
        RandomIntegerExpression randomIntegerExpression = (RandomIntegerExpression)theEObject;
        T result = caseRandomIntegerExpression(randomIntegerExpression);
        if (result == null) result = caseRandomExpression(randomIntegerExpression);
        if (result == null) result = caseScalarExpression(randomIntegerExpression);
        if (result == null) result = caseExpression(randomIntegerExpression);
        if (result == null) result = caseInputConstraint(randomIntegerExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.RANDOM_REAL_EXPRESSION:
      {
        RandomRealExpression randomRealExpression = (RandomRealExpression)theEObject;
        T result = caseRandomRealExpression(randomRealExpression);
        if (result == null) result = caseRandomExpression(randomRealExpression);
        if (result == null) result = caseScalarExpression(randomRealExpression);
        if (result == null) result = caseExpression(randomRealExpression);
        if (result == null) result = caseInputConstraint(randomRealExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.RANDOM_ELEMENT_EXPRESSION:
      {
        RandomElementExpression randomElementExpression = (RandomElementExpression)theEObject;
        T result = caseRandomElementExpression(randomElementExpression);
        if (result == null) result = caseRandomExpression(randomElementExpression);
        if (result == null) result = caseScalarExpression(randomElementExpression);
        if (result == null) result = caseExpression(randomElementExpression);
        if (result == null) result = caseInputConstraint(randomElementExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.CONST_REF_EXPRESSION:
      {
        ConstRefExpression constRefExpression = (ConstRefExpression)theEObject;
        T result = caseConstRefExpression(constRefExpression);
        if (result == null) result = caseRefExpression(constRefExpression);
        if (result == null) result = caseScalarExpression(constRefExpression);
        if (result == null) result = caseExpression(constRefExpression);
        if (result == null) result = caseInputConstraint(constRefExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case InputConstraintPackage.NEGATIVE_EXPRESSION:
      {
        NegativeExpression negativeExpression = (NegativeExpression)theEObject;
        T result = caseNegativeExpression(negativeExpression);
        if (result == null) result = caseScalarExpression(negativeExpression);
        if (result == null) result = caseExpression(negativeExpression);
        if (result == null) result = caseInputConstraint(negativeExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Input Constraint</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Input Constraint</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInputConstraint(InputConstraint object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpression(Expression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Scalar Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Scalar Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseScalarExpression(ScalarExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Random Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Random Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRandomExpression(RandomExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Ref Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Ref Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRefExpression(RefExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Element Ref Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Element Ref Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseElementRefExpression(ElementRefExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Interval Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Interval Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntervalExpression(IntervalExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Set Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Set Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSetExpression(SetExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Integer Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Integer Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntegerLiteral(IntegerLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Real Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Real Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRealLiteral(RealLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Boolean Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Boolean Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBooleanLiteral(BooleanLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBinaryExpression(BinaryExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Pre Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Pre Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePreExpression(PreExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Random Integer Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Random Integer Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRandomIntegerExpression(RandomIntegerExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Random Real Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Random Real Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRandomRealExpression(RandomRealExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Random Element Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Random Element Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRandomElementExpression(RandomElementExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Const Ref Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Const Ref Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseConstRefExpression(ConstRefExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Negative Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Negative Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNegativeExpression(NegativeExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //InputConstraintSwitch

/**
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.specmodels.model;

import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.ShouldUpdate;
import com.facebook.litho.annotations.State;
import com.facebook.litho.annotations.TreeProp;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.ExecutableElement;

/**
 * Factory for creating {@link MethodParamModel}s.
 */
public final class MethodParamModelFactory {

  public static MethodParamModel create(
      ExecutableElement method,
      TypeName type,
      String name,
      List<Annotation> annotations,
      List<AnnotationSpec> externalAnnotations,
      List<Class<? extends Annotation>> permittedInterStateInputAnnotations,
      Object representedObject) {
    final SimpleMethodParamModel simpleMethodParamModel =
        new SimpleMethodParamModel(type, name, annotations, externalAnnotations, representedObject);
    final TypeName typeName = simpleMethodParamModel.getType();

    // We check whether we're calling ShouldUpdate here since it uses a different infrastructure to
    // track previous props/state :(
    if (typeName instanceof ParameterizedTypeName &&
        ((ParameterizedTypeName) typeName).rawType.equals(ClassNames.DIFF) &&
        method.getAnnotation(ShouldUpdate.class) == null) {
      return new DiffModel(simpleMethodParamModel, true);
    }

    for (Annotation annotation : annotations) {
      if (annotation instanceof Prop) {
        return new PropModel(
            simpleMethodParamModel,
            ((Prop) annotation).optional(),
            ((Prop) annotation).resType(),
            ((Prop) annotation).varArg());
      }

      if (annotation instanceof State) {
        return new StateParamModel(simpleMethodParamModel, ((State) annotation).canUpdateLazily());
      }

      if (annotation instanceof TreeProp) {
        return new TreePropModel(simpleMethodParamModel);
      }

      if (permittedInterStateInputAnnotations.contains(annotation.annotationType())) {
        return new InterStageInputParamModel(simpleMethodParamModel);
      }
    }

    return simpleMethodParamModel;
  }
}

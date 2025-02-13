// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.codeInspection;

import com.intellij.analysis.AnalysisBundle;
import com.intellij.codeInsight.intention.PriorityAction;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.icons.AllIcons;
import com.intellij.modcommand.ActionContext;
import com.intellij.modcommand.ModCommand;
import com.intellij.modcommand.ModCommandAction;
import com.intellij.modcommand.ModCommands;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpdateInspectionOptionFix implements ModCommandAction {
  private final LocalInspectionTool myInspection;
  private final String myProperty;
  private final @IntentionName String myMessage;
  private final Object myValue;

  public UpdateInspectionOptionFix(@NotNull LocalInspectionTool inspection, @NotNull @NonNls String property, @NotNull @IntentionName String message, boolean value) {
    myInspection = inspection;
    myProperty = property;
    myMessage = message;
    myValue = value;
  }

  public UpdateInspectionOptionFix(@NotNull LocalInspectionTool inspection, @NotNull @NonNls String property, @NotNull @IntentionName String message, int value) {
    myInspection = inspection;
    myProperty = property;
    myMessage = message;
    myValue = value;
  }

  @Override
  public @Nullable Presentation getPresentation(@NotNull ActionContext context) {
    return Presentation.of(myMessage).withPriority(PriorityAction.Priority.LOW).withIcon(AllIcons.Actions.Cancel);
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return AnalysisBundle.message("set.inspection.option.fix");
  }

  @Override
  public @NotNull ModCommand perform(@NotNull ActionContext context) {
    return ModCommands.updateOption(context.file(), myInspection, tool -> {
      tool.getOptionController().setOption(myProperty, myValue);
    });
  }
}

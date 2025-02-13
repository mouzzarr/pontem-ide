// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.feedback.kafka.dialog

import com.intellij.feedback.kafka.bundle.KafkaFeedbackBundle
import com.intellij.openapi.project.Project

class KafkaConsumerFeedbackDialog(project: Project?, forTest: Boolean) :
  KafkaConsumerProducerFeedbackDialog(project, forTest,
                                      KafkaFeedbackBundle.message("consumer.dialog.description")) {

  override val myTitle: String = KafkaFeedbackBundle.message("dialog.top.title")
}
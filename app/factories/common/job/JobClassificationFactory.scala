package factories.common.job

import domain.common.job.JobClassification

class JobClassification {
  def createJobClassification(values:Map[String, String]):JobClassification={
  JobClassification(jobClassificationId = values("jobClassificationId"),
                        currentTitle = values("currentTitle"),
                        oldTitle = values("oldTitle"),
                        oldCode = values("oldCode"),
                        currentCode = values("currentCode"),
                        codeConversion = values("codeConversion"),
                        comment = values("comment"))
  }

}
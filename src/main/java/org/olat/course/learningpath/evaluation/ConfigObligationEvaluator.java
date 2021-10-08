/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.course.learningpath.evaluation;

import java.util.List;
import java.util.Set;

import org.olat.core.CoreSpringFactory;
import org.olat.course.learningpath.LearningPathConfigs;
import org.olat.course.learningpath.LearningPathService;
import org.olat.course.learningpath.obligation.ExceptionalObligation;
import org.olat.course.nodes.CourseNode;
import org.olat.course.run.scoring.AssessmentEvaluation;
import org.olat.course.run.scoring.ObligationEvaluator;
import org.olat.modules.assessment.ObligationOverridable;
import org.olat.modules.assessment.model.AssessmentObligation;

/**
 * 
 * Initial date: 1 Sep 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class ConfigObligationEvaluator implements ObligationEvaluator {
	
	private LearningPathService learningPathService;
	
	@Override
	public ObligationOverridable getObligation(AssessmentEvaluation currentEvaluation,
			CourseNode courseNode, ExceptionalObligationEvaluator exceptionalObligationEvaluator) {
		LearningPathConfigs configs = getLearningPathService().getConfigs(courseNode);
		// Initialize the obligation by the configured standard obligation
		AssessmentObligation evaluatedObligation = configs.getObligation();
		
		// Check if the user is affected by a exceptional obligation
		List<ExceptionalObligation> exceptionalObligations = configs.getExceptionalObligations();
		if (exceptionalObligations != null && !exceptionalObligations.isEmpty()) {
			Set<AssessmentObligation> filtered = exceptionalObligationEvaluator
					.filterAssessmentObligation(exceptionalObligations, evaluatedObligation);
			evaluatedObligation = getMostImportantExceptionalObligation(filtered, evaluatedObligation);
		}
		
		ObligationOverridable obligation = currentEvaluation.getObligation().clone();
		obligation.setCurrent(evaluatedObligation);
		return obligation;
	}

	@Override
	public ObligationOverridable getObligation(AssessmentEvaluation currentEvaluation,
			CourseNode courseNode, ExceptionalObligationEvaluator exceptionalObligationEvaluator, List<AssessmentEvaluation> children) {
		return currentEvaluation.getObligation();
	}
	
	@Override
	public AssessmentObligation getMostImportantExceptionalObligation(Set<AssessmentObligation> assessmentObligations,
			AssessmentObligation defaultObligation) {
		if (!assessmentObligations.isEmpty()) {
			if (AssessmentObligation.mandatory == defaultObligation) {
				defaultObligation = assessmentObligations.contains(AssessmentObligation.optional)
						? AssessmentObligation.optional
						: AssessmentObligation.excluded;
			} else if (AssessmentObligation.optional == defaultObligation) {
				defaultObligation = assessmentObligations.contains(AssessmentObligation.mandatory)
						? AssessmentObligation.mandatory
						: AssessmentObligation.excluded;
			} else if (AssessmentObligation.excluded == defaultObligation) {
				defaultObligation = assessmentObligations.contains(AssessmentObligation.mandatory)
						? AssessmentObligation.mandatory
						: AssessmentObligation.optional;
			}
		}
		return defaultObligation;
	}


	protected LearningPathService getLearningPathService() {
		if (learningPathService == null) {
			learningPathService = CoreSpringFactory.getImpl(LearningPathService.class);
		}
		return learningPathService;
	}

}

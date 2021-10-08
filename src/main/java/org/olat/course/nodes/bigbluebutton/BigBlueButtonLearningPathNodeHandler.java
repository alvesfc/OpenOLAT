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
package org.olat.course.nodes.bigbluebutton;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.util.nodes.INode;
import org.olat.course.learningpath.FullyAssessedTrigger;
import org.olat.course.learningpath.LearningPathEditConfigs;
import org.olat.course.learningpath.LearningPathNodeHandler;
import org.olat.course.learningpath.model.ModuleLearningPathConfigs;
import org.olat.course.learningpath.ui.LearningPathNodeConfigController;
import org.olat.course.nodes.BigBlueButtonCourseNode;
import org.olat.course.nodes.CourseNode;
import org.olat.repository.RepositoryEntry;
import org.springframework.stereotype.Service;

/**
 * 
 * Initial date: 20 mars 2020<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
@Service
public class BigBlueButtonLearningPathNodeHandler implements LearningPathNodeHandler {

	private static final LearningPathEditConfigs EDIT_CONFIGS = LearningPathEditConfigs.builder()
			.enableNodeVisited()
			.enableConfirmed()
			.build();
	
	@Override
	public String acceptCourseNodeType() {
		return BigBlueButtonCourseNode.TYPE;
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public void updateDefaultConfigs(CourseNode courseNode, boolean newNode, INode parent) {
		getLearningPathConfigs(courseNode, newNode);
	}

	@Override
	public ModuleLearningPathConfigs getConfigs(CourseNode courseNode) {
		return getLearningPathConfigs(courseNode, false);
	}

	private ModuleLearningPathConfigs getLearningPathConfigs(CourseNode courseNode, boolean newNode) {
		ModuleLearningPathConfigs configs = new ModuleLearningPathConfigs(courseNode.getModuleConfiguration(), true);
		configs.updateDefaults(newNode, FullyAssessedTrigger.nodeVisited);
		return configs;
	}

	@Override
	public Controller createConfigEditController(UserRequest ureq, WindowControl wControl, RepositoryEntry courseEntry,
			CourseNode courseNode) {
		return new LearningPathNodeConfigController(ureq, wControl, courseEntry, courseNode, EDIT_CONFIGS);
	}

	@Override
	public LearningPathEditConfigs getEditConfigs() {
		return EDIT_CONFIGS;
	}

	@Override
	public void onMigrated(CourseNode courseNode) {
		//
	}

}

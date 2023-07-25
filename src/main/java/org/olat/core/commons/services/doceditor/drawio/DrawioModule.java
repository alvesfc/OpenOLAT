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
package org.olat.core.commons.services.doceditor.drawio;

import org.olat.core.configuration.AbstractSpringModule;
import org.olat.core.configuration.ConfigOnOff;
import org.olat.core.util.StringHelper;
import org.olat.core.util.coordinate.CoordinatorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * Initial date: 21.07.2023<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
@Service
public class DrawioModule extends AbstractSpringModule implements ConfigOnOff {

	public static final String DRAWIO_ENABLED = "drawio.enabled";
	public static final String DRAWIO_EDITOR_URL = "drawio.editorUrl";
	private static final String DRAWIO_DATA_TRANSER_CONFIRMATION_ENABLED = "drawio.data.transfer.confirmation.enabled";

	@Value("${drawio.enabled:false}")
	private boolean enabled;
	@Value("${drawio.editorUrl}")
	private String editorUrl;
	@Value("${drawio.transfer.confirmation.enabled:true}")
	private boolean dataTransferConfirmationEnabled;

	@Autowired
	public DrawioModule(CoordinatorManager coordinatorManager) {
		super(coordinatorManager);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void init() {
		String enabledObj = getStringPropertyValue(DRAWIO_ENABLED, true);
		if (StringHelper.containsNonWhitespace(enabledObj)) {
			enabled = "true".equals(enabledObj);
		}

		String editorhUrlObj = getStringPropertyValue(DRAWIO_EDITOR_URL, true);
		if (StringHelper.containsNonWhitespace(editorhUrlObj)) {
			editorUrl = editorhUrlObj;
		}
		
		String dataTransferConfirmationEnabledObj = getStringPropertyValue(DRAWIO_DATA_TRANSER_CONFIRMATION_ENABLED, true);
		if(StringHelper.containsNonWhitespace(dataTransferConfirmationEnabledObj)) {
			dataTransferConfirmationEnabled = "true".equals(dataTransferConfirmationEnabledObj);
		}
	}

	@Override
	protected void initFromChangedProperties() {
		init();
	}

	public String getEditorUrl() {
		return editorUrl;
	}

	public void setEditorUrl(String editorUrl) {
		this.editorUrl = editorUrl;
		setStringProperty(DRAWIO_EDITOR_URL, editorUrl, true);
	}

	public boolean isDataTransferConfirmationEnabled() {
		return dataTransferConfirmationEnabled;
	}

	public void setDataTransferConfirmationEnabled(boolean dataTransferConfirmationEnabled) {
		this.dataTransferConfirmationEnabled = dataTransferConfirmationEnabled;
		setStringProperty(DRAWIO_DATA_TRANSER_CONFIRMATION_ENABLED, Boolean.toString(dataTransferConfirmationEnabled), true);
	}

}
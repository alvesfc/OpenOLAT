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
package org.olat.portfolio.site;

import org.olat.core.CoreSpringFactory;
import org.olat.core.gui.UserRequest;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.gui.control.navigation.AbstractSiteDefinition;
import org.olat.core.gui.control.navigation.SiteConfiguration;
import org.olat.core.gui.control.navigation.SiteDefinition;
import org.olat.core.gui.control.navigation.SiteInstance;
import org.olat.core.util.StringHelper;
import org.olat.portfolio.PortfolioModule;

/**
 * 
 * Initial date: 25.03.2014<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class EPSiteDef  extends AbstractSiteDefinition implements SiteDefinition {

	@Override
	protected SiteInstance createSite(UserRequest ureq, WindowControl wControl, SiteConfiguration config) {
		if(StringHelper.containsNonWhitespace(config.getSecurityCallbackBeanId())) {
			return new EPSite(this, ureq.getLocale());
		} else if(!ureq.getUserSession().getRoles().isGuestOnly()) {
			// only for registered users and invitee but not guests
			return new EPSite(this, ureq.getLocale());
		}
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		PortfolioModule module = CoreSpringFactory.getImpl(PortfolioModule.class);
		return module.isEnabled() && super.isEnabled();
	}
}

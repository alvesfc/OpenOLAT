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
package org.olat.modules.lecture.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.olat.core.CoreSpringFactory;
import org.olat.core.gui.translator.Translator;
import org.olat.core.id.Identity;
import org.olat.core.logging.OLog;
import org.olat.core.logging.Tracing;
import org.olat.core.util.Formatter;
import org.olat.core.util.StringHelper;
import org.olat.core.util.openxml.OpenXMLWorkbook;
import org.olat.core.util.openxml.OpenXMLWorkbookResource;
import org.olat.core.util.openxml.OpenXMLWorksheet;
import org.olat.core.util.openxml.OpenXMLWorksheet.Row;
import org.olat.modules.lecture.LectureBlock;
import org.olat.modules.lecture.LectureBlockRollCall;
import org.olat.modules.lecture.LectureService;
import org.olat.user.UserManager;
import org.olat.user.propertyhandlers.UserPropertyHandler;

/**
 * 
 * Initial date: 7 avr. 2017<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class LectureBlockExport extends OpenXMLWorkbookResource {
	
	private static final OLog log = Tracing.createLoggerFor(LectureBlockExport.class);
	
	private final Translator translator;
	private final LectureBlock lectureBlock;
	private final LectureService lectureService;
	
	private final boolean authorizedAbsence;
	private final boolean isAdministrativeUser;
	private List<UserPropertyHandler> userPropertyHandlers;
	
	public LectureBlockExport(LectureBlock lectureBlock, boolean isAdministrativeUser, Translator translator) {
		super(label(lectureBlock));
		this.lectureBlock = lectureBlock;
		lectureService = CoreSpringFactory.getImpl(LectureService.class);
		this.isAdministrativeUser = isAdministrativeUser;
		this.authorizedAbsence = true;
		UserManager userManager = CoreSpringFactory.getImpl(UserManager.class);
		userPropertyHandlers = userManager.getUserPropertyHandlersFor(ParticipantListRepositoryController.USER_PROPS_ID, isAdministrativeUser);
		this.translator = userManager.getPropertyHandlerTranslator(translator);
	}
	
	private static final String label(LectureBlock lectureBlock) {
		return StringHelper.transformDisplayNameToFileSystemName(lectureBlock.getTitle())
				+ "_" + Formatter.formatDatetimeFilesystemSave(new Date(System.currentTimeMillis()))
				+ ".xlsx";
	}

	@Override
	protected void generate(OutputStream out) {
		try(OpenXMLWorkbook workbook = new OpenXMLWorkbook(out, 1)) {
			OpenXMLWorksheet exportSheet = workbook.nextWorksheet();
			addHeaders(exportSheet);
			addContent(exportSheet);
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	private void addContent(OpenXMLWorksheet exportSheet) {
		List<Identity> participants = lectureService.getParticipants(lectureBlock);
		List<LectureBlockRollCall> rollCalls = lectureService.getRollCalls(lectureBlock);
		Map<Identity,LectureBlockRollCall> participantToRollCallMap = rollCalls.stream()
				.collect(Collectors.toMap(r -> r.getIdentity(), r -> r));
		
		for(Identity participant:participants) {
			Row row = exportSheet.newRow();
			
			int pos = 0;
			if(isAdministrativeUser) {
				row.addCell(pos++, participant.getName());
			}
			
			for (UserPropertyHandler userPropertyHandler : userPropertyHandlers) {
				if (userPropertyHandler == null) continue;
				String val = userPropertyHandler.getUserProperty(participant.getUser(), translator.getLocale());
				row.addCell(pos++, val);
			}
			
			LectureBlockRollCall rollCall = participantToRollCallMap.get(participant);
			if(rollCall != null) {
				List<Integer> absentList = rollCall.getLecturesAbsentList();
				for(int i=0; i<lectureBlock.getPlannedLecturesNumber(); i++) {
					String val = absentList.contains(i) ? "x" : null;
					row.addCell(pos++, val);
				}
	
				if(authorizedAbsence && rollCall.getAbsenceAuthorized() != null
						&& rollCall.getAbsenceAuthorized().booleanValue()) {
					row.addCell(pos++, "x");
					row.addCell(pos++, rollCall.getAbsenceReason());
				}
			}
		}
	}
	
	private void addHeaders(OpenXMLWorksheet exportSheet) {
		Row headerRow = exportSheet.newRow();
		
		int pos = 0;
		if(isAdministrativeUser) {
			headerRow.addCell(pos++, translator.translate("table.header.username"));
		}
		
		for (UserPropertyHandler userPropertyHandler : userPropertyHandlers) {
			if (userPropertyHandler == null) continue;

			String propName = userPropertyHandler.getName();
			headerRow.addCell(pos++, translator.translate("form.name." + propName));
		}
		
		for(int i=0; i<lectureBlock.getPlannedLecturesNumber(); i++) {
			headerRow.addCell(pos++, Integer.toString(i + 1));	
		}
		
		if(authorizedAbsence) {
			//authorized absence
			headerRow.addCell(pos++, translator.translate("table.header.authorized.absence"));
			//authorized absence reason
			headerRow.addCell(pos++, translator.translate("authorized.absence.reason"));
		}
		
		//comment
		headerRow.addCell(pos++, translator.translate("table.header.comment"));
	}
}

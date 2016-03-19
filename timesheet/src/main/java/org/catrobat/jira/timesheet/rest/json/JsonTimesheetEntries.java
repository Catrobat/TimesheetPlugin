/*
 * Copyright 2015 Christof Rabensteiner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.catrobat.jira.timesheet.rest.json;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class JsonTimesheetEntries {

	@XmlElement
	private JsonTimesheetEntry[] entries;
	
  @XmlElement
	private String[] errorMessages;
	
  public JsonTimesheetEntries() {
  }

	public JsonTimesheetEntries(JsonTimesheetEntry[] entries, String[] errorMessages) {
		this.entries = entries;
		this.errorMessages = errorMessages;
	}

	public JsonTimesheetEntry[] getEntries() {
		return entries;
	}

	public void setEntries(JsonTimesheetEntry[] entries) {
		this.entries = entries;
	}
	
	public String[] getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String[] errorMessages) {
		this.errorMessages = errorMessages;
	}
}

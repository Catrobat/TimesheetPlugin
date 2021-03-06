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
package org.catrobat.confluence.rest.json;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class JsonTimesheetEntry {

  @XmlElement
	private int entryID;
	
  @XmlElement
	private Date beginDate;

  @XmlElement
	private Date endDate;

  @XmlElement
	private int pauseMinutes;
	
  @XmlElement
	private String description;
	
  @XmlElement
	private int teamID;
  
  @XmlElement
	private int categoryID;

  public JsonTimesheetEntry() {
  }

  public JsonTimesheetEntry(int entryID, Date beginDate, Date endDate, int pauseMinutes, String description, int teamID, int categoryID) {
    this.entryID = entryID;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.pauseMinutes = pauseMinutes;
    this.description = description;
    this.teamID = teamID;
    this.categoryID = categoryID;
  }
  
  public int getEntryID() {
    return entryID;
  }

  public void setEntryID(int entryID) {
    this.entryID = entryID;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public int getPauseMinutes() {
    return pauseMinutes;
  }

  public void setPauseMinutes(int pauseMinutes) {
    this.pauseMinutes = pauseMinutes;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getTeamID() {
    return teamID;
  }

  public void setTeamID(int teamID) {
    this.teamID = teamID;
  }

  public int getCategoryID() {
    return categoryID;
  }

  public void setCategoryID(int categoryID) {
    this.categoryID = categoryID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JsonTimesheetEntry that = (JsonTimesheetEntry) o;

    if (entryID != that.entryID) return false;
    if (pauseMinutes != that.pauseMinutes) return false;
    if (teamID != that.teamID) return false;
    if (categoryID != that.categoryID) return false;
    if (!beginDate.equals(that.beginDate)) return false;
    if (!endDate.equals(that.endDate)) return false;
    return description.equals(that.description);

  }

  @Override
  public int hashCode() {
    int result = entryID;
    result = 31 * result + beginDate.hashCode();
    result = 31 * result + endDate.hashCode();
    result = 31 * result + pauseMinutes;
    result = 31 * result + description.hashCode();
    result = 31 * result + teamID;
    result = 31 * result + categoryID;
    return result;
  }

  @Override
  public String toString() {
    return "JsonTimesheetEntry{" +
            "entryID=" + entryID +
            ", beginDate=" + beginDate +
            ", endDate=" + endDate +
            ", pauseMinutes=" + pauseMinutes +
            ", description='" + description + '\'' +
            ", teamID=" + teamID +
            ", categoryID=" + categoryID +
            '}';
  }
}

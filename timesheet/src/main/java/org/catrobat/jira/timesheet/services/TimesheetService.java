/*
 * Copyright 2016 Adrian Schnedlitz
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

package org.catrobat.jira.timesheet.services;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.jira.service.ServiceException;
import org.catrobat.jira.timesheet.activeobjects.Timesheet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Transactional
public interface TimesheetService {
    /**
     * Edits a existing Timesheet
     *
     * @param userKey             identifies the user
     * @param targetHoursPractice specifies the amount of hours the user has to
     *                            solve in practical work
     * @param targetHoursTheory   specifies the amount of hours the user has to
     *                            invest in theoretical work
     * @param lectures            describes the lecture in which the user is enrolled
     * @return the new Timesheet, or null
     */
    @Nullable
    Timesheet editTimesheet(String userKey, int targetHoursPractice, int targetHoursTheory,
                            int targetHours, int targetHoursCompleted, int targetHoursRemoved, String lectures,
                            String reason, int ects, String latestEntryDate, Boolean isActive, Boolean isEnabled);

    /**
     * Adds a new Timesheet
     *
     * @param userKey             identifies the user
     * @param targetHoursPractice specifies the amount of hours the user has to
     *                            solve in practical work
     * @param targetHoursTheory   specifies the amount of hours the user has to
     *                            invest in theoretical work
     * @param lectures            describes the lecture in which the user is enrolled
     * @return the new Timesheet
     */
    @Nonnull
    Timesheet add(String userKey, int targetHoursPractice, int targetHoursTheory,
                  int targetHours, int targetHoursCompleted, int targetHoursRemoved, String lectures,
                  String reason, int ects, String latestEntryDate, Boolean isActive, Boolean isEnabled);

    /**
     * Return all Timesheets
     *
     * @return
     */
    @Nonnull
    List<Timesheet> all();

    @Nullable
    Timesheet updateTimesheetEnableState(int timesheetID, Boolean isEnabled);

    /**
     * Returns Timesheet corresponding to a User
     *
     * @param userKey
     * @return Timesheet, null if unknown user
     */
    @Nullable
    Timesheet getTimesheetByUser(String userKey) throws ServiceException;

    /**
     * Returns a timesheet with the corresponding id
     *
     * @param id
     * @return
     */
    @Nullable
    Timesheet getTimesheetByID(int id) throws ServiceException;

}

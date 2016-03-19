/*
 * Copyright 2014 Stephan Fellhofer
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

package org.catrobat.jira.timesheet.helper;

import com.atlassian.sal.api.user.UserManager;
import org.catrobat.jira.timesheet.activeobjects.Timesheet;
import org.catrobat.jira.timesheet.activeobjects.TimesheetEntry;

import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public class CsvExporterAll {

    public static final String DELIMITER = ";";
    public static final String NEW_LINE = "\n";
    private final List<Timesheet> timesheetList;
    private final UserManager userManager;

    public CsvExporterAll(final List<Timesheet> timesheetList, final UserManager userManager) {
        this.timesheetList = timesheetList;
        this.userManager = userManager;
    }

    public String getCsvString() {
        StringBuilder sb = new StringBuilder();

        for (Timesheet timesheet : timesheetList) {
            sb.append("User Key" + DELIMITER +
                    "Practise Hours" + DELIMITER +
                    "Theory Hours" + DELIMITER +
                    "Lecture" + NEW_LINE);

            sb.append(timesheet.getUserKey()).append(DELIMITER);
            sb.append(timesheet.getTargetHoursPractice()).append(DELIMITER);
            sb.append(timesheet.getTargetHoursTheory()).append(DELIMITER);
            sb.append(timesheet.getLectures()).append(NEW_LINE);

            for(TimesheetEntry timesheetEntry : timesheet.getEntries()){
                sb.append("Begin Date" + DELIMITER +
                        "End Date" + DELIMITER +
                        "Pause Minutes" + DELIMITER +
                        "Duration Minutes" + DELIMITER +
                        "Team" + DELIMITER +
                        "Category" + DELIMITER +
                        "Description" + NEW_LINE);

                sb.append(unescape(timesheetEntry.getBeginDate().toString())).append(DELIMITER);
                sb.append(unescape(timesheetEntry.getEndDate().toString())).append(DELIMITER);
                sb.append(unescape(Integer.toString(timesheetEntry.getPauseMinutes()))).append(DELIMITER);
                sb.append(unescape(Integer.toString(timesheetEntry.getDurationMinutes()))).append(DELIMITER);
                sb.append(unescape(timesheetEntry.getTeam().toString())).append(DELIMITER);
                sb.append(unescape(timesheetEntry.getCategory().toString())).append(DELIMITER);
                sb.append(unescape(timesheetEntry.getDescription().toString())).append(NEW_LINE);
            }
        }

        return sb.toString();
    }

    private String unescape(String escapedHtml4String) {
        if (escapedHtml4String == null || escapedHtml4String.trim().length() == 0) {
            return "\"\"";
        } else {
            return "\"" + unescapeHtml4(escapedHtml4String).replaceAll("\"", "\"\"") + "\"";
        }
    }
}

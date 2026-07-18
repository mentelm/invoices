# Time Tracking

Pulls raw time entries from Toggl and groups them into Work Items, ready to become Invoicing's line items.

## Language

**Work Item**:
The thing time entries are grouped by. Its name is either free text from Toggl or, when detected, a Jira Issue Code.
_Avoid_: Task (implies it's always a Toggl task, when it may represent a Jira issue instead)

**Jira Issue Code**:
An issue key (e.g. `PROJ-123`) extracted from a Work Item's free-text name, used to give the Work Item a precise, traceable name.

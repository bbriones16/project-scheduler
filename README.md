# project-scheduler
Determine start and end dates of project tasks depending on dependencies and duration

1. We need to calculate calendar schedules for project plans

2. Each project plan consists of tasks. Every task has a certain duration. 

3. A task can depend on zero or more other tasks. If a task depends on some other tasks, it can only be started after these tasks are completed

4. So, for a set of tasks (with durations and dependencies), the solution for the challenge should generate a schedule, i.e. assign Start and End Dates for every task

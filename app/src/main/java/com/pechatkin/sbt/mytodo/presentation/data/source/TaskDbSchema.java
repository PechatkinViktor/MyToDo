package com.pechatkin.sbt.mytodo.presentation.data.source;

class TaskDbSchema {

    static final class TaskTable {

        static final String NAME = "tasks";

        static final class Cols {

            static final String TASK_NAME = "task_name";
            static final String TASK_STATUS = "status";
        }
    }
}

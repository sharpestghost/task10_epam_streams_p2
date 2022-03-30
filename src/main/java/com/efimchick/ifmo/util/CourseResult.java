package com.efimchick.ifmo.util;

import java.util.Map;
import java.util.Set;

public class CourseResult {
    private final Person person;
    private final Map<String, Integer> taskResults;

    public CourseResult(final Person person, final Map<String, Integer> taskResults) {
        this.person = person;
        this.taskResults = taskResults;
    }

    public Person getPerson() {
        return person;
    }

    public Map<String, Integer> getTaskResults() {
        return taskResults;
    }

    public Set<String> getTaskList() {
        return taskResults.keySet();
    }

    public Double getTotalResults() {
        return taskResults.values().stream().mapToDouble(Integer::doubleValue).sum();
    }


}

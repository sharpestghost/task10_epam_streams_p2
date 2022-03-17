package com.efimchick.ifmo.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Double getAverageResults() {
        System.out.println(taskResults.values().stream().max(Comparator.naturalOrder()));
        DoubleSummaryStatistics stats = taskResults.values().stream().mapToDouble(Integer::doubleValue).summaryStatistics();
        return stats.getAverage();
    }
}

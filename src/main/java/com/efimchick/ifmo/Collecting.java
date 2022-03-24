package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Collecting {

    private static final int DIVISIER_OF_EVEN = 2;
    private static final int A_GRADE_SCORE = 90;
    private static final int B_GRADE_SCORE = 83;
    private static final int C_GRADE_SCORE = 75;
    private static final int D_GRADE_SCORE = 68;
    private static final int E_GRADE_SCORE = 60;

    public Collecting() { }

    //### Integer Streams
    public int production(IntStream limit) {
        return limit.reduce(1, (a, b) -> a * b);
    }

    public int sum(IntStream limit) {
        return limit.sum();
    }
    public int oddSum(IntStream limit) {
        return limit.filter(this::isOdd).sum();
    }

    public boolean isOdd(int value) {
        return value % DIVISIER_OF_EVEN != 0;
    }

    public Map<Integer, Integer> sumByRemainder(int i, IntStream limit) {
        return limit.boxed().collect(Collectors.groupingBy(s -> s % i,
                Collectors.summingInt(x -> x)));
    }

    //### Course Result Streams (main methods)
    public Map<Person, Double> totalScores(Stream<CourseResult> programmingResults) {
        Map<Person, Double> scoreMap = new HashMap<>();
        CourseResult[] courseResults = programmingResults.toArray(CourseResult[]::new);
        int taskCount = getTaskCount(courseResults);
        Arrays.stream(courseResults).forEach(courseResult -> scoreMap.put(courseResult.getPerson(),
                courseResult.getTotalResults() / taskCount));
        return scoreMap;
    }

    public double averageTotalScore(Stream<CourseResult> programmingResults) {
        DoubleSummaryStatistics stats = totalScores(programmingResults).values().stream()
                .mapToDouble(Double::doubleValue).summaryStatistics();
        return stats.getAverage();
    }

    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> programmingResults) {
        CourseResult[] courseResults = programmingResults.toArray(CourseResult[]::new);
        Map<String, Double> averageScoresMap = new HashMap<>();
        Map<String, List<Integer>> examResults = getAllExamResults(courseResults);
        examResults.forEach((key, value) -> averageScoresMap.put(key, value.stream()
                .mapToDouble(a -> a).sum() / courseResults.length));
        return averageScoresMap;
    }

    public String easiestTask(Stream<CourseResult> programmingResults) {
        return Collections.max(averageScoresPerTask(programmingResults).entrySet(),
                Comparator.comparingDouble(Map.Entry::getValue)).getKey();
    }

    public Map<Person, String> defineMarks(Stream<CourseResult> programmingResults) {
        Map<Person, String> anotherMap = new HashMap<>();
        totalScores(programmingResults).forEach((key, value) -> anotherMap.put(key, getGrade(value)));
        return anotherMap;
    }

    //### Course Result Streams (auxiliary methods)
    private Map<String,List<Integer>> getAllExamResults(CourseResult[] programmingResults) {
        Map<String,List<Integer>> taskResultsMap = new HashMap<>();
        Arrays.stream(programmingResults).forEach((CourseResult courseResult) -> {
            Map<String, Integer> taskResultsByStudent = courseResult.getTaskResults();
            taskResultsByStudent.forEach((String task, Integer taskResult) -> {
                if (!taskResultsMap.containsKey(task)) {
                    taskResultsMap.put(task, new ArrayList<>());
                }
                taskResultsMap.get(task).add(taskResult);
            });
        });
        return taskResultsMap;
    }

    private int getTaskCount(CourseResult[] programmingResults) {
        return getAllExamResults(programmingResults).size();
    }

    private String getGrade(Double value) {
        String grade;
        if (value > A_GRADE_SCORE) {
            grade = "A";
        } else if (value >= B_GRADE_SCORE) {
            grade = "B";
        } else if (value >= C_GRADE_SCORE) {
            grade = "C";
        } else if (value >= D_GRADE_SCORE) {
            grade = "D";
        } else if (value >= E_GRADE_SCORE) {
            grade = "E";
        } else {
            grade = "F";
        }
        return grade;
    }
}

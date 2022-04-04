package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
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
    private static final String RESULT_SEPARATOR = " | ";

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
        Set<String> taskList = new TreeSet<>();
        List<CourseResult> courseResultList = programmingResults.collect(Collectors.toList());
        //list of tasks is selected, then the total and average score for each student is found
        courseResultList.forEach(courseResult -> taskList.addAll(courseResult.getTaskList()));
        courseResultList.forEach(courseResult -> scoreMap.put(courseResult.getPerson(),
                courseResult.getTotalResults() / taskList.size()));
        return scoreMap;
    }

    public double averageTotalScore(Stream<CourseResult> programmingResults) {
        DoubleSummaryStatistics stats = totalScores(programmingResults).values().stream()
                .mapToDouble(Double::doubleValue).summaryStatistics();
        return stats.getAverage();
    }

    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> programmingResults) {
        Map<String, List<Integer>> scoreMap = new TreeMap<>();
        List<CourseResult> courseResultList = programmingResults.collect(Collectors.toList());
        //filling map with tasks
        courseResultList.forEach((CourseResult courseResult) -> courseResult.getTaskResults().keySet().
                forEach((String task) -> scoreMap.put(task, new ArrayList<>())));
        //filling map with scores for each task
        courseResultList.forEach((CourseResult courseResult) ->
                courseResult.getTaskResults().forEach((String task, Integer score) -> scoreMap.get(task).add(score)));
        //return map with average score per task
        return scoreMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().stream().mapToDouble(a -> a).sum() / courseResultList.size()));
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

    //### Printable String (not completed)
    public Collector<CourseResult, StringJoiner, String> printableStringCollector() {
        return Collector.of(() -> new StringJoiner("\n"),
                (StringJoiner joiner, CourseResult courseResult) ->
                        joiner.add(formatStudentResults(courseResult)),  // accumulator
                StringJoiner::merge,     // combiner
                StringJoiner::toString);
    }

    private String formatStudentResults(CourseResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.getPerson().getFullName()).append(RESULT_SEPARATOR);
        result.getTaskResults().forEach((String key, Integer value) ->
            sb.append(value).append(RESULT_SEPARATOR)
        );
        return sb.toString();
    }
}

package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collecting {
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
        return value % 2 != 0;
    }

    public Map<Integer, Integer> sumByRemainder(int i, IntStream limit) {
        return limit.boxed().collect(Collectors.groupingBy(s -> s % i,
                Collectors.summingInt(x -> x)));
    }

    public Map<Person, Double> totalScores(Stream<CourseResult> programmingResults) {
        Stream<CourseResult> d = programmingResults;
        CourseResult[] arr = d.toArray(CourseResult[]::new);
        return programmingResults.collect(Collectors.toMap(
                CourseResult::getPerson, CourseResult::getAverageResults));

    }

    public double averageTotalScore(Stream<CourseResult> programmingResults) {
        return totalScores(programmingResults).values().stream().mapToDouble(Double::doubleValue).summaryStatistics().getAverage();
    }

    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> programmingResults) {
        return null;
    }

    public double defineMarks(Stream<CourseResult> programmingResults) {
        return 0.0;
    }

    public String easiestTask(Stream<CourseResult> programmingResults) {
        return null;
    }

    public Stream printableStringCollector() {
        return Stream.empty();
    }
}
